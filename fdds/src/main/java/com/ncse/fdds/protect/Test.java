package com.ncse.fdds.protect;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: 修宏鑫
 * @Date: 2020/4/10 09:05
 * @Description:
 */
@Component
@Slf4j
public class Test {

    private final static ExecutorService executor = Executors.newFixedThreadPool(3000);

    @KafkaListener(topics = {"flight1"})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();

            //System.err.println(message.toString());
        }
    }

    @KafkaListener(topics = {"task_change_log"})
    public void listenFlight(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            log.info("task_change_log---" + message.toString());
            if (message != null) {
                executor.submit(new MyReceiver(message.toString()));
            }
        }
    }
}
