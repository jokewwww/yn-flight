package com.higer.oildataexchange.service;

import com.alibaba.fastjson.JSONObject;
import com.higer.oildataexchange.common.Constant;
import com.higer.oildataexchange.common.StringUtils;
import com.higer.oildataexchange.common.XmlUtils;
import com.higer.oildataexchange.entity.acdm.AcdmFlow;
import com.higer.oildataexchange.entity.acdm.AcdmXml;
import com.higer.oildataexchange.entity.acdm.FligtVo;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Service
@Slf4j
public class AirportPushConsumer {

    @Autowired
    private SendHttpService sendHttpService;

    @Value(value = "${send.acdm.url}")
    private String sendAcdmUrl;

    //@KafkaListener(topics =  {"${task_change_log}"})
    public void listener(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            log.info("kafka收到数据acdm数据;" + record.value());
            FligtVo fligtVo = JSONObject.parseObject(record.value(), FligtVo.class);

            HashMap<String, String> params = new HashMap<>();
            // 主类型
            params.put("mainType", "FLOP");
            // 子类型
            params.put("subType", "CLDT");
            // 发送者
            params.put("sender", "GSM");
           /* // 报文内容
            params.put("message",
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?><MSG><META><SNDR>AODB</SNDR><SEQS>1000</SEQS><DTTM>20021010090311</DTTM>" +
                            "<TYPE>FLOP</TYPE><STYP>CLDT</STYP></META><FLOP><FLID>121112312</FLID><FFID>CA-CA101-A-12DEC031345-D</FFID>" +
                            "<AOCID>CA101-201808271420-PEK-SHA</AOCID><CLDT CLNO=\"”1”\"><BELT>B01</BELT><BCLS>X</BCLS><PCOT>12DEC031330</PCOT>" +
                            "<PCCT>12DEC031430</PCCT><BTYP>D</BTYP></CLDT></FLOP></MSG>");*/
            String styp = "";
            if (fligtVo.getTaskStatus() == 3) {
                styp = "FUEL-BGN";
            } else if (fligtVo.getTaskStatus() == 7) {
                styp = "FUEL-END";
            } else {
                log.info("未推送acdm，当前状态：" + fligtVo.getTaskStatus());
                return;
            }

            AcdmFlow acdmFlow = formatInfoData(fligtVo);

            String arvs = "D".equals(fligtVo.getFlgtAdid()) ? fligtVo.getFlgtDes3c() : fligtVo.getFlgtOrg3c();
            AcdmXml<AcdmFlow> acdmFlowAcdmXml = AcdmXml.newInstanceAcdm(fligtVo.getFlgtFfid(), styp, fligtVo.getFlgtAl2c(),
                    fligtVo.getFlgtAdid(), "SHE", arvs, "1", acdmFlow);
            String xml = XmlUtils.convertToXml(acdmFlowAcdmXml, Constant.CHARSET, true);
            params.put("message", xml);
            //sendHttpService.sendAcdmHttpPost(sendAcdmUrl, JSON.toJSONString(params));

            ack.acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("油单反馈异常：" + e.getMessage());
            ack.acknowledge();
        }
    }

    /**
     * 生产xml信息
     */
    public AcdmFlow formatInfoData(FligtVo fligtVo) {
        AcdmFlow acdmFlow = new AcdmFlow();
        acdmFlow.setFFID(fligtVo.getFlgtFlno() + "-" + DateFormatUtils.format(fligtVo.getFlgtFlop(), Constant.YYYY_MM_DD));
        acdmFlow.setBATCH("1");
        String status = "";
        switch (fligtVo.getTaskStatus()) {
            case 3:
                status = "begin";
                break;
            case 7:
                status = "end";
                break;

        }
        acdmFlow.setSTATUS(status);
        acdmFlow.setEXPREASON("");
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        //拼音小写
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        //不带声调
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //要转换的中文，格式，转换之后的拼音的分隔符，遇到不能转换的是否保留   wo,shi,zhong,guo,ren,，hello
        if (StringUtils.isNotBlank(fligtVo.getTaskOpeStaffName())) {
            try {
                acdmFlow.setOPERATOR(PinyinHelper.toHanYuPinyinString(fligtVo.getTaskOpeStaffName(), format, " ", true));
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                acdmFlow.setOPERATOR(fligtVo.getTaskOpeStaffName());
            }
        }
        acdmFlow.setDEPT("车辆部");
        acdmFlow.setSCHDBEGINTM("");
        acdmFlow.setSCHDENDTM("");
        acdmFlow.setBEGINTM(DateFormatUtils.format(fligtVo.getTaskRecCreTime(), Constant.YYYYMMDDHHMMSS));
        acdmFlow.setENDTM(DateFormatUtils.format(new Date(), Constant.YYYYMMDDHHMMSS));
        acdmFlow.setSUPERVISOR("");
        acdmFlow.setSVBEGINTM("");
        acdmFlow.setSVENDTM("");
        acdmFlow.setINPLACETM("");
        return acdmFlow;
    }
}
