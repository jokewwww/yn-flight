package com.higer.oildataexchange.common;

import com.higer.oildataexchange.entity.flight.TFlightError;
import com.higer.oildataexchange.repository.TFlightErrorRepository;
import com.higer.oildataexchange.service.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/11/2 17:35
 * @Description: 处理发送失败的kafka  和redis
 */
@Component
public class ErrorDataSend extends Thread {

    private static final long TIME_OUT = 60 * 15;
    private static Logger logger = LoggerFactory.getLogger(ErrorDataSend.class);
    private static ErrorDataSend errorDataSend;
    public final byte[] funcLock = new byte[0];
    public boolean isWorked = false;
    @Autowired
    protected TFlightErrorRepository tFlightErrorRepository;
    @Autowired
    protected KafkaProducerService kafkaService;

    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        errorDataSend = this;
    }

    public void sendErrorData() {
        boolean bool = true;
        Page<TFlightError> all = errorDataSend.tFlightErrorRepository.findAll(PageRequest.of(0, 10));
        for (TFlightError tFlightError : all.getContent()) {
            //此处为了服务器压力测试
            try {
                Thread.sleep(200L);
            } catch (InterruptedException e) {
                logger.error("ErrorDataSend ->sendErrorData->线程睡眠失败");
                e.printStackTrace();
            }
            //错误的kafka数据 之前区分 car 和 gps  现在不区分 统一发送
            try {
                errorDataSend.kafkaService.send("carsignal", tFlightError.getErrorData(), tFlightError.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //发送成功判断是否有遗留数据
        Page<TFlightError> alls = errorDataSend.tFlightErrorRepository.findAll(PageRequest.of(0, 10));
        if (alls.getContent().size() > 0) {
            notifyDo();
        }
    }

    public void startWork() {
        if (isWorked)
            return;
        isWorked = true;
        start();
    }

    public void stopWork() {
        if (!isWorked)
            return;
        isWorked = false;
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isWorked) {
            synchronized (funcLock) {
                try {
                    funcLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            sendErrorData();
        }
    }

    public void notifyDo() {
        //dosome
        synchronized (funcLock) {
            funcLock.notify();
        }
    }
}
