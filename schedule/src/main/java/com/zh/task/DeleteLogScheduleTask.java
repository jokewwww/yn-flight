package com.zh.task;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.zh.bean.flight.SysLog;
import com.zh.bean.zuul.SysRequestLog;
import com.zh.dao.mapper.SysLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.concurrent.ExecutorService;

/**
 * Copyright: Copyright (c) 2021 hge
 *
 * @ClassName: FlightAlarmScheduleTask.java
 * @Description:
 * @version: v1.0.0
 * @author: nimz
 * @date: 2021年11月18日 15:25
 */
@Slf4j
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class DeleteLogScheduleTask {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Autowired
    private ExecutorService executorService;

    public static void main(String[] args) throws InterruptedException {
        DateTime dateTime = DateUtil.offsetDay(new Date(), -60);
        System.out.println(dateTime);
    }

    /**
     * 删除log
     */
    @Scheduled(cron = " 0 0 1 * * ? ")
    private void deleteLog() {
        DateTime dateTime = DateUtil.offsetDay(new Date(), -100);
        SysLog sysLog = new SysLog();
        sysLog.setCreateDate(dateTime);
        sysLogMapper.deleteLog(sysLog);

        DateTime dateRequestTime = DateUtil.offsetDay(new Date(), -60);
        SysRequestLog sysRequestLog = new SysRequestLog();
        sysRequestLog.setCreateDate(dateRequestTime);
        sysLogMapper.deleteSysRequestLog(sysRequestLog);
    }
}
