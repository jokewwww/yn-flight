package com.zh.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

    /**
     * 初始注入scheduler
     *
     * @return
     * @throws SchedulerException
     */
    @Bean
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws SchedulerException {
        return schedulerFactoryBean.getScheduler();
    }


    @Bean
    public MyJobFactory jobFactory() {
        return new MyJobFactory();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(MyJobFactory jobFactory) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(jobFactory);
        return schedulerFactoryBean;
    }
}
