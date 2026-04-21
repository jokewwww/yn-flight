package com.example.jobschedual.factory;


import com.example.jobschedual.entity.QueueEntity;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/3/13 19:32
 * @Description:
 */
public class QueueInterface {

    public static Map<String, Object> maps = new ConcurrentHashMap<String, Object>();

    public static LinkedBlockingQueue<Object> getQueue(String type) {
        LinkedBlockingQueue<Object> queueEntities = new LinkedBlockingQueue<>();
        if (!StringUtils.isEmpty(type)) {
            maps.put(type, queueEntities);
        }
        return queueEntities;
    }

    synchronized public static Object getMapQueue(String type) {
        try {
            if (StringUtils.isEmpty(type)) {
                return null;
            }
            Object object = maps.get(type);
            if (null != object) {
                return object;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    synchronized public static AtomicBoolean setMapQueue(String type, QueueEntity queueEntity) {
        try {
            if (StringUtils.isEmpty(type) || queueEntity == null) {
                return new AtomicBoolean(false);
            }
            maps.put(type, queueEntity);
            return new AtomicBoolean(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AtomicBoolean(false);
    }

}
