package com.ncse.fdds;

import com.ncse.fdds.newibmmq.Receive;
import com.ncse.fdds.newibmmq.ReceiveConnect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.kafka.annotation.EnableKafka;

@org.springframework.context.annotation.Configuration
@EnableKafka
@ComponentScan
@Slf4j
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class Configuration {

    @Bean
    public Receive receive() {
        return new Receive();
    }

    @Bean
    public ReceiveConnect receiveConnect(Receive receive) {
        return new ReceiveConnect(receive);
    }

}
