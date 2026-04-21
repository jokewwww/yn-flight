package com.zh.thread;

import com.zh.bean.flight.SysLog;
import com.zh.service.SysLogService;

public class SysLogThread implements Runnable {

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
        sysLogService.insert(sysLog);
    }
}
