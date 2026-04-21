package com.zhoildataexchange.configuration;

import com.zhoildataexchange.util.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/11/6 20:39
 * @Description:
 */
@Component
public class MyBootListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(MyBootListener.class);

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String redisPort;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        JedisUtil jedisUtil = new JedisUtil();
        jedisUtil.setJedisPool(new JedisPool(redisHost,Integer.valueOf(redisPort)));
    }

}
