package com.higer.higerservice.util;

import com.alibaba.fastjson.JSON;
import com.higer.higerservice.component.KafkaService;
import com.higer.higerservice.component.ObjectProperties;
import com.higer.higerservice.entity.oilpro.ARecords;
import com.higer.higerservice.redis.RedisService;
import com.higer.higerservice.repository.oilpro.ARecordsRepository;
import com.pro.entity.ComboStatusToCec;
import com.pro.entity.OilGpsSignal;
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
    public boolean isWorked = false;
    public byte[] funcLock = new byte[0];
    @Autowired
    protected ARecordsRepository aRecordsRepository;
    @Autowired
    protected RedisService redisService;
    @Autowired
    protected KafkaService kafkaService;
    @Autowired
    private ObjectProperties objectProperties;

    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        errorDataSend = this;
        errorDataSend.aRecordsRepository = this.aRecordsRepository;
        errorDataSend.redisService = redisService;
        errorDataSend.kafkaService = kafkaService;
    }

    public void sendErrorData() {
        Boolean bool = true;
        Page<ARecords> all = errorDataSend.aRecordsRepository.findByFuncAndState("post", 1, new PageRequest(0, 10));
        for (ARecords aRecords : all.getContent()) {
            //此处为了服务器压力测试
            try {
                Thread.sleep(200L);
            } catch (InterruptedException e) {
                logger.error("ErrorDataSend ->sendErrorData->线程睡眠失败");
                e.printStackTrace();
            }
            if ("redis".equals(aRecords.getFunc())) {
                //我在发送 错误的redis数据
                try {
                    ComboStatusToCec comboStatus = JSON.parseObject(aRecords.getDate(), ComboStatusToCec.class);
                    if (null != comboStatus) {
                        OilGpsSignal oilGpsSignal = new OilGpsSignal();
                        oilGpsSignal.loadFromCombo(comboStatus);
                        String strGpsSignal = JSON.toJSONString(oilGpsSignal);
                        String key = "cargps:" + comboStatus.getAirport() + ":" + oilGpsSignal.getHp();
                        //errorDataSend.redisService.setStrToTime(key, strGpsSignal, TIME_OUT);
                        errorDataSend.redisService.setStr(key, strGpsSignal);
                    } else {
                        logger.error("在 ErrorDataSend 类中 59行 JSON转对象失败");
                        bool = false;
                        return;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    e.printStackTrace();
                    bool = false;
                    return;
                }
                try {
                    ARecords one = errorDataSend.aRecordsRepository.findOne(aRecords.getId());
                    if (one != null) {
                        errorDataSend.aRecordsRepository.delete(aRecords);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //错误的kafka数据 之前区分 car 和 gps  现在不区分 统一发送
                try {
                    System.out.println("发送kafka错误数据noReal=-=---------" + aRecords.getDate());
                    errorDataSend.kafkaService.send("carsignal", "noReal", aRecords.getDate(), aRecords.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //发送成功判断是否有遗留数据
        if (bool) {
            Page<ARecords> alls = errorDataSend.aRecordsRepository.findByFuncAndState("post", 1, new PageRequest(0, 10));
            if (alls.getContent().size() > 0) {
                notifyDo();
            }
        }
    }

    public void startWork() {
        if (true == isWorked)
            return;
        isWorked = true;
        start();
    }

    public void stopWork() {
        if (false == isWorked)
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
        while (true == isWorked) {
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
