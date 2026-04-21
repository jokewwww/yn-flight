package com.higer.read_kafka.configuration;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/4/3 19:58
 * @Description:
 */
@Component
public class TestCom {

    /*@KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = "test")
    public void consumerListener(ConsumerRecord<?, ?> record, Acknowledgment ack) {
        log.info("Before receiving:" + record.toString());
        String value = (String) record.value();
        try {
            ack.acknowledge();//提交offset
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
