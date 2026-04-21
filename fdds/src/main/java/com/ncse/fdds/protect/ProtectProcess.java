package com.ncse.fdds.protect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class ProtectProcess implements Runnable {

    public static long lastDataTime;

    static {
        lastDataTime = new Date().getTime();
    }

    @Value("${millsTimeOut}")
    private long millsTimeOut;

    public static void relet() {
        lastDataTime = new Date().getTime();
    }

    /**
     * xx秒检测是否有数据写入 如果没有退出程序 让k8s重启服务
     */
    @Override
    public void run() {
        /*while (true){
            System.out.println("222222222222");
            long now=new Date().getTime();
            if(now-lastDataTime>millsTimeOut)
                log.info("程序非正常退出1");
                System.exit(1);//程序非正常退出
            try {
                Thread.sleep((int)(millsTimeOut/10));
            } catch (InterruptedException e) {
                log.info("程序非正常退出2");
                System.exit(1);//程序非正常退出
            }
        }*/
    }
}
