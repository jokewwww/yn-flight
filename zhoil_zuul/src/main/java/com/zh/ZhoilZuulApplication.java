package com.zh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@EnableZuulProxy
@SpringBootApplication
public class ZhoilZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZhoilZuulApplication.class, args);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
