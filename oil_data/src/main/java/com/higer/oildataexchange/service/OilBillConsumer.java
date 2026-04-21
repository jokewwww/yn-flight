package com.higer.oildataexchange.service;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.higer.oildataexchange.common.*;
import com.higer.oildataexchange.entity.R;
import com.higer.oildataexchange.entity.TCustom;
import com.higer.oildataexchange.entity.flight.TAirportCode;
import com.higer.oildataexchange.entity.flight.TFlight;
import com.higer.oildataexchange.entity.oil.FSSD;
import com.higer.oildataexchange.entity.oil.FSSD_R;
import com.higer.oildataexchange.entity.oil.TFuelRecpt;
import com.higer.oildataexchange.entity.orderInfo.TOrderInfo;
import com.higer.oildataexchange.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.Optional;

import static com.higer.oildataexchange.common.Constant.YYYY_MM_DD_HH_MM_SS_SSS;

@Service
@Slf4j
public class OilBillConsumer {

    @Autowired
    private TFuelRecptRepository recptRepository;

    @Autowired
    private SendHttpService httpService;

    @Autowired
    private TAirportCodeRepository airportCodeRepository;

    @Value(value = "${oilUploadUrl}")
    private String oilUploadUrl;

    @Autowired
    private TFuelRecptRepository kafkaFuelRepository;

    @Autowired
    private SendRedisService sendRedisService;

    @Autowired
    private TOrderInfoRepository tOrderInfoRepository;

    @Autowired
    private TCustomRepository customRepository;

    @Autowired
    private TFlightRepository tFlightRepository;

    @Value(value = "${paperless.cus}")
    private String paperlessCus;

