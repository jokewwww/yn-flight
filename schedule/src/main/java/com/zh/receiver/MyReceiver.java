package com.zh.receiver;

import com.zh.service.WorkTimeKafka;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyReceiver extends Thread {
    private String msg;
    private WorkTimeKafka workTimeKafka;

    public MyReceiver(WorkTimeKafka workTimeKafka, String msg) {
        this.workTimeKafka = workTimeKafka;
        this.msg = msg;
    }

    @Override
    public void run() {
        try {
            workTimeKafka.readKafka(msg);
        } catch (Exception e) {
            log.error("回传数据异常错误：{}", e.getMessage());
            e.printStackTrace();
        }
    }
}
