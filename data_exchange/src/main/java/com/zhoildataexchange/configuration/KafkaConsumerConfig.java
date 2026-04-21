package com.zhoildataexchange.configuration;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/10/26 11:02
 * @Description:
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keySerializer;

    @Value("${spring.kafka.consumer.value-deserializer}")
    private String valueSerializer;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Bean
    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keySerializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueSerializer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        /* 自动确认offset的时间间隔  */
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "3000");
        //消息发送的最长等待时间.需大于session.timeout.ms这个时间
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, "40000");
        //一次从kafka中poll出来的数据条数
        //max.poll.records条数据需要在在session.timeout.ms这个时间内处理完
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,"5");
        //server发送到消费端的最小数据，若是不满足这个数值则会等待直到满足指定大小。默认为1表示立即接收。
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, "1");
        //若是不满足fetch.min.bytes时，等待消费端请求的最长等待时间
        //props.put("fetch.wait.max.ms", "1000");
        return props;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
