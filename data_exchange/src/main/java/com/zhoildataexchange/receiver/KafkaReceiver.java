package com.zhoildataexchange.receiver;

import com.zhoildataexchange.redis.RedisService;
import com.zhoildataexchange.service.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/1/28 10:31
 * @Description:
 */
@Component
@EnableScheduling
public class KafkaReceiver {

    private final static Logger log = LoggerFactory.getLogger(KafkaReceiver.class);

    private final static ExecutorService executor = Executors.newFixedThreadPool(3000);

    public final static String TOPIC = "flight_exchange";

    @Autowired
    private KafkaService kafkaService;
    @Autowired
    private RedisService redisService;

    @KafkaListener(topics = {TOPIC})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            System.err.println(message.toString());
            if (message != null) {
                executor.submit(new MyReceiver(kafkaService, message.toString()));
            }
        }
    }

}
