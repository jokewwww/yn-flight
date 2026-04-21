package com.zh.thread;

import com.zh.constant.Constant;
import com.zh.entity.WorkTime;
import com.zh.entity.WorkTimeSend;
import com.zh.util.SendMsg2Redis;
import org.springframework.data.redis.core.StringRedisTemplate;

public class SysThread implements Runnable {

    private StringRedisTemplate stringRedisTemplate;

    private WorkTime workTime;

    public SysThread(StringRedisTemplate stringRedisTemplate, WorkTime workTime) {
        super();
        this.stringRedisTemplate = stringRedisTemplate;
        this.workTime = workTime;
    }

    @Override
    public void run() {
        // 保存系统日志
        SendMsg2Redis.testDingYueToAllType(stringRedisTemplate, Constant.STAFF, Constant.PC_WORK_TIME, new WorkTimeSend(workTime));
    }
}
