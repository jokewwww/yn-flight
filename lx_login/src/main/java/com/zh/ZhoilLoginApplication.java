package com.zh;

import com.zh.filter.HttpServletRequestReplacedFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ZhoilLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhoilLoginApplication.class, args);
    }

    @Bean
    protected RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public FilterRegistrationBean httpServletRequestReplacedRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpServletRequestReplacedFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("httpServletRequestReplacedFilter");
        registration.setOrder(1);
        return registration;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean("redisTemplate")
    public RedisTemplate redisTemplate(@Lazy RedisConnectionFactory connectionFactory) {
        RedisTemplate redis = new RedisTemplate();
        GenericToStringSerializer<String> keySerializer = new GenericToStringSerializer<String>(String.class);
        redis.setKeySerializer(keySerializer);
        redis.setHashKeySerializer(keySerializer);
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
        redis.setValueSerializer(valueSerializer);
        redis.setHashValueSerializer(valueSerializer);
        redis.setConnectionFactory(connectionFactory);

        return redis;
    }
}
