package com.zh.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Copyright: Copyright (c) 2021 hge
 *
 * @ClassName: WorkTime.java
 * @Description:
 * @version: v1.0.0
 * @author: nimz
 * @date: 2021年12月06日 16:04
 */
public class WorkTime implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 加油员
     */
    private String staffId;

    /**
     * 加油员
     */
    private String staffName;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务接受时间
     */
    private Date taskAccTime;

    /**
     * 任务结束时间
     */
    private Date taskDoneTime;

    /**
     * 工作时长 分钟
     */
    private Double workTime;

    /**
     * 本次加油航班
     */
    private String flno;

    /**
     * 累积加油架次
     */
    private List<Task> flnoList;

    /**
     * 累积加油时间 分钟
     */
    private Double allWorkTime;

    /**
     * 休息时长 分钟
     */
    private Double restTime;

    /**
     * 休息开始时间
     */
    private Long restStartDate;

    /**
     * 间隔
     */
    private Double intervalTime;

    private Long lastSendTime;

    /**
     * 当前状态 1：加油中；2：休息中
     */
    private Integer status;

    /**
     * 所属机场代码
     */
    private String flgtAirportCode;

    public String getFlgtAirportCode() {
        return flgtAirportCode;
    }

    public void setFlgtAirportCode(String flgtAirportCode) {
        this.flgtAirportCode = flgtAirportCode;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Long getRestStartDate() {
        return restStartDate;
    }

    public void setRestStartDate(Long restStartDate) {
        this.restStartDate = restStartDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getLastSendTime() {
        return lastSendTime;
    }

    public void setLastSendTime(Long lastSendTime) {
        this.lastSendTime = lastSendTime;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
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

    public Double getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Double workTime) {
        this.workTime = workTime;
    }

    public String getFlno() {
        return flno;
    }

    public void setFlno(String flno) {
        this.flno = flno;
    }

    public List<Task> getFlnoList() {
        return flnoList;
    }

    public void setFlnoList(List<Task> flnoList) {
        this.flnoList = flnoList;
    }

    public Double getAllWorkTime() {
        return allWorkTime;
    }

    public void setAllWorkTime(Double allWorkTime) {
        this.allWorkTime = allWorkTime;
    }

    public Double getRestTime() {
        return restTime;
    }

    public void setRestTime(Double restTime) {
        this.restTime = restTime;
    }

    public Double getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Double intervalTime) {
        this.intervalTime = intervalTime;
    }

    @Override
    public String toString() {
        return "WorkTime{" +
                "staffId='" + staffId + '\'' +
                ", staffName='" + staffName + '\'' +
                ", taskId='" + taskId + '\'' +
                ", taskAccTime=" + taskAccTime +
                ", taskDoneTime=" + taskDoneTime +
                ", workTime=" + workTime +
                ", flno='" + flno + '\'' +
                ", flnoList=" + flnoList +
                ", allWorkTime=" + allWorkTime +
                ", restTime=" + restTime +
                ", restStartDate=" + restStartDate +
                ", intervalTime=" + intervalTime +
                ", lastSendTime=" + lastSendTime +
                ", status=" + status +
                ", flgtAirportCode=" + flgtAirportCode +
                '}';
    }
}
