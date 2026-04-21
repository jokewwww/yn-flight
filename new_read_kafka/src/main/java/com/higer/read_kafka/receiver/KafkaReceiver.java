package com.higer.read_kafka.receiver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.higer.read_kafka.entity.flight.in.InTFlight;
import com.higer.read_kafka.redis.RedisService;
import com.higer.read_kafka.service.KafkaService;
import com.higer.read_kafka.util.JsonUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
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

    private final static ExecutorService executor = Executors.newFixedThreadPool(2000);

    public final static String TOPIC = "flight";

    @Autowired
    private KafkaService kafkaService;
    @Autowired
    private RedisService redisService;

    @KafkaListener(topics = { TOPIC })
    public void listen(ConsumerRecord<?, ?> record) {
        System.out.println("kafka消费分区 ------》"+record.partition());
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            System.out.println("kafka 刚接收到的数据 ------》"+message.toString());
            System.out.println("kafka 消息序号 ------》"+ JSONObject.parseObject(message.toString()).getString("msgSeqn"));
            if (message != null) {
                executor.submit(new MyReceiver(kafkaService, message.toString()));
            }
        }
    }

    /**
     *  凌晨4点，页面切换ws通知处理
     */
    @Scheduled(cron = "0 50 3 * * ?")
    public void change() {
        //凌晨4点清除 key
        String s = "sequenceKey";
        redisService.del(s);


        kafkaService.flight4Change();
    }

}
