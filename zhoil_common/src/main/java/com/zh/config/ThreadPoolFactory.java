package com.zh.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * 线程池配置
 * <p>
 * Copyright: Copyright (c) 2018 zteits
 *
 * @ClassName: ThreadPoolFactory.java
 * @Description:
 * @version: v1.0.0
 * @author:
 * @date: 2018年11月16日 15:41
 */
public class ThreadPoolFactory {

    /**
     * APP线程池
     *
     * @return
     */
    public static ExecutorService appThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("app-pool-%d").build();

        // 线程池配置10个核心线程
        // 线程队列长度1024
        // 其余线程在队列中排队
        // 如果队列满了，支持最大100个线程

        return new ThreadPoolExecutor(10, 100,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

}
