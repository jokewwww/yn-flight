package com.higer.statistical.entity.flight;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "T_TASK")
@Data
public class TTask{


	/**
     * 任务ID
     */
	@Id
    @Column(name="task_id")
    private String taskId;

	@Column(name="task_takeoff_fuel")
	private Integer taskTakeoffFuel;

	@Column(name = "task_chock_fuel")
	private Integer taskChockFuel;
}
