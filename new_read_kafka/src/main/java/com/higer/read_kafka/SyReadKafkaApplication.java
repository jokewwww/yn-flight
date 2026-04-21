package com.higer.read_kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class SyReadKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SyReadKafkaApplication.class, args);
    }

}
