package com.higer.oildataexchange.service;

import com.higer.oildataexchange.common.Constant;
import com.higer.oildataexchange.common.ModelAssistant;
import com.higer.oildataexchange.common.XmlUtils;
import com.higer.oildataexchange.entity.R;
import com.higer.oildataexchange.entity.orderInfo.OIRQ;
import com.higer.oildataexchange.entity.orderInfo.OIRQ_R;
import com.higer.oildataexchange.entity.orderInfo.TOrderInfo;
import com.higer.oildataexchange.repository.TOrderInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
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
import java.util.stream.Collectors;

import static com.higer.oildataexchange.common.Constant.YYYY_MM_DD_HH_MM_SS;
import static com.higer.oildataexchange.common.Constant.YYYY_MM_DD_HH_MM_SS_SSS;

@Service
@Slf4j
public class OrderInfoService implements Job {


    @Autowired
    protected KafkaProducerService kafkaService;
    @Autowired
    private RedisTemplate<String, Date> redisTemplate;
    @Autowired
    private SendHttpService sendHttpService;
    @Autowired
    private TOrderInfoRepository orderInfoRepository;
    @Value(value = "${orderInfoUrl}")
    private String orderInfoUrl;
    @Value(value = "${fliter_airport_code}")
    private String filterAirportCode;

    public void orderInfoSchedule() {
        try {
            Date currentDate = new Date();
            Date lastDate = redisTemplate.opsForValue().getAndSet("oilOrderInfo", currentDate);
            if (lastDate == null) {
                log.debug(String.format("第一次执行订单查询，执行时间为%s", DateFormatUtils.format(new Date(), YYYY_MM_DD_HH_MM_SS)));
            } else {
                log.debug(String.format("上一次执行订单查询时间%s，本次执行时间%s", DateFormatUtils.format(lastDate, YYYY_MM_DD_HH_MM_SS), DateFormatUtils.format(currentDate, YYYY_MM_DD_HH_MM_SS)));
            }
            OIRQ oirq = new OIRQ();
            Date startDate = new Date();
            startDate = DateUtils.setMilliseconds(startDate, 0);
            startDate = DateUtils.setSeconds(startDate, 0);
            startDate = DateUtils.setMinutes(startDate, 0);
            startDate = DateUtils.setHours(startDate, 0);
            startDate = DateUtils.addDays(startDate, -1);
            oirq.setDTFR(startDate);
            oirq.setADDOIL_TYPE("");
            oirq.setOIL_TYPE("");
            oirq.setOTST("");
            oirq.setAPC3(filterAirportCode);
            oirq.setLUTS(lastDate == null ? "" : DateFormatUtils.format(lastDate, YYYY_MM_DD_HH_MM_SS_SSS));
            //oirq.setLUTS("2021-01-14 14:15:40");
            oirq.setLMTN(100);
            R<OIRQ> oirqr = R.newInstanceR("CNAF", "CNAF", oirq);
            String xml = XmlUtils.convertToXml(oirqr, Constant.CHARSET, true);
            log.info("orderInfoSchedule发送：" + xml);
            String s = sendHttpService.sendHttpPost(orderInfoUrl, xml);
            log.info("orderInfoSchedule收到：" + s);
            Document document = DocumentHelper.parseText(s);
            Element rootElement = document.getRootElement();
            Element bd = rootElement.element("BD");
            List<Element> sub = bd.elements("SUB");
            List<TOrderInfo> resultList = sub.stream().map(Element::asXML)
                    .map(item -> XmlUtils.convertToObj(item, OIRQ_R.class))
                    .map(OIRQ_R::tOrderInfo).collect(Collectors.toList());
            resultList.forEach(one -> {
                if (one.getCdat() == null) {
                    one.setCdat(new Date());
                }
                if (one.getEstimatedTime() == null) {
                    one.setEstimatedTime(new Date());
                }
                if (one.getEstimatedTime() == null) {
                    one.setEstimatedTime(new Date());
                }
                if (StringUtils.isBlank(one.getOrg3())) {
                    one.setOrg3(one.getApc3());
                }
                TOrderInfo byOrderNo = orderInfoRepository.findByOrderNo(one.getOrderNo());
                if (byOrderNo != null) {
                    ModelAssistant.copyProperties(one, byOrderNo);
                    orderInfoRepository.save(byOrderNo);
                } else {
                    orderInfoRepository.save(one);
                }
            });
        } catch (Exception e) {
            log.error("订单查询异常：" + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        this.orderInfoSchedule();
    }
}
