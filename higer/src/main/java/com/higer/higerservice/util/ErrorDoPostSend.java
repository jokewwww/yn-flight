package com.higer.higerservice.util;

import com.higer.higerservice.component.ObjectProperties;
import com.higer.higerservice.entity.oilpro.ARecords;
import com.higer.higerservice.repository.oilpro.ARecordsRepository;
import com.higer.higerservice.service.CommInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/11/3 10:30
 * @Description: 处理未发送成功的油单
 */
@Component
public class ErrorDoPostSend extends Thread {
    private static ErrorDoPostSend errorDoPostSend;
    public boolean isWorked = false;
    public byte[] funcLock = new byte[0];
    @Autowired
    protected ARecordsRepository aRecordsRepository;
    @Autowired
    protected CommInterfaceService commInterfaceService;
    @Autowired
    private ObjectProperties objectProperties;

    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        errorDoPostSend = this;
        errorDoPostSend.aRecordsRepository = this.aRecordsRepository;
        errorDoPostSend.commInterfaceService = commInterfaceService;
    }

    public void sendErrorData() {
        String url = null;
        Boolean bool = true;
        String myUrl = objectProperties.getMyUrl();
        if (myUrl != null) {
            url = myUrl;
        }
        Page<ARecords> all = errorDoPostSend.aRecordsRepository.findByDoPost("post", 1, new PageRequest(0, 10));
        String finalUrl = url;
        for (ARecords aRecords : all.getContent()) {
            if (null != finalUrl) {
                try {
                    ResponseObject responseObject = errorDoPostSend.commInterfaceService.doPost(finalUrl, aRecords.getDate().getBytes(), aRecords.getId());
                    //发送失败
                    if (responseObject.getCode() != 0) {
                        bool = false;
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    bool = false;
                    return;
                }
            }
        }
        //查询是否还存有遗留数据
        if (bool) {
            Page<ARecords> alls = errorDoPostSend.aRecordsRepository.findByDoPost("post", 1, new PageRequest(0, 10));
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
