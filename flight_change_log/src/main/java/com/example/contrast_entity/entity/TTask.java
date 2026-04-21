package com.example.contrast_entity.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/3/12 15:42
 * @Description:
 */
@Entity
@Table(name = "T_TASK", schema = "zhoil_flight", catalog = "")
public class TTask {
    private String taskId;
    private String taskFlightId;
    private String taskFlightNo;
    private String taskAirportCode;
    private String taskAptareaCode;
    private String taskOpeStaffId;
    private int taskContent;
    private Integer taskStatus;
    private Timestamp taskAsgTime;
    private Timestamp taskAccTime;
    private Timestamp taskChagStaTime;
    private Timestamp taskArriveTime;
    private Timestamp taskRcPrintTime;
    private Timestamp taskChagEndTime;
    private Timestamp taskDoneTime;
    private String taskFuelRecptNo;
    private String taskVehiNo;
    private String taskCreStaffId;
    private Integer taskStarmark;
    private Timestamp taskRecCreTime;
    private Integer taskTakeoffFuel;
    private Integer taskChockFuel;
    private Integer taskTotalFuel;
    private String taskMeterType;
    private Integer taskCentTank;
    private Integer taskLeftTank;
    private Integer taskRightTank;
    private String taskCrewSign;

    @Id
    @Column(name = "task_id", nullable = false, length = 40)
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Basic
    @Column(name = "task_flight_id", nullable = true, length = 40)
    public String getTaskFlightId() {
        return taskFlightId;
    }

    public void setTaskFlightId(String taskFlightId) {
        this.taskFlightId = taskFlightId;
    }

    @Basic
    @Column(name = "task_flight_no", nullable = true, length = 100)
    public String getTaskFlightNo() {
        return taskFlightNo;
    }

    public void setTaskFlightNo(String taskFlightNo) {
        this.taskFlightNo = taskFlightNo;
    }

    @Basic
    @Column(name = "task_airport_code", nullable = true, length = 4)
    public String getTaskAirportCode() {
        return taskAirportCode;
    }

    public void setTaskAirportCode(String taskAirportCode) {
        this.taskAirportCode = taskAirportCode;
    }

    @Basic
    @Column(name = "task_aptarea_code", nullable = true, length = 7)
    public String getTaskAptareaCode() {
        return taskAptareaCode;
    }

    public void setTaskAptareaCode(String taskAptareaCode) {
        this.taskAptareaCode = taskAptareaCode;
    }

    @Basic
    @Column(name = "task_ope_staff_id", nullable = true, length = 20)
    public String getTaskOpeStaffId() {
        return taskOpeStaffId;
    }

    public void setTaskOpeStaffId(String taskOpeStaffId) {
        this.taskOpeStaffId = taskOpeStaffId;
    }

    @Basic
    @Column(name = "task_content", nullable = false)
    public int getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(int taskContent) {
        this.taskContent = taskContent;
    }

    @Basic
    @Column(name = "task_status", nullable = true)
    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Basic
    @Column(name = "task_asg_time", nullable = true)
    public Timestamp getTaskAsgTime() {
        return taskAsgTime;
    }

    public void setTaskAsgTime(Timestamp taskAsgTime) {
        this.taskAsgTime = taskAsgTime;
    }

    @Basic
    @Column(name = "task_acc_time", nullable = true)
    public Timestamp getTaskAccTime() {
        return taskAccTime;
    }

    public void setTaskAccTime(Timestamp taskAccTime) {
        this.taskAccTime = taskAccTime;
    }

    @Basic
    @Column(name = "task_chag_sta_time", nullable = true)
    public Timestamp getTaskChagStaTime() {
        return taskChagStaTime;
    }

    public void setTaskChagStaTime(Timestamp taskChagStaTime) {
        this.taskChagStaTime = taskChagStaTime;
    }

    @Basic
    @Column(name = "task_arrive_time", nullable = true)
    public Timestamp getTaskArriveTime() {
        return taskArriveTime;
    }

    public void setTaskArriveTime(Timestamp taskArriveTime) {
        this.taskArriveTime = taskArriveTime;
    }

    @Basic
    @Column(name = "task_rc_print_time", nullable = true)
    public Timestamp getTaskRcPrintTime() {
        return taskRcPrintTime;
    }

    public void setTaskRcPrintTime(Timestamp taskRcPrintTime) {
        this.taskRcPrintTime = taskRcPrintTime;
    }

    @Basic
    @Column(name = "task_chag_end_time", nullable = true)
    public Timestamp getTaskChagEndTime() {
        return taskChagEndTime;
    }

