package com.flight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ZhoilWsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhoilWsApplication.class, args);
    }

    @Bean
    protected RestTemplate restTemplate() {
        return new RestTemplate();
    }
}