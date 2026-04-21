package com.example.contrast_entity.receiver;

import com.example.contrast_entity.service.FlightChangeLogsService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@EnableScheduling
public class KafkaReceiver {


    @Autowired
    private FlightChangeLogsService flightChangeLogsService;

    private final static Logger log = LoggerFactory.getLogger(KafkaReceiver.class);
    private final static ExecutorService executor = Executors.newFixedThreadPool(300);
    public final static String TOPIC = "flight_change_log";

    @KafkaListener(topics = {TOPIC})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            if (message != null) {
                executor.submit(new MyReceiver(flightChangeLogsService, message.toString()));
            }
        }
    }

}