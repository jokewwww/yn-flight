package com.zh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zh.bean.login.MyFuel;
import com.zh.bean.login.MyStaff;
import com.zh.constant.Constant;
import com.zh.dao.mapper.my.NewFuelMapper;
import com.zh.dao.mapper.my.StaffMapper;
import com.zh.util.SendMsg2Redis;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class MyFuelJobService extends MyFuel implements Job {

    private final static Logger log = LoggerFactory.getLogger(MyFuelJobService.class);

    @Autowired
    private NewFuelMapper fuelMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private StaffMapper staffMapper;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("执行定时任务：" + JSONObject.toJSONString(this));
        fuelMapper.updateNewFuelFlag(this, 0);
        log.debug("c：" + this.getFuelTestBillNo() + "-" + LocalDateTime.now().toString());
        List<MyStaff> list = staffMapper.getAllStaffList(this.getFuelAirportCode(), null, null);//根据机场代码查询所有人员
        String to = list.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));//人员拼接
//        log.info("推送订阅到："+to);
        SendMsg2Redis.testDingYue(stringRedisTemplate, to, Constant.MANAGER_STAFF, Constant.OIL_SAVE, this);//推送
    }
}
