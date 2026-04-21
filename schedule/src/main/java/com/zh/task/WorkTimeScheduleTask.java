package com.zh.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zh.bean.login.MyStaff;
import com.zh.component.RedissonDistributedLocker;
import com.zh.constant.Constant;
import com.zh.dao.mapper.StaffMapper;
import com.zh.dao.mapper.WorkTimeMapper;
import com.zh.entity.SetEntity;
import com.zh.entity.WorkTime;
import com.zh.entity.WorkTimeSend;
import com.zh.util.SendMsg2Redis;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
public class WorkTimeScheduleTask {

    @Autowired
    RedissonDistributedLocker redissonDistributedLocker;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private WorkTimeMapper workTimeMapper;
    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            long thisWorkTime = DateUtil.between(DateUtil.date(1640312561000L), new Date(), DateUnit.MINUTE, false);
            System.out.println((double) thisWorkTime);
            TimeUnit.SECONDS.sleep(60);
        }
    }

    // 删除定时派发
//    @Scheduled(cron = "0 0/1 * * * ?")
    //@Scheduled(cron = "0 0/1 * * * ?")
    @Scheduled(cron = " 0 0/1 * * * ?")
    private void sendWorkTime() {
        // 删除过去30分钟的
        Set<String> keys = stringRedisTemplate.keys(Constant.WORK_TIME + "*");
        if (keys == null || keys.size() == 0) {
            return;
        }
        keys.stream().forEach(dto -> {
            workTimeProcess(dto);
        });
    }

    private void workTimeProcess(String dto) {
        //            String cacheValue = stringRedisTemplate.opsForValue().get(dto);
//            WorkTime workTime = JSONUtil.toBean(cacheValue, WorkTime.class);
        String workTimeLockKey = "workTimeLock" + dto;
        boolean res = redissonDistributedLocker.tryLock(workTimeLockKey);
        if (!res) {
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.warn("Failed to acquire the lock.，线程ID{}", Thread.currentThread().getId());
            return;
        }
        try {
            WorkTime workTime = (WorkTime) redisTemplate.opsForValue().get(dto);
            if (workTime == null) return;
            String flgtAirportCode = workTime.getFlgtAirportCode();
            if (StrUtil.isBlank(flgtAirportCode)) return;
            //获取该机场的所有调度
            List<MyStaff> staffs = staffMapper.getStaffList(flgtAirportCode, null, 2);
            String staffIds = staffs.stream().map(MyStaff::getStaffId).collect(Collectors.joining(","));
            // 如果任务结束且过去30分钟没有接任务则删除
            if (ObjectUtil.isNotNull(workTime)
                    && ObjectUtil.isNotNull(workTime.getTaskDoneTime())
                    && ObjectUtil.isNotNull(workTime.getStatus())
                    && workTime.getStatus() == 2) {
                long dif = DateUtil.between(workTime.getTaskDoneTime(), new Date(), DateUnit.MINUTE, false);
                if (30 < dif) {
                    workTime.setRestStartDate(System.currentTimeMillis());
                    workTime.setFlnoList(null);
                    workTime.setTaskAccTime(null);
                    workTime.setTaskDoneTime(null);
                    workTime.setAllWorkTime(null);
                    workTime.setStatus(2);
                    redisTemplate.opsForValue().set(dto, workTime);
                }
            }

            SetEntity setting = workTimeMapper.getSetting();

            // 发送消息 本次加油时间和累计加油时间
            if (ObjectUtil.isNotNull(workTime) && ObjectUtil.isNotNull(workTime.getStatus()) && workTime.getStatus() == 2) {
                if (CollectionUtil.isNotEmpty(workTime.getFlnoList())) {
                    // 累计加油时间
                    long allTime = DateUtil.between(workTime.getFlnoList().get(0).getTaskAccTime(), new Date(), DateUnit.MINUTE, false);
                    workTime.setAllWorkTime((double) allTime);
                    workTime.setRestTime(0.0);
                    // 如果累计工作时间大于
                    if (allTime > Double.parseDouble(setting.getTime())) {
                        long lastSendTime = workTime.getLastSendTime() == null ? System.currentTimeMillis() : System.currentTimeMillis() - workTime.getLastSendTime();
                        lastSendTime = lastSendTime / 1000 / 60;
                        if (lastSendTime >= Double.parseDouble(setting.getIntervalTime())) {
                            if (StrUtil.isNotBlank(staffIds)) {
                                SendMsg2Redis.testDingYue(stringRedisTemplate, staffIds, Constant.STAFF, Constant.PC_WORK_TIME_ALARM, new WorkTimeSend(workTime));
                            }
                            workTime.setLastSendTime(System.currentTimeMillis());
                        }
                    }
                } else {
                    // 休息时间
                    workTime.setWorkTime(0.0);
                    if (ObjectUtil.isNotNull(workTime.getRestStartDate())) {
                        workTime.setRestTime(NumberUtil.round((System.currentTimeMillis() - workTime.getRestStartDate()) / 1000 / 60, 2).doubleValue());
                    }

                }
                if (StrUtil.isNotBlank(staffIds)) {
                    SendMsg2Redis.testDingYue(stringRedisTemplate, staffIds, Constant.STAFF, Constant.PC_WORK_TIME, new WorkTimeSend(workTime));
                }
            }

            if (ObjectUtil.isNotNull(workTime) && ObjectUtil.isNotNull(workTime.getStatus()) && workTime.getStatus() == 1) {
                // 休息时间
                workTime.setRestTime(0.0);
                // 本次工作时间
                long thisWorkTime = DateUtil.between(workTime.getTaskAccTime(), new Date(), DateUnit.MINUTE, false);
                workTime.setWorkTime((double) thisWorkTime);
                // 累计加油时间
                if (CollectionUtil.isNotEmpty(workTime.getFlnoList())) {
                    long allTime = DateUtil.between(workTime.getFlnoList().get(0).getTaskAccTime(), new Date(), DateUnit.MINUTE, false);
                    workTime.setAllWorkTime((double) allTime);
                } else {
                    workTime.setAllWorkTime((double) thisWorkTime);
                }
//                executorService.execute(new SysThread(stringRedisTemplate,workTime));
                if (StrUtil.isNotBlank(staffIds)) {
                    SendMsg2Redis.testDingYue(stringRedisTemplate, staffIds, Constant.STAFF, Constant.PC_WORK_TIME, new WorkTimeSend(workTime));
                }
                double allTime = workTime.getAllWorkTime();
                // 如果累计工作时间大于设置报警时间进行报警
                if (allTime > Double.parseDouble(setting.getTime())) {
                    long lastSendTime = workTime.getLastSendTime() == null ? System.currentTimeMillis() : System.currentTimeMillis() - workTime.getLastSendTime();
                    lastSendTime = lastSendTime / 1000 / 60;
                    if (lastSendTime >= Double.parseDouble(setting.getIntervalTime())) {
                        if (StrUtil.isNotBlank(staffIds)) {
                            SendMsg2Redis.testDingYue(stringRedisTemplate, staffIds, Constant.STAFF, Constant.PC_WORK_TIME_ALARM, new WorkTimeSend(workTime));
                        }
                        workTime.setLastSendTime(System.currentTimeMillis());
                    }
                }
            }
            // 放心新值
            redisTemplate.opsForValue().set(dto, workTime);
        } finally {
            redissonDistributedLocker.unlock(workTimeLockKey);
        }

    }
}
