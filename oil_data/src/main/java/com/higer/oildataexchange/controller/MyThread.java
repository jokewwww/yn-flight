package com.higer.oildataexchange.controller;

import com.alibaba.fastjson.JSON;
import com.higer.oildataexchange.common.Constant;
import com.higer.oildataexchange.common.XmlUtils;
import com.higer.oildataexchange.entity.R;
import com.higer.oildataexchange.entity.TCustom;
import com.higer.oildataexchange.entity.flight.TAirportCode;
import com.higer.oildataexchange.entity.flight.TFlight;
import com.higer.oildataexchange.entity.flight.TStaff;
import com.higer.oildataexchange.entity.oil.ExcelOneOIL;
import com.higer.oildataexchange.entity.oil.FSSD;
import com.higer.oildataexchange.entity.oil.FSSD_R;
import com.higer.oildataexchange.entity.orderInfo.TOrderInfo;
import com.higer.oildataexchange.repository.*;
import com.higer.oildataexchange.service.SendHttpService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.higer.oildataexchange.common.Constant.YYYY_MM_DD_HH_MM_SS_SSS;


class MyThread extends Thread {
    private List<ExcelOneOIL> list;
    private SendHttpService httpService;
    private TFlightRepository tFlightRepository;
    private String oilUploadUrl = "http://zuul-sinopo-test.incnaf.com/sinopo-data-app/airportOil/oilUpload";
    private ExcelOil1repository excelOil1repository;
    private TOrderInfoRepository tOrderInfoRepository;
    private TCustomRepository tCustomRepository;
    private TStaffRepository tStaffRepository;
    private TAirportCodeRepository tAirportCodeRepository;


    public MyThread(List<ExcelOneOIL> list) {
        this.list = list;
        this.httpService = SpringContextUtil.getBean(SendHttpService.class);
        this.tFlightRepository = SpringContextUtil.getBean(TFlightRepository.class);
        this.excelOil1repository = SpringContextUtil.getBean(ExcelOil1repository.class);
        this.tOrderInfoRepository = SpringContextUtil.getBean(TOrderInfoRepository.class);
        this.tCustomRepository = SpringContextUtil.getBean(TCustomRepository.class);
        this.tStaffRepository = SpringContextUtil.getBean(TStaffRepository.class);
        this.tAirportCodeRepository = SpringContextUtil.getBean(TAirportCodeRepository.class);
    }

    @Autowired

    @Override
    public void run() {
        for (ExcelOneOIL one : list) {
            try {
                if (one == null) {
                    continue;
                }
                FSSD fssd = getData(one);
                R<FSSD> fssdr = R.newInstanceR("CNAF", "CZ", fssd);
                System.out.println("油单上传参数：" + JSON.toJSONString(fssdr));
                String xml = XmlUtils.convertToXml(fssdr, Constant.CHARSET, true);

                String s = httpService.sendHttpPost(oilUploadUrl, xml);
                System.out.println("油单上传，返回信息:{}" + s);
                Document document = DocumentHelper.parseText(s);
                Element rootElement = document.getRootElement();
                Element bd = rootElement.element("BD");
                FSSD_R fssdR = XmlUtils.convertToObj(bd.asXML(), FSSD_R.class);
                //0未上传  1 上传失败 2 上传成功 3 新增 4 修改
                if (fssdR != null) {
                    if (StringUtils.equals(fssdR.getPTST(), "S")) {//成功
                        excelOil1repository.updStatusDate("2", "", "", one.getYdhm(), one.getDate());
                    } else {
                        excelOil1repository.updStatusDate("1", fssdR.getFRSN(), s, one.getYdhm(), one.getDate());
                    }
                } else {
                    excelOil1repository.updStatusDate("1", "上传失败，请联系管理员", "", one.getYdhm(), one.getDate());
                }
            } catch (Exception e) {
                e.printStackTrace();
                excelOil1repository.updStatusDate("1", "上传异常", e.getMessage(), one.getYdhm(), one.getDate());
                continue;
            }

        }
    }

