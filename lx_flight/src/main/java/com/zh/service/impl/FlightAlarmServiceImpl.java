package com.zh.service.impl;

import com.google.common.collect.Maps;
import com.zh.bean.flight.MyFlightAlarm;
import com.zh.bean.flight.MyFlightTask;
import com.zh.constant.Constant;
import com.zh.util.SendMsg2Redis;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/10/10 20:57
 * @Description:
 */
public class FlightAlarmServiceImpl implements Job {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        HashMap<Object, Object> webMap = Maps.newHashMap();
        //TODO 推送数据
        JobDataMap data = jobExecutionContext.getJobDetail().getJobDataMap();
        MyFlightTask flightInfoById = (MyFlightTask) data.get("flight");//需要推送的数据
        MyFlightAlarm flightAlarm = (MyFlightAlarm) data.get("alarm");
        webMap.put("flight", flightInfoById);
        webMap.put("alarm", flightAlarm);
        System.out.println("推送数据");
        SendMsg2Redis.testDingYue(stringRedisTemplate, flightAlarm.getFlalStaffId(), Constant.TASKFLIGHT, Constant.PC_ALARM, webMap);
    }
}
