package com.higer.oildataexchange.service;

import com.alibaba.fastjson.JSON;
import com.higer.oildataexchange.common.Constant;
import com.higer.oildataexchange.common.XmlUtils;
import com.higer.oildataexchange.entity.R;
import com.higer.oildataexchange.entity.flight.FIRQ;
import com.higer.oildataexchange.entity.flight.FIRQ_R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
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
import java.util.Objects;
import java.util.Optional;

import static com.higer.oildataexchange.common.Constant.YYYY_MM_DD_HH_MM_SS;
import static com.higer.oildataexchange.common.Constant.YYYY_MM_DD_HH_MM_SS_SSS;

@Service
@Slf4j
public class FlightInfoService implements Job {


    public static Date currentDate = null;
    @Autowired
    private RedisTemplate<String, Date> redisTemplate;
    @Autowired
    private FlightInfoService flightInfoService;
    @Autowired
    private KafkaProducerService kafkaProducerService;
    @Autowired
    private SendHttpService sendHttpService;
    @Value(value = "${fliter_airport_code}")
    private String filterAiportCode = "";
    @Value(value = "${flightInfoSearch}")
    private String flightInfoUrl;

    public void FlightInfoGetSchedule() {
        currentDate = new Date();

        Date lastDate = Optional.ofNullable(redisTemplate.opsForValue().get("current")).orElse(new Date());
        log.debug(String.format("上一次执行时间%s，本次执行时间%s", DateFormatUtils.format(lastDate, YYYY_MM_DD_HH_MM_SS), DateFormatUtils.format(currentDate, YYYY_MM_DD_HH_MM_SS)));

        FIRQ firq = new FIRQ();
        firq.setADID("D");
        firq.setAPC3(filterAiportCode);
        Date startDate = new Date();
        startDate = DateUtils.setMilliseconds(startDate, 0);
        startDate = DateUtils.setSeconds(startDate, 0);
        startDate = DateUtils.setMinutes(startDate, 0);
        startDate = DateUtils.setHours(startDate, 0);
        startDate = DateUtils.addDays(startDate, -1);
        firq.setDTFR(startDate);
        firq.setLUTS(lastDate == null ? "" : DateFormatUtils.format(lastDate, YYYY_MM_DD_HH_MM_SS_SSS));
        firq.setLMTN(100);
        R<FIRQ> r = R.newInstanceR("CZ", "CNAF", firq);
        String xml = XmlUtils.convertToXml(r, Constant.CHARSET, true);
        log.info("FlightInfoGetSchedule发送" + xml + "结束");
        String response = sendHttpService.sendHttpPost(flightInfoUrl, xml);
        log.info("FlightInfoGetSchedule收到" + response);
        flightInfoService.parseResponse(response);
    }

    public void parseResponse(String xml) {
        try {
            Document document = DocumentHelper.parseText(xml);
            Element rootElement = document.getRootElement();
            Element bd = rootElement.element("BD");
            List<Element> sub = bd.elements("SUB");
            sub.stream().map(Element::asXML)
                    .map(item -> XmlUtils.convertToObj(item, FIRQ_R.class))
                    .filter(Objects::nonNull)
                    .filter(item ->
                            (item.getOLTT() != null && item.getOLTT() != 0)
                                    ||
                                    (item.getOLTL() != null && item.getOLTL() != 0)
                                    ||
                                    (item.getOEST() != null && item.getOEST() != 0))
                    .map(FIRQ_R::toFlight)
                    .map(JSON::toJSONString)
                    .forEach(this::sendKafka);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void sendKafka(String msg) {
        log.info("FlightInfoGetSchedule存入时间" + currentDate);
        redisTemplate.opsForValue().set("current", currentDate);
        kafkaProducerService.send("flight_exchange", msg, null);
        log.info("oilPayConfirmSchedule,send kafka：" + msg);

    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        this.FlightInfoGetSchedule();
    }
}
