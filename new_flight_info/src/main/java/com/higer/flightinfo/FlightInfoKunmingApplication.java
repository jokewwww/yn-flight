package com.higer.flightinfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityListeners;

@SpringBootApplication
@EnableJpaAuditing
public class FlightInfoKunmingApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightInfoKunmingApplication.class, args);
    }

}
