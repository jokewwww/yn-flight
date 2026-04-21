package com.zh.configuration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: 修宏鑫
 * @Date: 2020/4/7 13:59
 * @Description: 分布式锁
 */
@Configuration
public class RedissonManager {

    @Value("${redisson.address}")
    private String addressUrl;

    @Bean
    public RedissonClient getRedisson() throws Exception {
        RedissonClient redisson = null;
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + addressUrl)
                .setPassword("Asdfghjkl");
        redisson = Redisson.create(config);

        System.out.println(redisson.getConfig().toJSON().toString());
        return redisson;
    }
}
