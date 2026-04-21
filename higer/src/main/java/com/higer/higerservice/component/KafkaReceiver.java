package com.higer.higerservice.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/10/18 14:11
 * @Description:
 */
@Component
@Slf4j
public class KafkaReceiver {

    /*@KafkaListener(topics = {"carsignal"})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            log.info("----------------- record =" + record);
            log.info("------------------ message =" + message);
        }

    }
    @KafkaListener(topics = {"cargps"})
    public void listen1(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            log.info("----------------- record =" + record);
            log.info("------------------ message =" + message);
        }

    }*/

   /* @KafkaListener(topics = "testaa")
    public void listen(@Payload String message){
        System.err.println("mes:-----" + message);
    }*/
}
