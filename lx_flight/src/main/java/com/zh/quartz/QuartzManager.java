package com.zh.quartz;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class QuartzManager {

    private final static Logger log = LoggerFactory.getLogger(QuartzManager.class);


    private static String JOB_GROUP_NAME = "JOB_DEFAULT_GROUP_NAME";
    private static String TRIGGER_GROUP_NAME = "TRIGGER_DEFAULT_GROUP_NAME";

    @Autowired
    private Scheduler scheduler;

    /**
     * @param jobName 任务名
     * @param cls     任务
     * @Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
     */
    public void addJob(String jobName, Class<? extends Job> cls, String cron) {
        addJob(jobName, JOB_GROUP_NAME, jobName, TRIGGER_GROUP_NAME, cls, cron);
    }

    /**
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass         任务
     * @Description: 添加一个定时任务
     */
    public void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
                       Class<? extends Job> jobClass, String cron) {
        try {
            log.info("Add CronSchedule At {}", cron);
            JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            // 按新的cronExpression表达式构建一个新的trigger
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName)
                    .withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(job, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addSimpleTriggerJob(String jobName, Class<? extends Job> cls, Date date, Integer count, JobDataMap dataMap) {
        addSimpleTriggerJob(jobName, JOB_GROUP_NAME, jobName, TRIGGER_GROUP_NAME, cls, date, count, dataMap);
    }

    public void addSimpleTriggerJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class<? extends Job> cls, Date date, Integer count, JobDataMap dataMap) {
        try {
            log.info("Add SimpleSchedule At {}", DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss"));
            JobBuilder jobBuilder = JobBuilder.newJob(cls).withIdentity(jobName, jobGroupName);
            if (dataMap != null) {
                jobBuilder.setJobData(dataMap);
            }
            JobDetail job = jobBuilder.build();
            // 表达式调度构建器
            //MisFire策略：withMisfireHandlingInstructionFireNow（失效之后再恢复并马上执行）
            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                    .withMisfireHandlingInstructionFireNow()
                    .withIntervalInSeconds(30);
            if (count == null) {
                scheduleBuilder.repeatForever();
            } else {
                scheduleBuilder.withRepeatCount(count);
            }
            // 按新的cronExpression表达式构建一个新的trigger
            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(triggerName, triggerGroupName)
                    .withSchedule(scheduleBuilder)
                    .startAt(date)
                    .build();
            scheduler.scheduleJob(job, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param jobName
     * @Description: 修改一个任务的触发时间(使用默认的任务组名 ， 触发器名 ， 触发器组名)
     */
    public void modifyJobTime(String jobName, String cron) {
        modifyJobTime(jobName, TRIGGER_GROUP_NAME, cron);
    }


    /**
     * @param oldJobName ：原任务名
     * @param jobName
     * @param jobclass
     * @param cron
     * @Description:修改任务，（可以修改任务名，任务类，触发时间） 原理：移除原来的任务，添加新的任务
     * @date 2018年5月23日 上午9:13:10
     */
    public void modifyJob(String oldJobName, String jobName, Class<? extends Job> jobclass, String cron) {
        /*
         * removeJob(oldJobName);
         * addJob(jobName, jobclass, cron);
         * System.err.println("修改任务"+oldJobName);
         */
        TriggerKey triggerKey = TriggerKey.triggerKey(oldJobName, TRIGGER_GROUP_NAME);
        JobKey jobKey = JobKey.jobKey(oldJobName, JOB_GROUP_NAME);
        try {
            Trigger trigger = scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            scheduler.pauseTrigger(triggerKey);// 停止触发器
            scheduler.unscheduleJob(triggerKey);// 移除触发器
            scheduler.deleteJob(jobKey);// 删除任务
            System.out.println("移除任务:" + oldJobName);

            JobDetail job = JobBuilder.newJob(jobclass).withIdentity(jobName, JOB_GROUP_NAME).build();
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            // 按新的cronExpression表达式构建一个新的trigger
            Trigger newTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, TRIGGER_GROUP_NAME)
                    .withSchedule(scheduleBuilder).build();
            // 交给scheduler去调度
            scheduler.scheduleJob(job, newTrigger);

            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
                System.out.println("添加新任务:" + jobName);
            }
            System.out.println("修改任务【" + oldJobName + "】为:" + jobName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void modifySimpleTriggerJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class<? extends Job> cls, Date date, Integer count, JobDataMap dataMap) {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        try {
            Trigger trigger = scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            scheduler.pauseTrigger(triggerKey);// 停止触发器
            scheduler.unscheduleJob(triggerKey);// 移除触发器
            scheduler.deleteJob(jobKey);// 删除任务
            System.out.println("移除任务:" + jobName);

            JobBuilder jobBuilder = JobBuilder.newJob(cls).withIdentity(jobName, jobGroupName);
            if (dataMap != null) {
                jobBuilder.setJobData(dataMap);
            }
            JobDetail job = jobBuilder.build();
            // 表达式调度构建器
            //MisFire策略：withMisfireHandlingInstructionFireNow（失效之后再恢复并马上执行）
            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                    .withMisfireHandlingInstructionFireNow()
                    .withIntervalInSeconds(30);
            if (count == null) {
                scheduleBuilder.repeatForever();
            } else {
                scheduleBuilder.withRepeatCount(count);
            }
            // 按新的cronExpression表达式构建一个新的trigger
            trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(triggerName, triggerGroupName)
                    .withSchedule(scheduleBuilder)
                    .startAt(date)
                    .build();
            scheduler.scheduleJob(job, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
            System.out.println("修改任务【" + jobName + "】到:" + DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void modifySimpleTriggerJob(String jobName, Class<? extends Job> cls, Date date, Integer count, JobDataMap dataMap) {
        modifySimpleTriggerJob(jobName, JOB_GROUP_NAME, jobName, TRIGGER_GROUP_NAME, cls, date, count, dataMap);
    }

    /**
     * @param triggerName
     * @param triggerGroupName
     * @Description: 修改一个任务的触发时间
     */
    public void modifyJobTime(String triggerName, String triggerGroupName, String cron) {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
        try {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                // trigger已存在，则更新相应的定时设置
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                // 按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                // 按新的trigger重新设置job执行
                scheduler.resumeTrigger(triggerKey);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param jobName
     * @Description 移除一个任务(使用默认的任务组名 ， 触发器名 ， 触发器组名)
     */
    public void removeJob(String jobName) {
        removeJob(jobName, JOB_GROUP_NAME, jobName, TRIGGER_GROUP_NAME);
    }

    /**
     * 移除所有触发器
     */
    public void removeAllJob() {
        try {
            scheduler.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     * @Description: 移除一个任务
     */
    public void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        System.out.println("移除任务【" + jobName + "】");
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        try {
            scheduler.pauseTrigger(triggerKey);// 停止触发器
            scheduler.unscheduleJob(triggerKey);// 移除触发器
            scheduler.deleteJob(jobKey);// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param jobName
     * @Description:暂停一个任务(使用默认组名)
     */
    public void pauseJob(String jobName) {
        pauseJob(jobName, JOB_GROUP_NAME);
    }

    /**
     * @param jobName
     * @param jobGroupName
     * @Description:暂停一个任务
     */
    public void pauseJob(String jobName, String jobGroupName) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param jobName
     * @Description:恢复一个任务(使用默认组名)
     */
    public void resumeJob(String jobName) {
        resumeJob(jobName, JOB_GROUP_NAME);
    }

    /**
     * @param jobName
     * @param jobGroupName
     * @Description:恢复一个任务
     * @date 2018年5月17日 上午9:56:09
     */
    public void resumeJob(String jobName, String jobGroupName) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description:启动所有定时任务
     */
    public void startJobs() {
        try {
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description 关闭所有定时任务
     */
    public void shutdownJobs() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param jobName
     * @Description: 立即运行任务，这里的立即运行，只会运行一次，方便测试时用。
     * @date 2018年5月17日 上午10:03:26
     */
    public void triggerJob(String jobName) {
        JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_NAME);
        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param jobName
     * @param jobGroupName
     * @Description: 立即运行任务，这里的立即运行，只会运行一次，方便测试时用。
     * @date 2018年5月17日 上午10:03:26
     */
    public void triggerJob(String jobName, String jobGroupName) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param jobName 触发器名
     * @Description: 获取任务状态
     * NONE: 不存在
     * NORMAL: 正常
     * PAUSED: 暂停
     * COMPLETE:完成
     * ERROR : 错误
     * BLOCKED : 阻塞
     * @date 2018年5月21日 下午2:13:45
     */
    public String getTriggerState(String jobName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME);
        String name = null;
        try {
            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
            name = triggerState.name();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * @param cron
     * @Description:获取最近8次执行时间
     * @date 2018年5月24日 下午5:13:03
     */
    public List<String> getRecentTriggerTime(String cron) {
        List<String> list = new ArrayList<String>();
        try {
            CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
            cronTriggerImpl.setCronExpression(cron);
            // 这个是重点，一行代码搞定
            List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, 8);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Date date : dates) {
                list.add(dateFormat.format(date));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }


    public boolean exist(String jobName, String jobGroupName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
        try {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            return trigger != null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean exist(String jobName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME);
        try {
            Trigger trigger = scheduler.getTrigger(triggerKey);
            return trigger != null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
