package com.ncse.fdds.protect;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class CronTest {

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void doRestart() {
        System.out.println("--------关闭");
        System.exit(1);
    }

}