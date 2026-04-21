package com.higer.statistical;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class StatisticalApplication {

    public static void main(String[] args) {
        try {
            ConfigurableApplicationContext run = SpringApplication.run(StatisticalApplication.class, args);
            String[] activeProfiles = run.getEnvironment().getActiveProfiles();
            for (String s : activeProfiles) {
                System.out.println("StatisticalApplication 使用的 profiles 为 : " + s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