    public void setTaskChagEndTime(Timestamp taskChagEndTime) {
        this.taskChagEndTime = taskChagEndTime;
    }

    @Basic
    @Column(name = "task_done_time", nullable = true)
    public Timestamp getTaskDoneTime() {
        return taskDoneTime;
    }

    public void setTaskDoneTime(Timestamp taskDoneTime) {
        this.taskDoneTime = taskDoneTime;
    }

    @Basic
    @Column(name = "task_fuel_recpt_no", nullable = true, length = 13)
    public String getTaskFuelRecptNo() {
        return taskFuelRecptNo;
    }

    public void setTaskFuelRecptNo(String taskFuelRecptNo) {
        this.taskFuelRecptNo = taskFuelRecptNo;
    }

    @Basic
    @Column(name = "task_vehi_no", nullable = true, length = 10)
    public String getTaskVehiNo() {
        return taskVehiNo;
    }

    public void setTaskVehiNo(String taskVehiNo) {
        this.taskVehiNo = taskVehiNo;
    }

    @Basic
    @Column(name = "task_cre_staff_id", nullable = true, length = 20)
    public String getTaskCreStaffId() {
        return taskCreStaffId;
    }

    public void setTaskCreStaffId(String taskCreStaffId) {
        this.taskCreStaffId = taskCreStaffId;
    }

    @Basic
    @Column(name = "task_starmark", nullable = true)
    public Integer getTaskStarmark() {
        return taskStarmark;
    }

    public void setTaskStarmark(Integer taskStarmark) {
        this.taskStarmark = taskStarmark;
    }

    @Basic
    @Column(name = "task_rec_cre_time", nullable = true)
    public Timestamp getTaskRecCreTime() {
        return taskRecCreTime;
    }

    public void setTaskRecCreTime(Timestamp taskRecCreTime) {
        this.taskRecCreTime = taskRecCreTime;
    }

    @Basic
    @Column(name = "task_takeoff_fuel", nullable = true)
    public Integer getTaskTakeoffFuel() {
        return taskTakeoffFuel;
    }

    public void setTaskTakeoffFuel(Integer taskTakeoffFuel) {
        this.taskTakeoffFuel = taskTakeoffFuel;
    }

    @Basic
    @Column(name = "task_chock_fuel", nullable = true)
    public Integer getTaskChockFuel() {
        return taskChockFuel;
    }

    public void setTaskChockFuel(Integer taskChockFuel) {
        this.taskChockFuel = taskChockFuel;
    }

    @Basic
    @Column(name = "task_total_fuel", nullable = true)
    public Integer getTaskTotalFuel() {
        return taskTotalFuel;
    }

    public void setTaskTotalFuel(Integer taskTotalFuel) {
        this.taskTotalFuel = taskTotalFuel;
    }

    @Basic
    @Column(name = "task_meter_type", nullable = true, length = 2)
    public String getTaskMeterType() {
        return taskMeterType;
    }

    public void setTaskMeterType(String taskMeterType) {
        this.taskMeterType = taskMeterType;
    }

    @Basic
    @Column(name = "task_cent_tank", nullable = true)
    public Integer getTaskCentTank() {
        return taskCentTank;
    }

    public void setTaskCentTank(Integer taskCentTank) {
        this.taskCentTank = taskCentTank;
    }

    @Basic
    @Column(name = "task_left_tank", nullable = true)
    public Integer getTaskLeftTank() {
        return taskLeftTank;
    }

    public void setTaskLeftTank(Integer taskLeftTank) {
        this.taskLeftTank = taskLeftTank;
    }

    @Basic
    @Column(name = "task_right_tank", nullable = true)
    public Integer getTaskRightTank() {
        return taskRightTank;
    }

    public void setTaskRightTank(Integer taskRightTank) {
        this.taskRightTank = taskRightTank;
    }

    @Basic
    @Column(name = "task_crew_sign", nullable = true, length = 9999)
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
                Objects.equals(taskRcPrintTime, tTask.taskRcPrintTime) &&
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

        return Objects.hash(taskId, taskFlightId, taskFlightNo, taskAirportCode, taskAptareaCode, taskOpeStaffId, taskContent, taskStatus, taskAsgTime, taskAccTime, taskChagStaTime, taskArriveTime, taskRcPrintTime, taskChagEndTime, taskDoneTime, taskFuelRecptNo, taskVehiNo, taskCreStaffId, taskStarmark, taskRecCreTime, taskTakeoffFuel, taskChockFuel, taskTotalFuel, taskMeterType, taskCentTank, taskLeftTank, taskRightTank, taskCrewSign);
    }
}
