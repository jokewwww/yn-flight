package com.zh.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Auther: 修宏鑫
 * @Date: 2020/1/23 20:00
 * @Description:
 */
@RestController
@RequestMapping(value = "/test")
public class Test {

    @GetMapping(value = "/a")
    public String test() throws ExecutionException, InterruptedException {
        System.out.println("----程序开始运行----");
        Date date1 = new Date();
        int taskSize = 5;
        // 创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(taskSize);
        // 创建多个有返回值的任务
        List<Future> list = new ArrayList<Future>();
        for (int i = 0; i < taskSize; i++) {
            Callable c = new MyCallable(i + " ");
            // 执行任务并获取Future对象
            Future f = pool.submit(c);
            // System.out.println(">>>" + f.get().toString());
            list.add(f);
        }
        // 关闭线程池
        pool.shutdown();

        // 获取所有并发任务的运行结果
        for (Future f : list) {
            // 从Future对象上获取任务的返回值，并输出到控制台
            System.out.println(">>>" + f.get().toString());
        }
        Date date2 = new Date();
        System.out.println("----程序结束运行----，程序运行时间【"
                + (date2.getTime() - date1.getTime()) + "毫秒】");

        return "success";
    }

    class MyCallable implements Callable<Object> {
        private String taskNum;

        MyCallable(String taskNum) {
            this.taskNum = taskNum;
        }

        MyCallable() {
            this.taskNum = taskNum;
        }

        public Object call() throws Exception {
            System.out.println(">>>" + taskNum + "任务启动");
            Date dateTmp1 = new Date();
            String data = "{\"remark\":\"test\",\"loginUserIn\":{\n" +
                    "\"staffAirportCode\":\"2010\"\n" +
                    "}}";
            String msg = HttpUtils.postBody("http://127.0.0.1:8081/flight/recpt/param", data, "application/json");
            System.out.println("获取结果------->" + msg);
            Thread.sleep(1000);
            Date dateTmp2 = new Date();
            long time = dateTmp2.getTime() - dateTmp1.getTime();
            System.out.println(">>>" + taskNum + "任务终止");
            return taskNum + "任务返回运行结果,当前任务时间【" + time + "毫秒】";
        }
    }
}

