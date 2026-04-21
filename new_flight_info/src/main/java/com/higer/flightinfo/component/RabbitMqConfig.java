package com.higer.flightinfo.component;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public final static String QUEUE_NAME="YUNNAN_ACDM_HANGYOU_Q";


    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory, RabbitMqConsumer rabbitMqConsumer) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setConcurrentConsumers(1); // 设置消费者数量为1
        container.setMaxConcurrentConsumers(1); // 设置最大消费者数量为1
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 启用手动确认
        container.setMessageListener(new MessageListenerAdapter(rabbitMqConsumer,"process"));
        return container;
    }
}