    @KafkaListener(topics = {"${oil-bill.upload.topic}"})
    public void listener(ConsumerRecord<String, String> record, Acknowledgment ack) {
        TFuelRecpt kafkaFuel = null;
        TFuelRecpt fuelRecpt = null;
        Boolean wrong = true;
        try {
            log.info("油单数据;" + record.value());

            kafkaFuel = JSONObject.parseObject(record.value(), TFuelRecpt.class);
            Assert.notNull(kafkaFuel.getFlrcId(), "油单id不能为空！！");
            fuelRecpt = kafkaFuelRepository.findByFlrcId(kafkaFuel.getFlrcId());

            if (fuelRecpt == null) {
                throw new Exception("智慧航油系统:未找到油单信息,加油单id" + kafkaFuel.getFlrcId());
            }

            if (StringUtils.isEmpty(kafkaFuel.getFlrcDest())) {
                throw new Exception("智慧航油系统:目的地机场不可为空");
            }


            FSSD fssd = formatFSSDData(kafkaFuel);

            if (StringUtils.isEmpty(fssd.getDES3())) {
                throw new Exception("智慧航油系统:目的地三字码没匹配到请检查目的地机场");
            }

            /**------生成pdf-------**/
            //   if (StringUtils.isEmpty(kafkaFuel.getFlrcSingle())){
            ClassPathResource resource = new ClassPathResource("static/template.pdf");
            InputStream inputStream = resource.getInputStream();
            File file = File.createTempFile("template_export_copy", ".pdf");
            try {
                FileUtils.copyInputStreamToFile(inputStream, file);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
            byte[] bytes = PDFUtils.fillTemplate(kafkaFuel, file);
            if (bytes != null) {
                String base64 = Base64Utils.encodeToString(bytes);
                //FileUtils.writeByteArrayToFile(new File("D:\\Download\\hh.pdf"),bytes);
                //PDFUtils.manipulatePdf( "D:\\Download\\hh.pdf","D:\\Download\\hhnew.pdf");
                fssd.setEFS(base64);
            }
           /* }else{
                fssd.setEFS(fuelRecpt.getFlrcSingle());
            }*/

            kafkaFuel.setFlrcVersion(null);
            kafkaFuel.setFlrcSingle(null);
            ModelAssistant.copyProperties(kafkaFuel, fuelRecpt);
            fuelRecpt.setFlrcVersion(fuelRecpt.getFlrcVersion() == null ? 1 : (fuelRecpt.getFlrcVersion() + 1));
            fssd.setVRSN(fuelRecpt.getFlrcVersion().toString());

            FSSD logFssd = new FSSD();
            //copy 用于输出日志 忽略efs太长了
            ModelAssistant.copyProperties(fssd, logFssd);

            if (StringUtils.isEmpty(logFssd.getDES3())) {
                wrong = true;
                new Exception("智慧航油系统 ; 目的地机场存在问题,请核对后重新上传");
            }

            R<FSSD> fssdr = R.newInstanceR("CNAF", "CZ", fssd);
            R<FSSD> logFssdr = R.newInstanceR("CNAF", "CZ", logFssd);
            log.info("油单上传参数：" + JSON.toJSONString(logFssdr));
            String xml = XmlUtils.convertToXml(fssdr, Constant.CHARSET, true);
            wrong = false;
            String s = httpService.sendHttpPost(oilUploadUrl, xml);
            log.info("油单上传，返回信息:{}", s);
            Document document = DocumentHelper.parseText(s);
            Element rootElement = document.getRootElement();
            Element bd = rootElement.element("BD");
            FSSD_R fssdR = XmlUtils.convertToObj(bd.asXML(), FSSD_R.class);
            //0未上传  1 上传失败 2 上传成功 3 新增 4 修改
            if (fssdR != null) {
                if (StringUtils.equals(fssdR.getPTST(), "S")) {//成功
                    fuelRecpt.setFlrcStatus(2);
                    fuelRecpt.setFlrcUpdateErrMsg("");
                    log.info("油单上传成功：{}", fssdR.getFSID());
                } else {
                    fuelRecpt.setFlrcStatus(1);//失败
                    fuelRecpt.setFlrcUpdateErrMsg("结算平台返回:" + fssdR.getFRSN());//失败原因
                    log.error("油单上传失败：{}", fssdR.getFSID());
                }
            } else {
                fuelRecpt.setFlrcStatus(1);//失败
                fuelRecpt.setFlrcUpdateErrMsg("智慧航油系统:上传失败，请联系管理员");//失败原因
                log.error("油单上传失败：{}", fuelRecpt.getFlrcId());
            }
            fuelRecpt.setFlrcRecCreTime(new Date());
            fuelRecpt.setFlrcSingle(fssd.getEFS());

            // todo 成功不提示哦
            // sendRedisService.sendRedisMsg(recptRepository.save(fuelRecpt));
            recptRepository.save(fuelRecpt);

            ack.acknowledge();

        } catch (Exception e) {
            if (kafkaFuel != null && fuelRecpt != null) {
                fuelRecpt.setFlrcRecCreTime(new Date());
                fuelRecpt.setFlrcStatus(1);//失败
                if (wrong) {
                    fuelRecpt.setFlrcUpdateErrMsg("智慧航油系统:" + e.getMessage());//失败原因
                } else {
                    fuelRecpt.setFlrcUpdateErrMsg("云平台:" + e.getMessage());//失败原因
                }
                sendRedisService.sendRedisMsg(recptRepository.save(fuelRecpt));
                log.error("油单上传失败异常错误：{}", e.getMessage());
                e.printStackTrace();
                log.error("XML解析错误", e);
                ack.acknowledge();
            } else {
                log.error("智慧航油系统:未找到油单信息,加油单号：{}", Optional.ofNullable(kafkaFuel).map(TFuelRecpt::getFlrcNo).orElse("未知"));
                throw new CustomException("智慧航油系统:未找到油单信息,加油单号" + Optional.ofNullable(kafkaFuel).map(TFuelRecpt::getFlrcNo).orElse("未知"));
            }
        }
    }

    /**
     * 发送XML
     *
     * @param fuelRecpt
     * @return
     */
    private FSSD formatFSSDData(TFuelRecpt fuelRecpt) throws Exception {
        FSSD fssd = new FSSD();
        fssd.setIUD(fuelRecpt.getFlrcStatus() == 3 ? "I" : fuelRecpt.getFlrcStatus() == 4 ? "U" : "");
        Date now = new Date();
        fssd.setLSTU(now);
        fssd.setLUTS(DateFormatUtils.format(now, YYYY_MM_DD_HH_MM_SS_SSS));
//        APC3="KMG";
        /*------2021-3-18新增字段----------*/
        if (StringUtils.isBlank(fuelRecpt.getFlrcAirportCode())) {
            throw new Exception("机场（装运点）代码为空");
        } else {
            fssd.setLOADID(fuelRecpt.getFlrcAirportCode());
        }
        TAirportCode cnafAirportCode = airportCodeRepository.findByApcdCnafAirportCode(fuelRecpt.getFlrcAirportCode());
        if (cnafAirportCode != null) {
            if (StringUtils.isNotBlank(cnafAirportCode.getApcdIataCode())) {
                fssd.setAPC3(cnafAirportCode.getApcdIataCode());
            } else {
                throw new Exception("未找到机场三字码信息");
            }
            if (StringUtils.isBlank(cnafAirportCode.getApcdVkorg())) {
                fssd.setDPID("");
            } else {
                fssd.setDPID(cnafAirportCode.getApcdVkorg());
            }
            if (StringUtils.isBlank(cnafAirportCode.getApcdWerks())) {
                throw new Exception("供油工厂（油库）代码为空");
            } else {
                fssd.setFACTORY(cnafAirportCode.getApcdWerks());
            }
        } else {
            throw new Exception("未找到机场信息");
        }

        fssd.setALC2(fuelRecpt.getFlrcAirlCode());
        fssd.setFSID(fuelRecpt.getFlrcNo());
        //版本号
        /*if(StringUtils.equals(fssd.getIUD(),"U")){
            Integer version=fuelRecpt.getFlrcVersion();
            fssd.setVRSN(Objects.toString(version++));
            fuelRecpt.setFlrcVersion(version);
        }else{
            fuelRecpt.setFlrcVersion(0);
            fssd.setVRSN("0");
        }*/
//      VRSN=Objects.toString(StringUtils.equals(IUD,"I")?);
        fssd.setFSOP(DateFormatUtils.format(fuelRecpt.getFlrcDate(), Constant.YYYY_MM_DD));
        if (fuelRecpt.getFlrcType() == null) {
            throw new Exception("油单类型为空");
        } else {
            fssd.setFSTP(String.valueOf(fuelRecpt.getFlrcType()));
        }

        fssd.setIFBS(StringUtils.isBlank(fuelRecpt.getFlrcBwtar()) ? "FB" : fuelRecpt.getFlrcBwtar());
        if (StringUtils.isNotBlank(fuelRecpt.getTaskId())) {
            TFlight flightByTaskId = tFlightRepository.findFlightByTaskId(fuelRecpt.getTaskId());
            if (flightByTaskId != null) {
                fssd.setFLOP(DateFormatUtils.format(flightByTaskId.getFlgtFlop(), Constant.YYYYMMDD));
            }
        }
        // 查不到航班日期 就取油单日期
        if (StringUtils.isBlank(fssd.getFLOP())) {
            fssd.setFLOP(DateFormatUtils.format(fuelRecpt.getFlrcDate(), Constant.YYYYMMDD));
        }
        fssd.setFLNO(fuelRecpt.getFlrcFlightNo());
        //fssd.setADID(fuelRecpt.getFlgtAdid());
        fssd.setADID("D");
        if (StringUtils.isBlank(fuelRecpt.getFlrcAircrftNo())) {
            throw new Exception("飞机号为空");
        } else {

            fssd.setREGN(fuelRecpt.getFlrcAircrftNo());


        }
        if (StringUtils.isBlank(fuelRecpt.getFlrcAircrftType())) {
            throw new Exception("飞机类型为空");
        } else {
            fssd.setACTN(fuelRecpt.getFlrcAircrftType());
        }
        fssd.setPSN(fuelRecpt.getFlgtPlacecode());
        fssd.setORGNM(fuelRecpt.getFlrcDeparture());

        try {
            Optional<TAirportCode> departure = airportCodeRepository.findByApcdAirportName(fuelRecpt.getFlrcDeparture());
            departure.ifPresent(dep -> fssd.setORG3(dep.getApcdIataCode()));
        } catch (Exception e) {
            throw new Exception("起始机场名查询机场信息异常");
        }

//        ORG3="KMG";
        fssd.setVIANM(fuelRecpt.getFlrcTransit());
        if (StringUtils.isNotEmpty(fuelRecpt.getFlrcTransit())) {
            try {
                Optional<TAirportCode> transit = airportCodeRepository.findByApcdAirportName(fuelRecpt.getFlrcTransit());
                transit.ifPresent(tran -> fssd.setVIA3(tran.getApcdIataCode()));
            } catch (Exception e) {
                throw new Exception("经停机场名查询机场信息异常");
            }

        }
//        VIA3="KMG";
        fssd.setDESNM(fuelRecpt.getFlrcDest());
        try {
            Optional<TAirportCode> destination = airportCodeRepository.findByApcdAirportName(fuelRecpt.getFlrcDest());
            destination.ifPresent(dest -> fssd.setDES3(dest.getApcdIataCode()));
        } catch (Exception e) {
            throw new Exception("目的机场名查询机场信息异常");
        }

//      DES3="KMG";
        fssd.setOTBN(fuelRecpt.getFlrcTestBillNo());
        //fssd.setOTYP(fuelRecpt.getFlrcFuelName());
        fssd.setOTYP("JET A-1");
        fssd.setOTMP(fuelRecpt.getFlrcFuelTemp());
        fssd.setODEN(fuelRecpt.getFlrcFuelDnst());
        fssd.setOMSL(fuelRecpt.getFlrcMeterStat());
        fssd.setOMFL(fuelRecpt.getFlrcMeterFnsh());
        fssd.setOLLT(fuelRecpt.getFlrcFuelVol().intValue());
        fssd.setOLKG(fuelRecpt.getFlrcQuantity().intValue());
        fssd.setOHPN(fuelRecpt.getFlrcHydrtPitNo());
        //fssd.setVNB(StringUtils.substring(fuelRecpt.getFlrcVehiNo(),2));
        fssd.setVNB(fuelRecpt.getFlrcVehiNum());
        fssd.setTFBE(fuelRecpt.getFlrcStatTime());
        fssd.setTFEN(fuelRecpt.getFlrcFnshTime());
        fssd.setCDAT(fuelRecpt.getFlrcRecCreTime() == null ? new Date() : fuelRecpt.getFlrcRecCreTime());
        //fssd.setSIGN_FLG(StringUtils.isNotEmpty(fuelRecpt.getFlrcSign())?"Y":"N");
        //TODO 临时逻辑 南航以外的油单电子签名都是Y
        //if (!fssd.getFLNO().startsWith("CZ")){
        //fssd.setSIGN_FLG("Y");
        //}
        fssd.setOLVR(fuelRecpt.getFlgtOlvr());
        fssd.setTENB(fuelRecpt.getFlrcDeliverId());
        fssd.setTENM(fuelRecpt.getFlrcDeliverName());
        fssd.setTENB2("");
        fssd.setTENB3("");
        fssd.setTENB4("");
        fssd.setTENM2("");
        fssd.setTENM3("");
        fssd.setTENM4("");
        fssd.setTRANSPORT_MILEAGE(null);
        fssd.setREMARK(fuelRecpt.getFlrcRemark());
        if (fuelRecpt.getOrderId() != null) {
            TOrderInfo info = tOrderInfoRepository.findByOrderId(fuelRecpt.getOrderId());
            if (info != null) {
                fssd.setORDER_NO(info.getOrderNo());
                fssd.setTANKER_NO(info.getTankerNo());
            }
        }
        if (StringUtils.isNotBlank(fuelRecpt.getArcrCustomNum())) {
            String fuelRecptStr = "";
            try {
                fssd.setERP_CODE(Integer.parseInt(fuelRecpt.getArcrCustomNum()) + "");
            } catch (Exception e) {
                fssd.setERP_CODE(fuelRecpt.getArcrCustomNum());
            }
            TCustom tCustom = customRepository.findInfo(fuelRecpt.getArcrCustomNum());
            if (tCustom != null) {
                fssd.setCSTNM(tCustom.getCstmName());
            }
        } else {
            throw new Exception("加油单结算单位缺失");
        }

        fssd.setSIGN_FLG("Y");

        if (paperlessCus.contains(fssd.getERP_CODE())) {
            fssd.setSIGN_FLG(StringUtils.isNotEmpty(fuelRecpt.getFlrcSign()) ? "Y" : "N");
        }
        log.info("无纸化客户编码：{}", paperlessCus);
        log.info("油单：{}，无纸化状态：{}", fssd.getFSID(), fssd.getSIGN_FLG());

        /*------2021-3-18新增字段----------*/
        fssd.setINVENTORY("X999");
        fssd.setMATERIAL("1");
        if (StringUtils.isNotBlank(fuelRecpt.getFlrcDefuelReasonCode())) {
            String code = "";
            switch (fuelRecpt.getFlrcDefuelReasonCode()) {
                case "试发":
                    code = "Z01";
                    break;
                case "称重":
                    code = "Z02";
                    break;
                case "清洗油箱":
                    code = "Z03";
                    break;
                case "加多油":
                    code = "Z04";
                    break;
                case "排故":
                    code = "Z05";
                    break;
                case "其他":
                    code = "Z09";
                    break;
                case "抗震救灾":
                    code = "Z10";
                    break;
                default:
                    code = fuelRecpt.getFlrcDefuelReasonCode();
            }
            fssd.setDEFUEL_REASON(code);
            fssd.setDEFUEL_REASON_CODE(code);
        } else {
            fssd.setDEFUEL_REASON("");
            fssd.setDEFUEL_REASON_CODE("");
        }
        fssd.setDEFUEL_TEST(fuelRecpt.getFlrcDefuelTest());
        fssd.setDEFUEL_REFRUEL(fuelRecpt.getFlrcDefuelRefruel());
        fssd.setDEFUEL_STORAGE(fuelRecpt.getFlrcDefuelStorage());
        fssd.setACT3("");

        fssd.setRAPC3(fssd.getDES3());
        // todo settlement_rapcn 代结算机场三码

        if (ObjectUtil.isNotEmpty(fuelRecpt.getSettlementRapcn())) {
            try {
                Optional<TAirportCode> destination = airportCodeRepository.findByApcdAirportName(fuelRecpt.getSettlementRapcn());
                destination.ifPresent(dest -> {
                    fssd.setSAPC3(dest.getApcdIataCode());
                    fuelRecpt.setSettlementRapc3(dest.getApcdIataCode());
                });
            } catch (Exception e) {
                throw new Exception("代结算机场为空");
            }
        } else {
            fssd.setSAPC3(fssd.getAPC3());
            fuelRecpt.setSettlementRapc3(fssd.getAPC3());
            if (cnafAirportCode != null) {
                fuelRecpt.setSettlementRapcn(cnafAirportCode.getApcdAirportName());
            }
        }
        // fssd.setSAPC3(fssd.getAPC3());
        fssd.setDAPC3(fssd.getDES3());
        // todo 不需要
        // fssd.setSLDID(fssd.getAPC3());
        fssd.setRAPCN(fuelRecpt.getFlrcRapcn());
        if (null != fuelRecpt.getFlrcSupplyFuelType()) {
            fssd.setADDOIL_TYPE(fuelRecpt.getFlrcSupplyFuelType().toString());
        }
        if (null == fuelRecpt.getFlrcBusType()) {
            fssd.setBUS_TYPE("2");
        } else {
            fssd.setBUS_TYPE(fuelRecpt.getFlrcBusType().toString());
        }

        // 加抽油日期
        if (ObjectUtil.isNotEmpty(fuelRecpt.getFlrcWkopDate())) {
            fssd.setWKOP(DateFormatUtils.format(fuelRecpt.getFlrcWkopDate(), Constant.YYYY_MM_DD));
        }


        return fssd;
    }

}
