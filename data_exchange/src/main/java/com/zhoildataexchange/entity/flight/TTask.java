package com.zhoildataexchange.entity.flight;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/2/18 10:26
 * @Description:
 */
@Entity
@Table(name = "T_TASK")
@NamedQuery(name = "TTask.findAll", query = "SELECT a FROM TTask a")
public class TTask {
    private String taskId;
    private String taskFlightId;
    private String taskFlightNo;
    private String taskAirportCode;
    private String taskAptareaCode;
    private String taskOpeStaffId;
    private int taskContent;
    private Integer taskStatus;
    private Date taskAsgTime;
    private Date taskAccTime;
    private Date taskChagStaTime;
    private Date taskArriveTime;
    private Date taskChagEndTime;
    private Date taskDoneTime;
    private String taskFuelRecptNo;
    private String taskVehiNo;
    private String taskCreStaffId;
    private Integer taskStarmark;
    private Date taskRecCreTime;
    private Integer taskTakeoffFuel;
    private Integer taskChockFuel;
    private Integer taskTotalFuel;
    private String taskMeterType;
    private Integer taskCentTank;
    private Integer taskLeftTank;
    private Integer taskRightTank;
    private String taskCrewSign;

    @Id
    @Column(name = "task_id")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Basic
    @Column(name = "task_flight_id")
    public String getTaskFlightId() {
        return taskFlightId;
    }

    public void setTaskFlightId(String taskFlightId) {
        this.taskFlightId = taskFlightId;
    }

    @Basic
    @Column(name = "task_flight_no")
    public String getTaskFlightNo() {
        return taskFlightNo;
    }

    public void setTaskFlightNo(String taskFlightNo) {
        this.taskFlightNo = taskFlightNo;
    }

    @Basic
    @Column(name = "task_airport_code")
    public String getTaskAirportCode() {
        return taskAirportCode;
    }

    public void setTaskAirportCode(String taskAirportCode) {
        this.taskAirportCode = taskAirportCode;
    }

    @Basic
    @Column(name = "task_aptarea_code")
    public String getTaskAptareaCode() {
        return taskAptareaCode;
    }

    public void setTaskAptareaCode(String taskAptareaCode) {
        this.taskAptareaCode = taskAptareaCode;
    }

    @Basic
    @Column(name = "task_ope_staff_id")
    public String getTaskOpeStaffId() {
        return taskOpeStaffId;
    }

    public void setTaskOpeStaffId(String taskOpeStaffId) {
        this.taskOpeStaffId = taskOpeStaffId;
    }

    @Basic
    @Column(name = "task_content")
    public int getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(int taskContent) {
        this.taskContent = taskContent;
    }

    @Basic
    @Column(name = "task_status")
    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Basic
    @Column(name = "task_asg_time")
    public Date getTaskAsgTime() {
        return taskAsgTime;
    }

    public void setTaskAsgTime(Date taskAsgTime) {
        this.taskAsgTime = taskAsgTime;
    }

    @Basic
    @Column(name = "task_acc_time")
    public Date getTaskAccTime() {
        return taskAccTime;
    }

    public void setTaskAccTime(Date taskAccTime) {
        this.taskAccTime = taskAccTime;
    }

    @Basic
    @Column(name = "task_chag_sta_time")
    public Date getTaskChagStaTime() {
        return taskChagStaTime;
    }

    public void setTaskChagStaTime(Date taskChagStaTime) {
        this.taskChagStaTime = taskChagStaTime;
    }

    @Basic
    @Column(name = "task_arrive_time")
    public Date getTaskArriveTime() {
        return taskArriveTime;
    }

    public void setTaskArriveTime(Date taskArriveTime) {
        this.taskArriveTime = taskArriveTime;
    }

    @Basic
    @Column(name = "task_chag_end_time")
    public Date getTaskChagEndTime() {
        return taskChagEndTime;
    }

    public void setTaskChagEndTime(Date taskChagEndTime) {
        this.taskChagEndTime = taskChagEndTime;
    }

    @Basic
    @Column(name = "task_done_time")
    public Date getTaskDoneTime() {
        return taskDoneTime;
    }

    public void setTaskDoneTime(Date taskDoneTime) {
        this.taskDoneTime = taskDoneTime;
    }

    @Basic
    @Column(name = "task_fuel_recpt_no")
    public String getTaskFuelRecptNo() {
        return taskFuelRecptNo;
    }

    public void setTaskFuelRecptNo(String taskFuelRecptNo) {
        this.taskFuelRecptNo = taskFuelRecptNo;
    }

    @Basic
    @Column(name = "task_vehi_no")
    public String getTaskVehiNo() {
        return taskVehiNo;
    }

    public void setTaskVehiNo(String taskVehiNo) {
        this.taskVehiNo = taskVehiNo;
    }

    @Basic
    @Column(name = "task_cre_staff_id")
    public String getTaskCreStaffId() {
        return taskCreStaffId;
    }

    public void setTaskCreStaffId(String taskCreStaffId) {
        this.taskCreStaffId = taskCreStaffId;
    }

    @Basic
    @Column(name = "task_starmark")
    public Integer getTaskStarmark() {
        return taskStarmark;
    }

