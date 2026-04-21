package com.ncse.fdds;

import com.ncse.fdds.newibmmq.ReceiveConnect;
import com.ncse.fdds.protect.ProtectProcess;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FDDSApplication {

    public static void main(String[] args) {
        SpringApplication.run(FDDSApplication.class, args);
    }

    @Bean
    CommandLineRunner init(ReceiveConnect receiveConnect, ProtectProcess protectProcess) {
        return (args) -> {
            new Thread(receiveConnect).start();
            new Thread(protectProcess).start();
        };
    }
}
