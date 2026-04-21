package com.zh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;

/**
 * 线程池配置
 * <p>
 * Copyright: Copyright (c) 2018 zteits
 *
 * @ClassName: ThreadPoolConfig.java
 * @Description:
 * @version: v1.0.0
 * @author:
 * @date: 2018年11月16日 15:36
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ExecutorService getThreadPool() {
        return ThreadPoolFactory.appThreadPool();
    }
}
