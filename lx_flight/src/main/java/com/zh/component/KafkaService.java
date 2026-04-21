package com.zh.component;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;


/**
 * @Auther: 修宏鑫
 * @Date: 2018/10/18 14:09
 * @Description:
 */
@Component
public class KafkaService {


    @Autowired
    private KafkaTemplate kafkaTemplate;

    //发送消息方法
    public void send(String topic, String msg) {
        try {
            ListenableFuture send = kafkaTemplate.send(topic, msg);
            kafkaTemplate.metrics();
            kafkaTemplate.execute(new KafkaOperations.ProducerCallback<String, String, Object>() {
                @Override
                public Object doInKafka(Producer<String, String> producer) {
                    return null;
                }
            });
            // 消息发送的监听器，用于回调返回信息
            kafkaTemplate.setProducerListener(new ProducerListener<String, String>() {
                @Override
                public void onError(String topic, Integer partition, String key, String value, Exception exception) {
                    System.out.println("kafka 失败了");
                }

                @Override
                public boolean isInterestedInSuccess() {
                    //System.out.println("是否启用监听  true 启用 false 不启用");
                    return true;
                }

                @Override
                public void onSuccess(String topic, Integer partition, String key, String value, RecordMetadata recordMetadata) {

                    System.err.println("kafka success 了 ");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
