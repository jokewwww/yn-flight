package com.higer.oildataexchange.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/11/6 20:39
 * @Description:
 */
@Component
@Slf4j
public class MyBootListener implements ApplicationListener<ContextRefreshedEvent> {


    private static ErrorDataSend errorDataSend = null;


    public static void startWork() {
        if (null != errorDataSend) {
            errorDataSend.stopWork();
            errorDataSend = null;
        }
        errorDataSend = new ErrorDataSend();
        errorDataSend.startWork();
    }

    public static void stopWorkKafka() {
        if (null != errorDataSend) {
            errorDataSend.stopWork();
            errorDataSend = null;
        }
    }

    public static void notifyKafka() {
        //上线之后开启
        errorDataSend.notifyDo();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        MyBootListener.startWork();
    }


}
