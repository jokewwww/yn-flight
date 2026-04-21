package com.zh.thread;

import com.zh.bean.flight.SysLog;
import com.zh.service.SysLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SysLogThread implements Runnable {

    private final static Logger log = LoggerFactory.getLogger(SysLogThread.class);
    private SysLog sysLog;
    private SysLogService sysLogService;

    public SysLogThread(SysLog sysLog, SysLogService sysLogService) {
        super();
        this.sysLog = sysLog;
        this.sysLogService = sysLogService;
    }

    @Override
    public void run() {
        // 保存系统日志
        log.info("当前子线程：" + Thread.currentThread().getName());
        sysLogService.insert(sysLog);
    }
}
