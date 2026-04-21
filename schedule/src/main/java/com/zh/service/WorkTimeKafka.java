package com.zh.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.zh.bean.flight.MyFlightTask;
import com.zh.component.RedissonDistributedLocker;
import com.zh.constant.Constant;
import com.zh.entity.Task;
import com.zh.entity.WorkTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

/**
 * Copyright: Copyright (c) 2021 hge
 *
 * @ClassName: FlightAlarmScheduleTask.java
 * @Description:
 * @version: v1.0.0
 * @author: nimz
 * @date: 2021年11月18日 15:25
 */
@Service
public class WorkTimeKafka {

    @Autowired
    RedissonDistributedLocker redissonDistributedLocker;
    @Autowired
    private RedisTemplate redisTemplate;

    public static void main(String[] args) {
    }


    public void readKafka(String msg) {
        MyFlightTask kafkaData = JSONObject.parseObject(msg, MyFlightTask.class);
        if (ObjectUtil.isNull(kafkaData)) {
            return;
        }
        String redisKey = Constant.WORK_TIME + ":" + kafkaData.getTaskOpeStaffId();
        String workTimeLockKey = "workTimeLock" + redisKey;
        boolean res = redissonDistributedLocker.tryLock(workTimeLockKey);
        if (!res) {
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.warn("Failed to acquire the lock.，线程ID{}", Thread.currentThread().getId());
            return;
        }
        try {
            // 任务接收
            if ((kafkaData.getTaskStatus() == 3)) {
                if (redisTemplate.hasKey(redisKey)) {
                    WorkTime workTime = (WorkTime) redisTemplate.opsForValue().get(redisKey);
                    workTime.setStaffId(kafkaData.getTaskOpeStaffId());
                    workTime.setStaffName(kafkaData.getTaskOpeStaffName());
                    workTime.setTaskId(kafkaData.getTaskId());
                    workTime.setFlno(kafkaData.getFlgtFlno());
                    workTime.setFlgtAirportCode(kafkaData.getFlgtAirportCode());
                    if (ObjectUtil.isNotEmpty(kafkaData.getTaskAccTime())
                            && ObjectUtil.isNotEmpty(workTime.getTaskDoneTime())) {
                        long dif = DateUtil.between(workTime.getTaskDoneTime(), kafkaData.getTaskAccTime(), DateUnit.MINUTE, false);
                        if (30 >= dif) {
                            // 两次任务间隔小于30分钟
                            workTime.setTaskAccTime(kafkaData.getTaskAccTime());
                        } else if (30 < dif) {
                            // 清空
                            workTime.setWorkTime(0.0);
                            workTime.setFlnoList(null);
                            workTime.setTaskDoneTime(null);
                            workTime.setTaskAccTime(kafkaData.getTaskAccTime());
                            workTime.setAllWorkTime(null);
                        }
                    } else {
                        workTime.setTaskAccTime(kafkaData.getTaskAccTime());
                    }
                    workTime.setStatus(1);
                    redisTemplate.opsForValue().set(redisKey, workTime);
                } else {
                    WorkTime workTime = new WorkTime();
                    workTime.setStaffId(kafkaData.getTaskOpeStaffId());
                    workTime.setFlgtAirportCode(kafkaData.getFlgtAirportCode());
                    workTime.setFlno(kafkaData.getFlgtFlno());
                    workTime.setStaffName(kafkaData.getTaskOpeStaffName());
                    workTime.setTaskId(kafkaData.getTaskId());
                    workTime.setTaskAccTime(kafkaData.getTaskAccTime());
                    workTime.setStatus(1);
                    redisTemplate.opsForValue().set(redisKey, workTime);
                }
            }

            // 任务完成
            if (kafkaData.getTaskStatus() == 7 || kafkaData.getTaskStatus() == 9
                    || kafkaData.getTaskStatus() == 0 || kafkaData.getTaskStatus() == 10) {
                if (kafkaData.getTaskStatus() == 10) {
                    kafkaData.setTaskDoneTime(new Date());
                }
                if (redisTemplate.hasKey(redisKey)) {
//                String cacheValue =  redisTemplate.opsForValue().get(redisKey);
                    WorkTime workTime = (WorkTime) redisTemplate.opsForValue().get(redisKey);
                    if (ObjectUtil.isNotEmpty(kafkaData.getTaskAccTime())
                            && ObjectUtil.isNotEmpty(kafkaData.getTaskDoneTime())) {
                        workTime.setTaskDoneTime(kafkaData.getTaskDoneTime());
                        workTime.setFlno(kafkaData.getFlgtFlno());
                        long thisTime = DateUtil.between(kafkaData.getTaskAccTime(), kafkaData.getTaskDoneTime(), DateUnit.MINUTE, false);
                        workTime.setWorkTime((double) thisTime);
                        Task task = new Task();
                        task.setTaskAccTime(kafkaData.getTaskAccTime());
                        task.setTaskDoneTime(kafkaData.getTaskDoneTime());
                        task.setFlno(kafkaData.getFlgtFlno());
                        task.setTaskId(kafkaData.getTaskId());
                        if (workTime.getFlnoList() == null) {
                            ArrayList<Task> arrayList = new ArrayList<>();
                            arrayList.add(task);
                            workTime.setFlnoList(arrayList);
                        } else {
                            workTime.getFlnoList().add(task);
                            workTime.setFlnoList(workTime.getFlnoList());
                        }

                    } else {
                    }
                    workTime.setStatus(2);
                    redisTemplate.delete(redisKey);
                    redisTemplate.opsForValue().set(redisKey, workTime);
                } else {
                    // 没有任务开始时间暂时不处理
                }
            }
        } finally {
            redissonDistributedLocker.unlock(workTimeLockKey);
        }

        // 任务取消
//        if(ObjectUtil.isNotNull(kafkaData) && (kafkaData.getTaskStatus() == 9 || kafkaData.getTaskStatus() == 0 || kafkaData.getTaskStatus() == 10)){
//            String redisKey = Constant.WORK_TIME + ":" + kafkaData.getTaskOpeStaffId();
//
//            if(redisTemplate.hasKey(redisKey)){
//                WorkTime workTime = (WorkTime)redisTemplate.opsForValue().get(redisKey);
//                if(ObjectUtil.equal(kafkaData.getTaskId(),workTime.getTaskId())){
////                    workTime.setTaskId(null);
//                    workTime.setStatus(2);
//                    workTime.setRestStartDate(null);
//                    workTime.setTaskDoneTime(new Date());
//                    redisTemplate.opsForValue().set(redisKey,workTime);
//                }
//            }
//        }

        // 挂起
//        if(ObjectUtil.isNotNull(kafkaData) && kafkaData.getTaskStatus() == 10){
//            String redisKey = Constant.WORK_TIME + ":" + kafkaData.getTaskOpeStaffId();
//
//            if(redisTemplate.hasKey(redisKey)){
//                WorkTime workTime = (WorkTime)redisTemplate.opsForValue().get(redisKey);
//                if(ObjectUtil.equal(kafkaData.getTaskId(),workTime.getTaskId())){
////                    workTime.setTaskId(null);
//                    workTime.setStatus(2);
//                    workTime.setRestStartDate(System.currentTimeMillis());
//                    redisTemplate.opsForValue().set(redisKey,workTime);
//                }
//            }
//        }

    }

}
