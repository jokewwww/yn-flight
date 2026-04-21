package com.flight.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    /**
     * redis消息监听器容器
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter,
                                            MessageListenerAdapter listenerAdapterSingle,
                                            MessageListenerAdapter listenerAdapterLogout,
                                            MessageListenerAdapter listenerAdapterMsg,
                                            MessageListenerAdapter listenerAdapterNoticeManager,
                                            MessageListenerAdapter listenerAdapterRedisKeyevent
    ) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("chat"));
        container.addMessageListener(listenerAdapterSingle, new PatternTopic("chatSingle"));
        container.addMessageListener(listenerAdapterLogout, new PatternTopic("chatLogout"));
        container.addMessageListener(listenerAdapterMsg, new PatternTopic("chartMsg"));
        container.addMessageListener(listenerAdapterNoticeManager, new PatternTopic("noticeManager"));
        container.addMessageListener(listenerAdapterRedisKeyevent, new PatternTopic("__keyevent@*__:expired"));

        return container;
    }

    /**
     * 配置订阅者
     */
    @Bean
    MessageListenerAdapter listenerAdapterSingle(MessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessageSingle");
    }

    /**
     * 配置订阅者
     */
    @Bean
    MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    /**
     * 配置订阅者
     */
    @Bean
    MessageListenerAdapter listenerAdapterLogout(MessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessageLogout");
    }

    /**
     * 配置订阅者
     */
    @Bean
    MessageListenerAdapter listenerAdapterMsg(MessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveChartMsg");
    }

    /**
     * 配置订阅者
     */
    @Bean
    MessageListenerAdapter listenerAdapterNoticeManager(MessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "noticeManager");
    }


    /**
     * 配置订阅者
     */
    @Bean
    MessageListenerAdapter listenerAdapterRedisKeyevent(MessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "redisKeyevent");
    }
}
