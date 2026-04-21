package com.higer.higerservice.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Reids服务类
 *
 * @author lhzssw
 */
@Service
public class RedisService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    ValueOperations<Object, Object> valOpsObj;

    /**
     * 根据指定key获取String
     *
     * @param key
     * @return
     */
    public String getStr(String key) {
        return valOpsStr.get(key);
    }

    /**
     * 设置Str缓存
     *
     * @param key
     * @param val
     */
    public void setStr(String key, String val) {
        valOpsStr.set(key, val);
    }

    /**
     * 设置Str缓存(非永久储存,可设置缓存时间)
     *
     * @param key
     * @param val
     * @param time
     */
    public void setStrToTime(String key, String val, Long time) {
        valOpsStr.set(key, val, time, TimeUnit.SECONDS);
    }

    /**
     * 删除指定key
     *
     * @param key
     */
    public void del(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 根据指定o获取Object
     *
     * @param o
     * @return
     */
    public Object getObj(Object o) {
        return valOpsObj.get(o);
    }

    /**
     * 设置obj缓存
     *
     * @param o1
     * @param o2
     */
    public void setObj(Object o1, Object o2) {
        valOpsObj.set(o1, o2);
    }

    /**
     * 设置obj缓存(非永久储存,可设置缓存时间)
     *
     * @param o1
     * @param o2
     * @param time
     */
    public void setObjToTime(Object o1, Object o2, Long time) {
        valOpsObj.set(o1, o2, time, TimeUnit.SECONDS);
    }

    /**
     * 删除Obj缓存
     *
     * @param o
     */
    public void delObj(Object o) {
        redisTemplate.delete(o);
    }
}
