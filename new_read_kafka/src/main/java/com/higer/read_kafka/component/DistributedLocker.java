package com.higer.read_kafka.component;

import java.util.concurrent.TimeUnit;
import org.redisson.api.RLock;

/**
 * @Auther: 修宏鑫
 * @Date: 2020/4/7 15:20
 * @Description:
 */
public interface DistributedLocker {
    RLock lock(String lockKey);

    RLock lock(String lockKey, long timeout);

    RLock lock(String lockKey, TimeUnit unit, long timeout);

    boolean tryLock(String lockKey);

    boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime);

    void unlock(String lockKey);

    void unlock(RLock lock);

}
