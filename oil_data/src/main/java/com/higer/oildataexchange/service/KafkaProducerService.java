package com.higer.oildataexchange.service;

import com.higer.oildataexchange.common.KafkaSendResultHandler;
import com.higer.oildataexchange.repository.TFlightErrorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;


/**
 * @Auther: 修宏鑫
 * @Date: 2018/10/18 14:09
 * @Description:
 */
@Service
@Slf4j
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private TFlightErrorRepository tFlightErrorRepository;

    //发送消息方法
    public void send(String topic, String msg, Integer lock) {
        try {
            log.debug("发送kafka，topic：{},msg：{}", topic, msg);
            ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(topic, msg);
            kafkaTemplate.metrics();
            // 消息发送的监听器，用于回调返回信息
            kafkaTemplate.setProducerListener(new KafkaSendResultHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
