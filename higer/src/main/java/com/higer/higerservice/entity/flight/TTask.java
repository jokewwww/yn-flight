package com.higer.higerservice.entity.flight;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "T_TASK")
@Data
public class TTask {

    /**
     * 任务ID
     */
    @Id
    @Column(name = "task_id")
    private String taskId;

    @Column(name = "task_takeoff_fuel")
    private Integer taskTakeoffFuel;

    @Column(name = "task_chock_fuel")
    private Integer taskChockFuel;

    @Column(name = "task_status")
    private Integer taskStatus;

    @Column(name = "task_rc_print_time")
    private Date taskRcPrintTime;

    @Column(name = "task_fuel_recpt_no")
    private String taskFuelRecptNo;

    @Column(name = "task_done_time")
    private Date taskDoneTime;

    @Column(name = "task_chag_end_time")
    private Date taskChagEndTime;

    @Column(name = "task_chag_sta_time")
    private Date taskChagStaTime;

    @Column(name = "task_arrive_time")
    private Date taskArriveTime;
}
