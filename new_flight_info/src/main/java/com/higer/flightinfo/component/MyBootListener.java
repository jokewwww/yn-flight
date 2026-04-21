package com.higer.flightinfo.component;

import com.higer.flightinfo.util.ErrorDataSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/11/6 20:39
 * @Description:
 */
@Component
public class MyBootListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(MyBootListener.class);

    private static ErrorDataSend errorDataSend = null;


    public static void startWork() {
        if (null != errorDataSend) {
            errorDataSend.stopWork();
            errorDataSend = null;
        }else{
            errorDataSend = new ErrorDataSend();
            errorDataSend.startWork();
        }
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
            MyBootListener.startWork();
    }




}
