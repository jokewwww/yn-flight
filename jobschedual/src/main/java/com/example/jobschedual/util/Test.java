package com.example.jobschedual.util;

import com.alibaba.fastjson.JSONObject;
import com.example.jobschedual.redis.RedisService;
import com.example.jobschedual.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class Test {

    @Autowired
    private RedisService redisService;

    @Autowired
    private TaskService taskService;

//    @Scheduled(cron = "0/15 * * * * *")
    public void test(){
        taskService.changeC7();
        Map<String, Object> all = redisService.findAll();
        System.out.println(JSONObject.toJSONString(all));
    }
}
