package cn.iwen.duty.entity;

import javax.persistence.*;
import cn.iwen.frame.BaseUtils;
@Entity
@Table(name="T_TASK")
public class Task {
	
	/**
	*任务ID（uuid）
	*/
	@Id
	@Column(name = "task_id",nullable=false)
	private String taskId;

	/**
	*航班ID
	*/
	@Column(name = "task_flight_id")
	private String taskFlightId;

	/**
	*航班号
	*/
	@Column(name = "task_flight_no")
	private String taskFlightNo;

	/**
	*所属机场代码
	*/
	@Column(name = "task_airport_code")
	private String taskAirportCode;

	/**
	*所属机场区域代码
	*/
	@Column(name = "task_aptarea_code")
	private String taskAptareaCode;

	/**
	*加油员员工ID
	*/
	@Column(name = "task_ope_staff_id")
	private String taskOpeStaffId;

	/**
	*任务内容（0：加油，1：抽油 2补加油 3过时加油 -1默认值）
	*/
	@Column(name = "task_content")
	private Integer taskContent;

	/**
	*任务状态（0：未下发，1：待接受，2：申请待批（预留），3：已接受，4：到位（预留），5：加油完成，6：油单待审核（预留），7：任务完成，8：拒绝（预留）,9：取消加油）
	*/
	@Column(name = "task_status")
	private Integer taskStatus;

	/**
	*任务派发时间
	*/
	@Column(name = "task_asg_time")
	@com.alibaba.fastjson.annotation.JSONField(format=BaseUtils.DATE_TIME_FMT)
	private java.util.Date taskAsgTime;

	/**
	*任务接受时间
	*/
	@Column(name = "task_acc_time")
	@com.alibaba.fastjson.annotation.JSONField(format=BaseUtils.DATE_TIME_FMT)
	private java.util.Date taskAccTime;

	/**
	*加油开始时间
	*/
	@Column(name = "task_chag_sta_time")
	@com.alibaba.fastjson.annotation.JSONField(format=BaseUtils.DATE_TIME_FMT)
	private java.util.Date taskChagStaTime;

	/**
	*加油到位时间
	*/
	@Column(name = "task_arrive_time")
	@com.alibaba.fastjson.annotation.JSONField(format=BaseUtils.DATE_TIME_FMT)
	private java.util.Date taskArriveTime;

	/**
	*打印油单完成时间
	*/
	@Column(name = "task_rc_print_time")
	@com.alibaba.fastjson.annotation.JSONField(format=BaseUtils.DATE_TIME_FMT)
	private java.util.Date taskRcPrintTime;

	/**
	*加油完成时间
	*/
	@Column(name = "task_chag_end_time")
	@com.alibaba.fastjson.annotation.JSONField(format=BaseUtils.DATE_TIME_FMT)
	private java.util.Date taskChagEndTime;

	/**
	*任务完成时间
	*/
	@Column(name = "task_done_time")
	@com.alibaba.fastjson.annotation.JSONField(format=BaseUtils.DATE_TIME_FMT)
	private java.util.Date taskDoneTime;

	/**
	*加油单编号
	*/
	@Column(name = "task_fuel_recpt_no")
	private String taskFuelRecptNo;

	/**
	*加油车编号
	*/
	@Column(name = "task_vehi_no")
	private String taskVehiNo;

	/**
	*创建人员工ID（调度员）
	*/
	@Column(name = "task_cre_staff_id")
	private String taskCreStaffId;

	/**
	*任务星标（0：不是，1：是）
	*/
	@Column(name = "task_starmark")
	private Integer taskStarmark;

	/**
	*记录创建时间（等同于任务创建时间）
	*/
	@Column(name = "task_rec_cre_time")
	@com.alibaba.fastjson.annotation.JSONField(format=BaseUtils.DATE_TIME_FMT)
	private java.util.Date taskRecCreTime;

	/**
	*起飞油量
	*/
	@Column(name = "task_takeoff_fuel")
	private Integer taskTakeoffFuel;

	/**
	*轮挡油量
	*/
	@Column(name = "task_chock_fuel")
	private Integer taskChockFuel;

	/**
	*应加油量
	*/
	@Column(name = "task_total_fuel")
	private Integer taskTotalFuel;

	/**
	*仪表类型（KG，LB）
	*/
	@Column(name = "task_meter_type")
	private String taskMeterType;

	/**
	*中央邮箱油量
	*/
	@Column(name = "task_cent_tank")
	private Integer taskCentTank;

	/**
	*左机翼油箱油量
	*/
	@Column(name = "task_left_tank")
	private Integer taskLeftTank;

	/**
	*右机翼油箱油量
	*/
	@Column(name = "task_right_tank")
	private Integer taskRightTank;

