package com.higer.oildataexchange.service;

import cn.hutool.core.bean.BeanUtil;
import com.higer.oildataexchange.common.Constant;
import com.higer.oildataexchange.common.ModelAssistant;
import com.higer.oildataexchange.common.XmlUtils;
import com.higer.oildataexchange.entity.R;
import com.higer.oildataexchange.entity.credit.CIRQ;
import com.higer.oildataexchange.entity.credit.CIRQ_R;
import com.higer.oildataexchange.entity.credit.TCreditInfo;
import com.higer.oildataexchange.entity.credit.TCreditInfoHistory;
import com.higer.oildataexchange.repository.TCreditInfoHistoryRepository;
import com.higer.oildataexchange.repository.TCreditInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.higer.oildataexchange.common.Constant.YYYY_MM_DD_HH_MM_SS;
import static com.higer.oildataexchange.common.Constant.YYYY_MM_DD_HH_MM_SS_SSS;

@Service
@Slf4j
public class CreditInfoService implements Job {


    @Autowired
    protected KafkaProducerService kafkaService;
    @Autowired
    private RedisTemplate<String, Date> redisTemplate;
    @Autowired
    private SendHttpService sendHttpService;
    @Value(value = "${creditInfoUrl}")
    private String creditInfoUrl;
    @Value(value = "${fliter_airport_code}")
    private String filterAirportCode;
    @Autowired
    private TCreditInfoRepository creditInfoRepository;
    @Autowired
    private TCreditInfoHistoryRepository tCreditInfoHistoryRepository;

    public void creditInfoSchedule() {
        try {
            Date currentDate = new Date();
            Date lastDate = redisTemplate.opsForValue().getAndSet("creditInfo", currentDate);
            if (lastDate == null) {
                log.info(String.format("第一次执行信用查询，执行时间为%s", DateFormatUtils.format(new Date(), YYYY_MM_DD_HH_MM_SS)));
            } else {
                log.info(String.format("上一次执行订信用询时间%s，本次执行时间%s", DateFormatUtils.format(lastDate, YYYY_MM_DD_HH_MM_SS), DateFormatUtils.format(currentDate, YYYY_MM_DD_HH_MM_SS)));
            }
            CIRQ cirq = new CIRQ();
            //cirq.setAPC3("PKX");
            cirq.setCSTNO("");
            cirq.setLUTS(lastDate == null ? "" : DateFormatUtils.format(lastDate, YYYY_MM_DD_HH_MM_SS_SSS));
            //cirq.setLUTS("2020-04-26 00:00:01.001");
            cirq.setLMTN(100);
            String[] apc3Arr = filterAirportCode.split(",");
            if (apc3Arr != null && apc3Arr.length > 0) {
                for (String apc3 : apc3Arr) {
                    cirq.setAPC3(apc3);
                    R<CIRQ> oirqr = R.newInstanceR("CNAF", "CNAF_TSN", cirq);
                    String xml = XmlUtils.convertToXml(oirqr, Constant.CHARSET, true);
                    log.info("creditInfoSchedule发送：" + xml);
                    String s = sendHttpService.sendHttpPost(creditInfoUrl, xml);
                    //String respone = "[{\"cILVL\":\"\",\"cIRMK\":\"信用正常\",\"cITST\":\"0\",\"cSTNM\":\"北京首都航空有限公司\",\"cSTNO\":\"120021\",\"cUSTOMER_TYPE\":\"2\"},{\"cILVL\":\"\",\"cIRMK\":\"信用正常\",\"cITST\":\"0\",\"cSTNM\":\"河北航空有限公司\",\"cSTNO\":\"110320\",\"cUSTOMER_TYPE\":\"\"}]";

                    log.info("creditInfoSchedule收到：" + s);
                    Document document = DocumentHelper.parseText(s);
                    Element rootElement = document.getRootElement();
                    Element bd = rootElement.element("BD");
                    List<Element> sub = bd.elements("SUB");
                    sub.stream().map(Element::asXML)
                            .map(item -> XmlUtils.convertToObj(item, CIRQ_R.class))
                            .map(CIRQ_R::toCreditInfo)
                            .forEach(one -> {

                                TCreditInfoHistory history = new TCreditInfoHistory();
                                BeanUtil.copyProperties(one, history);
                                tCreditInfoHistoryRepository.save(history);

                                TCreditInfo byCstno = creditInfoRepository.findByCstno(one.getCstno());
                                if (byCstno != null) {
                                    ModelAssistant.copyProperties(one, byCstno);
                                    creditInfoRepository.save(byCstno);
                                } else {
                                    creditInfoRepository.save(one);
                                }
                            });
                }
            }
        } catch (Exception e) {
            log.error("信用查询异常：" + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        this.creditInfoSchedule();
    }
}
