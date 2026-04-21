package com;

import com.higer.higerservice.util.ErrorDataSend;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HigerserviceApplication {
    public static void main(String[] args) {
        try {
            ConfigurableApplicationContext run = SpringApplication.run(HigerserviceApplication.class, args);
            String[] activeProfiles = run.getEnvironment().getActiveProfiles();
            for (String s : activeProfiles) {
                System.out.println("Spring boot 使用的 profiles 为 : " + s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bean
    public ErrorDataSend errorDataSend() {
        return new ErrorDataSend();
    }
}
