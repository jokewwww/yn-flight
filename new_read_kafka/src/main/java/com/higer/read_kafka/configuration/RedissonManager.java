package com.higer.read_kafka.configuration;

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

    @Value("${spring.redis.host}")
    private String addressHost;

    @Value("${spring.redis.port}")
    private String addressPort;

    @Bean
    public RedissonClient getRedisson() throws Exception{
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://"+addressHost+":"+addressPort);
        RedissonClient redisson = Redisson.create(config);

        System.out.println(redisson.getConfig().toJSON().toString());
        return redisson;
    }
}