	/**
	*机组签名（JPG图片的base64编码）
	*/
	@Column(name = "task_crew_sign")
	private String taskCrewSign;


	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId=taskId;
	}

	public String getTaskFlightId() {
		return taskFlightId;
	}

	public void setTaskFlightId(String taskFlightId) {
		this.taskFlightId=taskFlightId;
	}

	public String getTaskFlightNo() {
		return taskFlightNo;
	}

	public void setTaskFlightNo(String taskFlightNo) {
		this.taskFlightNo=taskFlightNo;
	}

	public String getTaskAirportCode() {
		return taskAirportCode;
	}

	public void setTaskAirportCode(String taskAirportCode) {
		this.taskAirportCode=taskAirportCode;
	}

	public String getTaskAptareaCode() {
		return taskAptareaCode;
	}

	public void setTaskAptareaCode(String taskAptareaCode) {
		this.taskAptareaCode=taskAptareaCode;
	}

	public String getTaskOpeStaffId() {
		return taskOpeStaffId;
	}

	public void setTaskOpeStaffId(String taskOpeStaffId) {
		this.taskOpeStaffId=taskOpeStaffId;
	}

	public Integer getTaskContent() {
		return taskContent;
	}

	public void setTaskContent(Integer taskContent) {
		this.taskContent=taskContent;
	}

	public Integer getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus=taskStatus;
	}

	public java.util.Date getTaskAsgTime() {
		return taskAsgTime;
	}

	public void setTaskAsgTime(java.util.Date taskAsgTime) {
		this.taskAsgTime=taskAsgTime;
	}

	public java.util.Date getTaskAccTime() {
		return taskAccTime;
	}

	public void setTaskAccTime(java.util.Date taskAccTime) {
		this.taskAccTime=taskAccTime;
	}

	public java.util.Date getTaskChagStaTime() {
		return taskChagStaTime;
	}

	public void setTaskChagStaTime(java.util.Date taskChagStaTime) {
		this.taskChagStaTime=taskChagStaTime;
	}

	public java.util.Date getTaskArriveTime() {
		return taskArriveTime;
	}

	public void setTaskArriveTime(java.util.Date taskArriveTime) {
		this.taskArriveTime=taskArriveTime;
	}

	public java.util.Date getTaskRcPrintTime() {
		return taskRcPrintTime;
	}

	public void setTaskRcPrintTime(java.util.Date taskRcPrintTime) {
		this.taskRcPrintTime=taskRcPrintTime;
	}

	public java.util.Date getTaskChagEndTime() {
		return taskChagEndTime;
	}

	public void setTaskChagEndTime(java.util.Date taskChagEndTime) {
		this.taskChagEndTime=taskChagEndTime;
	}

	public java.util.Date getTaskDoneTime() {
		return taskDoneTime;
	}

	public void setTaskDoneTime(java.util.Date taskDoneTime) {
		this.taskDoneTime=taskDoneTime;
	}

	public String getTaskFuelRecptNo() {
		return taskFuelRecptNo;
	}

	public void setTaskFuelRecptNo(String taskFuelRecptNo) {
		this.taskFuelRecptNo=taskFuelRecptNo;
	}

	public String getTaskVehiNo() {
		return taskVehiNo;
	}

	public void setTaskVehiNo(String taskVehiNo) {
		this.taskVehiNo=taskVehiNo;
	}

	public String getTaskCreStaffId() {
		return taskCreStaffId;
	}

	public void setTaskCreStaffId(String taskCreStaffId) {
		this.taskCreStaffId=taskCreStaffId;
	}

	public Integer getTaskStarmark() {
		return taskStarmark;
	}

	public void setTaskStarmark(Integer taskStarmark) {
		this.taskStarmark=taskStarmark;
	}

	public java.util.Date getTaskRecCreTime() {
		return taskRecCreTime;
	}

	public void setTaskRecCreTime(java.util.Date taskRecCreTime) {
		this.taskRecCreTime=taskRecCreTime;
	}

	public Integer getTaskTakeoffFuel() {
		return taskTakeoffFuel;
	}

	public void setTaskTakeoffFuel(Integer taskTakeoffFuel) {
		this.taskTakeoffFuel=taskTakeoffFuel;
	}

	public Integer getTaskChockFuel() {
		return taskChockFuel;
	}

	public void setTaskChockFuel(Integer taskChockFuel) {
		this.taskChockFuel=taskChockFuel;
	}

	public Integer getTaskTotalFuel() {
		return taskTotalFuel;
	}

	public void setTaskTotalFuel(Integer taskTotalFuel) {
		this.taskTotalFuel=taskTotalFuel;
	}

	public String getTaskMeterType() {
		return taskMeterType;
	}

	public void setTaskMeterType(String taskMeterType) {
		this.taskMeterType=taskMeterType;
	}

	public Integer getTaskCentTank() {
		return taskCentTank;
	}

	public void setTaskCentTank(Integer taskCentTank) {
		this.taskCentTank=taskCentTank;
	}

	public Integer getTaskLeftTank() {
		return taskLeftTank;
	}

	public void setTaskLeftTank(Integer taskLeftTank) {
		this.taskLeftTank=taskLeftTank;
	}

	public Integer getTaskRightTank() {
		return taskRightTank;
	}

	public void setTaskRightTank(Integer taskRightTank) {
		this.taskRightTank=taskRightTank;
	}

	public String getTaskCrewSign() {
		return taskCrewSign;
	}

	public void setTaskCrewSign(String taskCrewSign) {
		this.taskCrewSign=taskCrewSign;
	}


	
}
