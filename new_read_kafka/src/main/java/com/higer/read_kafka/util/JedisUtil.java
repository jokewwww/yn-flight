package com.higer.read_kafka.util;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/5/16 13:50
 * @Description:
 */
@Log4j
public class JedisUtil{

    private static JedisPool jedisPool;

    @Autowired(required = true)
    public void setJedisPool(JedisPool jedisPool) {
        JedisUtil.jedisPool = jedisPool;
    }
    /**
     * 对某个键的值自增
     * @author liboyi
     * @param key 键
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static AtomicLong setIncr(String key, AtomicInteger cacheSeconds) {
        AtomicLong result = new AtomicLong(0);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result =new AtomicLong(jedis.incr(key));
            if (cacheSeconds.get() != 0) {
                jedis.expire(key, cacheSeconds.get());
            }
            log.debug("set "+ key + " = " + result);
        } catch (Exception e) {
            log.warn("set "+ key + " = " + result);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }
    private void denialOfService(String userId){
        AtomicLong count=JedisUtil.setIncr(userId, new AtomicInteger(86400));
        System.out.println();
    }

    public static void main(String[] args) {
        JedisPool jedisPool = new JedisPool("127.0.0.1",6379);
        JedisUtil.jedisPool = jedisPool;

        for (int i = 0; i <10 ; i++) {
            if(i == 5){
                jedisPool.getResource().del("a");
            }
            AtomicLong count=JedisUtil.setIncr("a", new AtomicInteger(86400));
            System.out.println(count);
        }

    }


}
