package com.higer.oildataexchange.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

/**
 * @classname: KafkaConfiguration
 * @author: zhaojiacan
 * @description: KafkaConfiguration 配置
 * @date: 2024/5/23 19:48
 * @version:1.0
 */
@Configuration
@Slf4j
public class KafkaConfiguration {

    @Bean
    @Primary
    public ErrorHandler kafkaErrorHandler(KafkaTemplate<?, ?> template) {

        log.warn("kafkaErrorHandler begin to Handle");
        // <1> 创建 DeadLetterPublishingRecoverer 对象
        ConsumerRecordRecoverer recoverer = new DeadLetterPublishingRecoverer(template);
        // <2> 创建 FixedBackOff 对象   设置重试间隔 10秒 次数为 3次
        BackOff backOff = new FixedBackOff(10 * 1000L, 3L);
        // <3> 创建 SeekToCurrentErrorHandler 对象
        return new SeekToCurrentErrorHandler(recoverer, backOff);
    }
}