    public void setTaskStarmark(Integer taskStarmark) {
        this.taskStarmark = taskStarmark;
    }

    @Basic
    @Column(name = "task_rec_cre_time")
    public Date getTaskRecCreTime() {
        return taskRecCreTime;
    }

    public void setTaskRecCreTime(Date taskRecCreTime) {
        this.taskRecCreTime = taskRecCreTime;
    }

    @Basic
    @Column(name = "task_takeoff_fuel")
    public Integer getTaskTakeoffFuel() {
        return taskTakeoffFuel;
    }

    public void setTaskTakeoffFuel(Integer taskTakeoffFuel) {
        this.taskTakeoffFuel = taskTakeoffFuel;
    }

    @Basic
    @Column(name = "task_chock_fuel")
    public Integer getTaskChockFuel() {
        return taskChockFuel;
    }

    public void setTaskChockFuel(Integer taskChockFuel) {
        this.taskChockFuel = taskChockFuel;
    }

    @Basic
    @Column(name = "task_total_fuel")
    public Integer getTaskTotalFuel() {
        return taskTotalFuel;
    }

    public void setTaskTotalFuel(Integer taskTotalFuel) {
        this.taskTotalFuel = taskTotalFuel;
    }

    @Basic
    @Column(name = "task_meter_type")
    public String getTaskMeterType() {
        return taskMeterType;
    }

    public void setTaskMeterType(String taskMeterType) {
        this.taskMeterType = taskMeterType;
    }

    @Basic
    @Column(name = "task_cent_tank")
    public Integer getTaskCentTank() {
        return taskCentTank;
    }

    public void setTaskCentTank(Integer taskCentTank) {
        this.taskCentTank = taskCentTank;
    }

    @Basic
    @Column(name = "task_left_tank")
    public Integer getTaskLeftTank() {
        return taskLeftTank;
    }

    public void setTaskLeftTank(Integer taskLeftTank) {
        this.taskLeftTank = taskLeftTank;
    }

    @Basic
    @Column(name = "task_right_tank")
    public Integer getTaskRightTank() {
        return taskRightTank;
    }

    public void setTaskRightTank(Integer taskRightTank) {
        this.taskRightTank = taskRightTank;
    }

    @Basic
    @Column(name = "task_crew_sign")
    public String getTaskCrewSign() {
        return taskCrewSign;
    }

    public void setTaskCrewSign(String taskCrewSign) {
        this.taskCrewSign = taskCrewSign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TTask tTask = (TTask) o;
        return taskContent == tTask.taskContent &&
                Objects.equals(taskId, tTask.taskId) &&
                Objects.equals(taskFlightId, tTask.taskFlightId) &&
                Objects.equals(taskFlightNo, tTask.taskFlightNo) &&
                Objects.equals(taskAirportCode, tTask.taskAirportCode) &&
                Objects.equals(taskAptareaCode, tTask.taskAptareaCode) &&
                Objects.equals(taskOpeStaffId, tTask.taskOpeStaffId) &&
                Objects.equals(taskStatus, tTask.taskStatus) &&
                Objects.equals(taskAsgTime, tTask.taskAsgTime) &&
                Objects.equals(taskAccTime, tTask.taskAccTime) &&
                Objects.equals(taskChagStaTime, tTask.taskChagStaTime) &&
                Objects.equals(taskArriveTime, tTask.taskArriveTime) &&
                Objects.equals(taskChagEndTime, tTask.taskChagEndTime) &&
                Objects.equals(taskDoneTime, tTask.taskDoneTime) &&
                Objects.equals(taskFuelRecptNo, tTask.taskFuelRecptNo) &&
                Objects.equals(taskVehiNo, tTask.taskVehiNo) &&
                Objects.equals(taskCreStaffId, tTask.taskCreStaffId) &&
                Objects.equals(taskStarmark, tTask.taskStarmark) &&
                Objects.equals(taskRecCreTime, tTask.taskRecCreTime) &&
                Objects.equals(taskTakeoffFuel, tTask.taskTakeoffFuel) &&
                Objects.equals(taskChockFuel, tTask.taskChockFuel) &&
                Objects.equals(taskTotalFuel, tTask.taskTotalFuel) &&
                Objects.equals(taskMeterType, tTask.taskMeterType) &&
                Objects.equals(taskCentTank, tTask.taskCentTank) &&
                Objects.equals(taskLeftTank, tTask.taskLeftTank) &&
                Objects.equals(taskRightTank, tTask.taskRightTank) &&
                Objects.equals(taskCrewSign, tTask.taskCrewSign);
    }

    @Override
    public int hashCode() {

        return Objects.hash(taskId, taskFlightId, taskFlightNo, taskAirportCode, taskAptareaCode, taskOpeStaffId, taskContent, taskStatus, taskAsgTime, taskAccTime, taskChagStaTime, taskArriveTime, taskChagEndTime, taskDoneTime, taskFuelRecptNo, taskVehiNo, taskCreStaffId, taskStarmark, taskRecCreTime, taskTakeoffFuel, taskChockFuel, taskTotalFuel, taskMeterType, taskCentTank, taskLeftTank, taskRightTank, taskCrewSign);
    }
}
