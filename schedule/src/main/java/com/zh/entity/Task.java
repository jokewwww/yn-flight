package com.zh.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Copyright: Copyright (c) 2021 hge
 *
 * @ClassName: WorkTime.java
 * @Description:
 * @version: v1.0.0
 * @author: nimz
 * @date: 2021年12月06日 16:04
 */
public class Task implements Serializable {

    /**
     * 任务接受时间
     */
    private Date taskAccTime;

    /**
     * 任务结束时间
     */
    private Date taskDoneTime;

    /**
     * 本次加油航班
     */
    private String flno;

    /**
     * 当前状态 1：加油中；2：休息中
     */
    private Integer status;

    /**
     * 任务id
     */
    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Date getTaskAccTime() {
        return taskAccTime;
    }

    public void setTaskAccTime(Date taskAccTime) {
        this.taskAccTime = taskAccTime;
    }

    public Date getTaskDoneTime() {
        return taskDoneTime;
    }

    public void setTaskDoneTime(Date taskDoneTime) {
        this.taskDoneTime = taskDoneTime;
    }

    public String getFlno() {
        return flno;
    }

    public void setFlno(String flno) {
        this.flno = flno;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
