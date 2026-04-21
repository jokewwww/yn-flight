package com.zh.receiver;

import com.zh.service.WorkTimeKafka;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@EnableScheduling
public class KafkaReceiver {


    public final static String TOPIC = "task_change_log";
    private final static ExecutorService executor = Executors.newFixedThreadPool(300);
    @Autowired
    private WorkTimeKafka workTimeKafka;

    @KafkaListener(topics = {TOPIC})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            if (message != null) {
                executor.submit(new MyReceiver(workTimeKafka, message.toString()));
            }
        }
    }

}