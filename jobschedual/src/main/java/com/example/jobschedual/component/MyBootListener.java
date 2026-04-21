package com.example.jobschedual.component;

import com.example.jobschedual.redis.RedisService;
import com.example.jobschedual.service.WorkStaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/11/6 20:39
 * @Description:
 */
@Component
public class MyBootListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(MyBootListener.class);

    @Value("${system_redis_start_date}")
    private String startDate;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        WorkStaffService bean = applicationContext.getBean(WorkStaffService.class);
        RedisService redisService = applicationContext.getBean(RedisService.class);
        //更改redis 初始化时间
        redisService.setStr("queueintit",startDate);
        bean.init();
    }

}
