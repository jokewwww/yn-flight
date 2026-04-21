package com.higer.oildataexchange.service;

import com.alibaba.fastjson.JSONObject;
import com.higer.oildataexchange.common.Constant;
import com.higer.oildataexchange.common.XmlUtils;
import com.higer.oildataexchange.entity.R;
import com.higer.oildataexchange.entity.orderInfo.TOrderInfo;
import com.higer.oildataexchange.entity.orderInfoBack.OSRP;
import com.higer.oildataexchange.entity.orderInfoBack.OSRP_R;
import com.higer.oildataexchange.repository.TAirportCodeRepository;
import com.higer.oildataexchange.repository.TOrderInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

import static com.higer.oildataexchange.common.Constant.YYYY_MM_DD_HH_MM_SS_SSS;

@Service
@Slf4j
public class OrderBackConsumer {

    @Autowired
    private SendHttpService sendHttpService;

    @Autowired
    private TAirportCodeRepository airportCodeRepository;

    @Value(value = "${orderInfoBackUrl}")
    private String orderInfoBackUrl;

    @Autowired
    private TOrderInfoRepository orderInfoRepository;

    @KafkaListener(topics = {"${order.back.topic}"})
    public void listener(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            log.info("订单回调数据;" + record.value());
            List<TOrderInfo> orderInfoList = JSONObject.parseArray(record.value(), TOrderInfo.class);
            orderInfoList.forEach(orderInfo -> {
                Date now = new Date();
                OSRP osrp = new OSRP();
                osrp.setIUD("U");
                osrp.setLUTS(DateFormatUtils.format(now, YYYY_MM_DD_HH_MM_SS_SSS));
                Assert.notNull(orderInfo, "订单信息为空");
                Assert.notNull(orderInfo.getApc3(), "所在的机场不能为空！！");
                Assert.notNull(orderInfo.getCstno(), "客户代码不能为空！！");
                Assert.notNull(orderInfo.getOrderNo(), "订单号不能为空！！");
                Assert.notNull(orderInfo.getOrderStatus(), "状态不能为空！！");
                osrp.setAPC3(orderInfo.getApc3());
                osrp.setORDER_NO(orderInfo.getOrderNo());
                osrp.setCSTNO(orderInfo.getCstno());
                osrp.setCSTNM(orderInfo.getCstnm());
                osrp.setORDER_STATUS(orderInfo.getOrderStatus().toString());
                osrp.setSDAT(DateUtils.setMilliseconds(now, 0));
                R<OSRP> osrpr = R.newInstanceR("CZ", "CNAF", osrp);
                String xml = XmlUtils.convertToXml(osrpr, Constant.CHARSET, true);
                log.info("OrderBackConsumer发送" + xml);
                String s = sendHttpService.sendHttpPost(orderInfoBackUrl, xml);
                log.info("OrderBackConsumer收到" + s);
                Document document = null;
                try {
                    document = DocumentHelper.parseText(s);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                Element rootElement = document.getRootElement();
                Element bd = rootElement.element("BD");
                OSRP_R qsrpR = XmlUtils.convertToObj(bd.asXML(), OSRP_R.class);
                if (qsrpR != null) {
                    Integer orderStatus = null;
                    //订单状态：0-尚未执行；1-等待执行；2-已经执行；3-未能执行
                    if ("S".equals(qsrpR.getPTST())) {
                        orderStatus = 1;
                    } else if ("F".equals(qsrpR.getPTST())) {
                        orderStatus = 3;
                    }
                    if (orderStatus != null) {
                        TOrderInfo byOrderNo = orderInfoRepository.findByOrderNo(qsrpR.getORDER_NO());
                        byOrderNo.setOrderStatus(orderStatus);
                        orderInfoRepository.save(byOrderNo);
                    }

                }
            });

            ack.acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("油单反馈异常：" + e.getMessage());
            ack.acknowledge();
        }
    }

}