    public FSSD getData(ExcelOneOIL one) throws Exception {
        FSSD fssd = new FSSD();
        fssd.setIUD("I");
        Date now = new Date();
        fssd.setLSTU(now);
        if (StringUtils.isBlank(one.getJcszm())) {
            Optional<TAirportCode> byApcdAirportName = tAirportCodeRepository.findByApcdAirportName(one.getJcmc());
            byApcdAirportName.ifPresent(tAirportCode -> {
                fssd.setAPC3(tAirportCode.getApcdIataCode().trim());
                if (StringUtils.isBlank(tAirportCode.getApcdVkorg())) {
                    fssd.setDPID("");
                } else {
                    fssd.setDPID(tAirportCode.getApcdVkorg());
                }
                fssd.setFACTORY(tAirportCode.getApcdWerks());
            });
            if (StringUtils.isEmpty(fssd.getAPC3())) {
                throw new Exception("未找到APC3");
            }
            if (StringUtils.isBlank(fssd.getFACTORY())) {
                throw new Exception("供油工厂（油库）代码为空");
            }
        } else {
            fssd.setAPC3(one.getJcszm());
            TAirportCode byApcdAirportName = tAirportCodeRepository.findByApcdIataCode(one.getJcszm());
            if (byApcdAirportName != null) {
                if (StringUtils.isBlank(byApcdAirportName.getApcdVkorg())) {
                    fssd.setDPID("");
                } else {
                    fssd.setDPID(byApcdAirportName.getApcdVkorg());
                }
                if (StringUtils.isBlank(byApcdAirportName.getApcdWerks())) {
                    throw new Exception("供油工厂（油库）代码为空");
                } else {
                    fssd.setFACTORY(byApcdAirportName.getApcdWerks());
                }
            } else {
                throw new Exception("未找到APC3机场");
            }
        }
        fssd.setLUTS(DateFormatUtils.format(now, YYYY_MM_DD_HH_MM_SS_SSS));
        //fssd.setERP_CODE(one.getCustom());
        fssd.setCSTNM(one.getHkgs());
        fssd.setLOADID(one.getYdhm().substring(0, 4));
        // TODO 顺丰航空有限公司特殊处理
        if ("顺丰航空有限公司".equals(one.getHkgs())) {
            fssd.setERP_CODE("120134");
        } else if ("南山公务机有限公司".equals(one.getHkgs())) {
            fssd.setERP_CODE("0000120146");
        } else {
            if (StringUtils.isNotBlank(one.getHkgsdm())) {
                fssd.setERP_CODE(Integer.parseInt(one.getHkgsdm().trim()) + "");
            } else {
                TCustom tCustom = tCustomRepository.findByCstmName(one.getHkgs().trim());
                if (tCustom != null) {
                    //fssd.setCSTNM(tCustom.getCstmName());
                    fssd.setERP_CODE(Integer.parseInt(tCustom.getCstmNum()) + "");
                } else {
                    throw new Exception("未找到用户编号");
                }
            }
        }

        /*List<TCustom> tCustom = tCustomRepository.findInfoByName(one.getFjhm());
        if (tCustom!=null && tCustom.size()>0){
            fssd.setERP_CODE(Integer.parseInt(tCustom.get(0).getCstmNum())+"");
            fssd.setCSTNM(tCustom.get(0).getCstmName());
        }else {
            throw new Exception("未找到用户编号");
        }*/
        fssd.setALC2(one.getHbh().trim().substring(0, 2));
        fssd.setFSID(one.getYdhm().trim());
        fssd.setVRSN("0");
        if (one.getDate().contains(".")) {
            fssd.setFSOP(one.getDate().trim().substring(0, 4) + "-" + one.getDate().trim().substring(5, 7) + "-" + one.getDate().trim().substring(8));
        } else if (one.getDate().trim().contains("-")) {
            fssd.setFSOP(one.getDate().trim());
        } else if (one.getDate().trim().contains("/")) {
            //2021-4-1
            fssd.setFSOP(DateFormatUtils.format(DateUtils.parseDate(one.getDate().trim(), "yyyy/MM/dd"), "yyyy-MM-dd"));
        } else {
            fssd.setFSOP(one.getDate().trim().substring(0, 4) + "-" + one.getDate().trim().substring(4, 6) + "-" + one.getDate().trim().substring(6));
        }
        fssd.setFLNO(one.getHbh());
        fssd.setADID("D");
        fssd.setREGN(one.getFjhm());
        // TODO 导入注意 机号为空的，默认设置机号为“B9330”（也可以是其他有效机号）
        if (ObjectUtils.isEmpty(fssd.getREGN())) {
            fssd.setREGN("B9330");
        }
        // TODO 导入注意 航班号为空的，默认与机号相同；航班号不足3位的，在航班号后面补“00”；
        if (ObjectUtils.isEmpty(fssd.getFLNO())) {
            fssd.setREGN(fssd.getREGN());
        }
        if (fssd.getFLNO().length() < 3) {
            fssd.setREGN(fssd.getFLNO() + "00");
        }

        fssd.setACTN(one.getFjlx());
        // TODO 导入注意 机型为空的，默认设置机型为“B738”
        if (ObjectUtils.isEmpty(fssd.getACTN())) {
            fssd.setACTN("B738");
        }
        fssd.setOTMP(Double.parseDouble(StringUtils.isBlank(one.getYpwd()) ? "0" : one.getYpwd().trim()));
        fssd.setODEN(Double.parseDouble(StringUtils.isBlank(one.getYpmd()) ? "0" : one.getYpmd().trim()));
        fssd.setOLLT(one.getJylsheng() == null ? 0 : (int) Double.parseDouble(one.getJylsheng().trim()));
        fssd.setOLKG(one.getJylgongjin() == null ? 0 : (int) Double.parseDouble(one.getJylgongjin().trim()));
        if (one.getBeginDate().contains(".")) {
            fssd.setTFBE(DateUtils.parseDate(one.getBeginDate().trim(), "yyyy.MM.dd HH:mm"));
            fssd.setTFEN(DateUtils.parseDate(one.getEndDate().trim(), "yyyy.MM.dd HH:mm"));
            fssd.setCDAT(DateUtils.parseDate(one.getBeginDate().trim(), "yyyy.MM.dd HH:mm"));
        } else if (one.getBeginDate().contains("-")) {
            fssd.setTFBE(DateUtils.parseDate(one.getBeginDate().trim(), "yyyy-MM-dd HH:mm:ss"));
            fssd.setTFEN(DateUtils.parseDate(one.getEndDate().trim(), "yyyy-MM-dd HH:mm:ss"));
            fssd.setCDAT(DateUtils.parseDate(one.getBeginDate().trim(), "yyyy-MM-dd HH:mm:ss"));
        }

        fssd.setOTYP("JET A-1");
        fssd.setTENB2("");
        fssd.setTENB3("");
        fssd.setTENB4("");
        fssd.setTENM2("");
        fssd.setTENM3("");
        fssd.setTENM4("");
        fssd.setTRANSPORT_MILEAGE(null);
        fssd.setREMARK("");
        if (StringUtils.isNotBlank(one.getXsdd())) {
            TOrderInfo info = tOrderInfoRepository.findByOrderId(Long.parseLong(one.getXsdd()));
            if (info != null) {
                fssd.setORDER_NO(info.getOrderNo());
                fssd.setTANKER_NO(info.getTankerNo());
            }
        }
        fssd.setTENM(one.getJyy());
        if (StringUtils.isNotBlank(one.getJyy())) {
            List<TStaff> byStaffName = tStaffRepository.findByStaffName(one.getJyy());
            if (byStaffName != null && byStaffName.size() > 0) {
                fssd.setTENB(byStaffName.get(0).getStaffId());
            }
        }

        // TODO 导入注意 员工号为空的，默认设置为“24740”（也可以是其他有效的工号）；
        if (ObjectUtils.isEmpty(fssd.getTENB())) {
            fssd.setTENB("24740");
        }

        fssd.setSIGN_FLG("Y");
        /*-------------------*/

        if (StringUtils.isNotBlank(one.getSfszm())) {
            fssd.setORG3(one.getSfszm());
            TAirportCode byApcdIataCode = tAirportCodeRepository.findByApcdIataCode(one.getSfszm());
            if (byApcdIataCode != null) {
                fssd.setORGNM(byApcdIataCode.getApcdAirportName());
            }
            fssd.setDES3(one.getMdszm());
            TAirportCode byApcdIataCode2 = tAirportCodeRepository.findByApcdIataCode(one.getMdszm());
            if (byApcdIataCode2 != null) {
                fssd.setDESNM(byApcdIataCode2.getApcdAirportName());
            }
            fssd.setVIA3(one.getJtszm());
            TAirportCode byApcdIataCode3 = tAirportCodeRepository.findByApcdIataCode(one.getJtszm());
            if (byApcdIataCode3 != null) {
                fssd.setVIANM(byApcdIataCode3.getApcdAirportName());
            }
            fssd.setPSN("");
        } else {
            List<TFlight> tFlightList = tFlightRepository.findFlgtInfo(one.getHbh(), fssd.getFSOP(), "D");
            if (tFlightList != null && tFlightList.size() > 0) {
                TFlight tFlight = tFlightList.get(0);
                fssd.setPSN(tFlight.getFlgtPlacecode());
                fssd.setORGNM(tFlight.getFlgtOrgnm());
                fssd.setORG3(tFlight.getFlgtOrg3c());
                fssd.setDES3(tFlight.getFlgtDes3c());
                fssd.setDESNM(tFlight.getFlgtDesnm());
                fssd.setVIA3(tFlight.getFlgtTrs3c1());
                fssd.setVIANM(tFlight.getFlgtTrsnm1());
            }
        }
        //  TODO 始发目的站为空的，填成与油单所在机场相同的机场
        if (ObjectUtils.isEmpty(fssd.getORG3())) {
            fssd.setORG3(fssd.getAPC3());
            fssd.setORGNM(one.getJcmc());
        }

        if (ObjectUtils.isEmpty(fssd.getDES3())) {
            fssd.setDES3(fssd.getAPC3());
            fssd.setDESNM(one.getJcmc());
        }


        // 油单类型
        fssd.setFSTP(one.getYdhm().substring(4, 5));
        //TODO 抽油信息
        if ("4".equals(fssd.getFSTP()) || "5".equals(fssd.getFSTP()) || "6".equals(fssd.getFSTP())) {
            fssd.setDEFUEL_REASON("Z05");
            fssd.setDEFUEL_TEST("Y");
            fssd.setDEFUEL_REFRUEL("Y");
            fssd.setDEFUEL_STORAGE("0");
        }
        //fssd.setFSTP("3");
        if (StringUtils.isNotBlank(one.getBslx())) {
            fssd.setIFBS(one.getBslx());
        } else {
            fssd.setIFBS("FB");
        }
        if ("N".equals(fssd.getIFBS())) {
            fssd.setIFBS("FB");
        }

        fssd.setFLOP(fssd.getFSOP().substring(0, 4) + fssd.getFSOP().substring(5, 7) + fssd.getFSOP().substring(8));
        fssd.setOHPN(StringUtils.isNotBlank(one.getDjbh()) ? one.getDjbh() : "");
        //  TODO 导入注意 地井号为空的，默认设置为“000”；
        if (ObjectUtils.isEmpty(fssd.getOHPN())) {
            fssd.setOHPN("000");
        }

        fssd.setVNB(StringUtils.isNotBlank(one.getYcbh()) ? one.getYcbh() : "8786");
        fssd.setINVENTORY("X999");
        fssd.setMATERIAL("1");
        return fssd;
    }
}