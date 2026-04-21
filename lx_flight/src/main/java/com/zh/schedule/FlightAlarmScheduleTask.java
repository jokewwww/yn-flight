package com.zh.schedule;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.zh.bean.flight.MyNewFlight;
import com.zh.bean.flight.MyNoTaskAlarmFlight;
import com.zh.bean.flight.MyNoTaskAlarmFlightSend;
import com.zh.bean.login.FlgtType;
import com.zh.constant.Constant;
import com.zh.dao.mapper.my.StaffMapper;
import com.zh.dao.mapper.my.TaskMapper;
import com.zh.service.FlgtTypeService;
import com.zh.service.FlightService;
import com.zh.util.SendMsg2Redis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Set;

/**
 * Copyright: Copyright (c) 2021 hge
 *
 * @ClassName: FlightAlarmScheduleTask.java
 * @Description:
 * @version: v1.0.0
 * @author: nimz
 * @date: 2021年11月18日 15:25
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class FlightAlarmScheduleTask {

    private final static Logger log = LoggerFactory.getLogger(FlightAlarmScheduleTask.class);

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private FlgtTypeService flgtTypeService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private FlightService flightService;

    public static void main(String[] args) {
        MyNoTaskAlarmFlight myNoTaskAlarmFlight = new MyNoTaskAlarmFlight();
        myNoTaskAlarmFlight.setTaskId("1");
        System.out.println(JSONUtil.toJsonStr(myNoTaskAlarmFlight));
    }

    // 删除定时派发
    //  @Scheduled(cron = "0 0/1 * * * ?")
//    @Scheduled(cron = " 0/15 * * * * ?")
    private void noTaskAlarmFlightDelete() {
//        System.err.println("执行获取当前未分配任务报警的航班删除: " + LocalDateTime.now());

        Set<String> keysLike = stringRedisTemplate.keys(Constant.PC_NO_TASK_FLIGHT_ALARM_R + ":*");
        if (keysLike == null || keysLike.size() == 0) {
            return;
        }

        String[] split = getLoginStrings();
        if (split == null) {
            return;
        }

        List<MyNoTaskAlarmFlight> noTaskAlarmFlight = taskMapper.getNoTaskAlarmFlight();

        keysLike.stream().forEach(dto -> {

            String cacheValue = stringRedisTemplate.opsForValue().get(dto);
            MyNoTaskAlarmFlight myNoTaskAlarmFlight = JSONUtil.toBean(cacheValue, MyNoTaskAlarmFlight.class);
            boolean present = noTaskAlarmFlight.stream().filter(myDto -> myNoTaskAlarmFlight.getFlgtId().equals(myDto.getFlgtId())).findFirst().isPresent();
            if (!present) {
                myNoTaskAlarmFlight.setType(1);
                SendMsg2Redis.testDingYue(stringRedisTemplate, split[1], Constant.TASKFLIGHT, Constant.PAD_BROADCAST, new MyNoTaskAlarmFlightSend(myNoTaskAlarmFlight));
                stringRedisTemplate.delete(Constant.PC_NO_TASK_FLIGHT_ALARM_R + ":" + myNoTaskAlarmFlight.getFlgtId());
            }


        });
    }

    // 定时查询未分配任务报警
    // @Scheduled(cron = "0 0/1 * * * ?")
    private void noTaskAlarmFlight() {
//        System.err.println("执行获取当前未分配任务报警的航班: " + LocalDateTime.now());
        List<MyNoTaskAlarmFlight> noTaskAlarmFlight = taskMapper.getNoTaskAlarmFlight();
        String[] split = getLoginStrings();
        if (split == null) {
            return;
        }

        noTaskAlarmFlight.stream()
                .filter(dto -> !redisTemplate.hasKey(Constant.PC_NO_TASK_FLIGHT_ALARM_R + ":" + dto.getFlgtId()))
                .forEach(dto -> {
                    // 向前端发送一次
                    dto.setTime(System.currentTimeMillis());
                    dto.setType(0);
                    redisTemplate.opsForValue().setIfAbsent(Constant.PC_NO_TASK_FLIGHT_ALARM_R + ":" + dto.getFlgtId(), dto);
                    SendMsg2Redis.testDingYue(stringRedisTemplate, split[1], Constant.TASKFLIGHT, Constant.PAD_BROADCAST, new MyNoTaskAlarmFlightSend(dto));
                });

    }

    /**
     * 获取当前登录人
     *
     * @return
     */
    private String[] getLoginStrings() {
        Set<String> keys = stringRedisTemplate.keys(Constant.PC_LOGIN + "*");
        if (keys == null || keys.size() == 0) {
            return null;
        }
        String next = keys.iterator().next();
        String[] split = next.split(":");
        return split;
    }

    // 间隔报警
    @Scheduled(cron = "0 0/1 * * * ?")
    private void noTaskAlarmFlightInterval() {

        Set<String> keysLike = stringRedisTemplate.keys(Constant.PC_NO_TASK_FLIGHT_ALARM_R + ":*");
        if (keysLike == null || keysLike.size() == 0) {
            return;
        }

        String[] split = getLoginStrings();
        if (split == null) {
            return;
        }

        List<FlgtType> flgtTypeList = flgtTypeService.getFlgtType(new FlgtType());

        for (String accurateKey : keysLike) {
            String cacheValue = stringRedisTemplate.opsForValue().get(accurateKey);
            MyNoTaskAlarmFlight myNoTaskAlarmFlight = JSONUtil.toBean(cacheValue, MyNoTaskAlarmFlight.class);
            flgtTypeList.stream().filter(dto -> dto.getFlgtAcname().equals(myNoTaskAlarmFlight.getFlgtAcname()))
                    .findFirst()
                    .filter(dto -> {
                        long intervalNow = (System.currentTimeMillis() - myNoTaskAlarmFlight.getTime()) / 1000 / 60;
                        return intervalNow - Long.parseLong(dto.getIntervalTime()) >= 0;
                    })
                    .ifPresent(dto -> {
                        log.info("提醒=================================");
                        myNoTaskAlarmFlight.setTime(System.currentTimeMillis());
                        redisTemplate.opsForValue().set(Constant.PC_NO_TASK_FLIGHT_ALARM_R + ":" + myNoTaskAlarmFlight.getFlgtId(), myNoTaskAlarmFlight);
                        SendMsg2Redis.testDingYue(stringRedisTemplate, split[1], Constant.TASKFLIGHT, Constant.PAD_BROADCAST, new MyNoTaskAlarmFlightSend(myNoTaskAlarmFlight));

                    });

        }

    }

    /**
     * 功能描述：缓存航班数据
     *
     * @param
     * @return void
     * @author zhaojiacan
     * @date 2024/5/17
     */
    @Scheduled(cron = "0 0/3 * * * ? ")
    public void cacheFlightAndTaskForPad() {
        //获取系统目前所有注册用户的机场编码
        List<String> airportCodes = staffMapper.getAirportCodes();
        airportCodes.stream().filter(StrUtil::isNotBlank).forEach(airportCode -> {
            MyNewFlight myNewFlight = new MyNewFlight();
            myNewFlight.setStaffAirportCode(airportCode);
            myNewFlight.setPageSize(1000);
            myNewFlight.setPaginate(1);
            flightService.cacheFlightAndTaskForPad(myNewFlight);
        });
    }


}
