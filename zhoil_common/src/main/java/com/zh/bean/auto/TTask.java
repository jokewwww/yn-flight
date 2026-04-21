package com.zh.bean.auto;

import java.io.Serializable;
import java.util.Date;

public class TTask implements Serializable{

public static long getSerialversionuid() {
	return serialVersionUID;
}

	/**
     * 任务ID
     */
    private String taskId;

    /**
     * 航班ID
     */
    private String taskFlightId;

    /**
     * 航班唯一号
     */
    private String taskFlightNo;

    /**
     * 加油员员工ID
     */
    private String taskOpeStaffId;

    /**
     * 任务内容
     */
    private int taskContent;

    /**
     * 任务状态,0：未下发1：待接受2：申请待批3：已接受4：到位5：加油完成6：油单待审核7：任务完成8：拒绝
     */
    private int taskStatus;

    /**
     * 任务派发时间
     */
    private Date taskAsgTime;

    /**
     * 任务接受时间
     */
    private Date taskAccTime;

    /**
     * 加油开始时间
     */
    private Date taskChagStaTime;

    /**
     * 加油完成时间
     */
    private Date taskChagEndTime;

    /**
     * 任务完成时间
     */
    private Date taskDoneTime;

    /**
     * 加油单编号
     */
    private String taskFuelRecptNo;

    /**
     * 加油车编号
     */
    private String taskVehiNo;

    /**
     * 创建人员工ID
     */
    private String taskCreStaffId;

    /**
     * 任务星标
     */
    private Boolean taskStarmark;

    /**
     * 记录创建时间
     */
    private Date taskRecCreTime;

    /**
     * 地井编号
     */
    private String well_hydrt_pit_no;

    /**
     * 任务名称
     */
    private String statusName;
    
    /**
     * 油单编号
     */
    private String flrcNo;

    /**
	 * @return the flrcNo
	 */
	public String getFlrcNo() {
		return flrcNo;
	}

	/**
	 * @param flrcNo the flrcNo to set
	 */
	public void setFlrcNo(String flrcNo) {
		this.flrcNo = flrcNo;
	}

	public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /**
     * T_TASK
     */
    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     * @return task_id 任务ID
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * 任务ID
     * @param taskId 任务ID
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    /**
     * 航班ID
     * @return task_flight_id 航班ID
     */
    public String getTaskFlightId() {
        return taskFlightId;
    }

    /**
     * 航班ID
     * @param taskFlightId 航班ID
     */
    public void setTaskFlightId(String taskFlightId) {
        this.taskFlightId = taskFlightId == null ? null : taskFlightId.trim();
    }

    /**
     * 航班唯一号
     * @return task_flight_no 航班唯一号
     */
    public String getTaskFlightNo() {
        return taskFlightNo;
    }

    /**
     * 航班唯一号
     * @param taskFlightNo 航班唯一号
     */
    public void setTaskFlightNo(String taskFlightNo) {
        this.taskFlightNo = taskFlightNo == null ? null : taskFlightNo.trim();
    }

    /**
     * 加油员员工ID
     * @return task_ope_staff_id 加油员员工ID
     */
    public String getTaskOpeStaffId() {
        return taskOpeStaffId;
    }

    /**
     * 加油员员工ID
     * @param taskOpeStaffId 加油员员工ID
     */
    public void setTaskOpeStaffId(String taskOpeStaffId) {
        this.taskOpeStaffId = taskOpeStaffId == null ? null : taskOpeStaffId.trim();
    }

    /**
     * 任务内容
     * @return task_content 任务内容
     */
    public int getTaskContent() {
        return taskContent;
    }

    /**
     * 任务内容
     * @param taskContent 任务内容
     */
    public void setTaskContent(int taskContent) {
        this.taskContent = taskContent;
    }



    /**
     * 任务状态,0：未下发1：待接受2：申请待批3：已接受4：到位5：加油完成6：油单待审核7：任务完成8：拒绝
     * @return task_status 任务状态,0：未下发1：待接受2：申请待批3：已接受4：到位5：加油完成6：油单待审核7：任务完成8：拒绝9：不加油
     */
//    public Boolean getTaskStatus() {
//        return taskStatus;
//    }
//
//    /**
//     * 任务状态,0：未下发1：待接受2：申请待批3：已接受4：到位5：加油完成6：油单待审核7：任务完成8：拒绝
//     * @param taskStatus 任务状态,0：未下发1：待接受2：申请待批3：已接受4：到位5：加油完成6：油单待审核7：任务完成8：拒绝
//     */
//    public void setTaskStatus(Boolean taskStatus) {
//        this.taskStatus = taskStatus;
//    }

    /**
     * 任务派发时间
     * @return task_asg_time 任务派发时间
     */
    public Date getTaskAsgTime() {
        return taskAsgTime;
    }

    public int getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
	}

	/**
     * 任务派发时间
     * @param taskAsgTime 任务派发时间
     */
    public void setTaskAsgTime(Date taskAsgTime) {
        this.taskAsgTime = taskAsgTime;
    }

    /**
     * 任务接受时间
     * @return task_acc_time 任务接受时间
     */
    public Date getTaskAccTime() {
        return taskAccTime;
    }

    /**
     * 任务接受时间
     * @param taskAccTime 任务接受时间
     */
    public void setTaskAccTime(Date taskAccTime) {
        this.taskAccTime = taskAccTime;
    }

    /**
     * 加油开始时间
     * @return task_chag_sta_time 加油开始时间
     */
    public Date getTaskChagStaTime() {
        return taskChagStaTime;
    }

    /**
     * 加油开始时间
     * @param taskChagStaTime 加油开始时间
     */
    public void setTaskChagStaTime(Date taskChagStaTime) {
        this.taskChagStaTime = taskChagStaTime;
    }

    /**
     * 加油完成时间
     * @return task_chag_end_time 加油完成时间
     */
    public Date getTaskChagEndTime() {
        return taskChagEndTime;
    }

    /**
     * 加油完成时间
     * @param taskChagEndTime 加油完成时间
     */
    public void setTaskChagEndTime(Date taskChagEndTime) {
        this.taskChagEndTime = taskChagEndTime;
    }

    /**
     * 任务完成时间
     * @return task_done_time 任务完成时间
     */
    public Date getTaskDoneTime() {
        return taskDoneTime;
    }

    /**
     * 任务完成时间
     * @param taskDoneTime 任务完成时间
     */
    public void setTaskDoneTime(Date taskDoneTime) {
        this.taskDoneTime = taskDoneTime;
    }

//    /**
//     * 加油单编号
//     * @return task_fuel_recpt_no 加油单编号
//     */
//    public Integer getTaskFuelRecptNo() {
//        return taskFuelRecptNo;
//    }
//
//    /**
//     * 加油单编号
//     * @param taskFuelRecptNo 加油单编号
//     */
//    public void setTaskFuelRecptNo(Integer taskFuelRecptNo) {
//        this.taskFuelRecptNo = taskFuelRecptNo;
//    }

    /**
     * 加油车编号
     * @return task_vehi_no 加油车编号
     */
    public String getTaskVehiNo() {
        return taskVehiNo;
    }

    public String getTaskFuelRecptNo() {
		return taskFuelRecptNo;
	}

	public void setTaskFuelRecptNo(String taskFuelRecptNo) {
		this.taskFuelRecptNo = taskFuelRecptNo;
	}

	/**
     * 加油车编号
     * @param taskVehiNo 加油车编号
     */
    public void setTaskVehiNo(String taskVehiNo) {
        this.taskVehiNo = taskVehiNo == null ? null : taskVehiNo.trim();
    }

    /**
     * 创建人员工ID
     * @return task_cre_staff_id 创建人员工ID
     */
    public String getTaskCreStaffId() {
        return taskCreStaffId;
    }

    /**
     * 创建人员工ID
     * @param taskCreStaffId 创建人员工ID
     */
    public void setTaskCreStaffId(String taskCreStaffId) {
        this.taskCreStaffId = taskCreStaffId == null ? null : taskCreStaffId.trim();
    }

    /**
     * 任务星标
     * @return task_starmark 任务星标
     */
    public Boolean getTaskStarmark() {
        return taskStarmark;
    }

    /**
     * 任务星标
     * @param taskStarmark 任务星标
     */
    public void setTaskStarmark(Boolean taskStarmark) {
        this.taskStarmark = taskStarmark;
    }

    /**
     * 记录创建时间
     * @return task_rec_cre_time 记录创建时间
     */
    public Date getTaskRecCreTime() {
        return taskRecCreTime;
    }

    /**
     * 记录创建时间
     * @param taskRecCreTime 记录创建时间
     */
    public void setTaskRecCreTime(Date taskRecCreTime) {
        this.taskRecCreTime = taskRecCreTime;
    }

    public String getWell_hydrt_pit_no() {
        return well_hydrt_pit_no;
    }

    public void setWell_hydrt_pit_no(String well_hydrt_pit_no) {
        this.well_hydrt_pit_no = well_hydrt_pit_no;
    }

	@Override
	public String toString() {
		return "TTask [taskId=" + taskId + ", taskFlightId=" + taskFlightId + ", taskFlightNo=" + taskFlightNo
				+ ", taskOpeStaffId=" + taskOpeStaffId + ", taskContent=" + taskContent + ", taskStatus=" + taskStatus
				+ ", taskAsgTime=" + taskAsgTime + ", taskAccTime=" + taskAccTime + ", taskChagStaTime="
				+ taskChagStaTime + ", taskChagEndTime=" + taskChagEndTime + ", taskDoneTime=" + taskDoneTime
				+ ", taskFuelRecptNo=" + taskFuelRecptNo + ", taskVehiNo=" + taskVehiNo + ", taskCreStaffId="
				+ taskCreStaffId + ", taskStarmark=" + taskStarmark + ", taskRecCreTime=" + taskRecCreTime
				+ ", well_hydrt_pit_no=" + well_hydrt_pit_no + ", statusName=" + statusName + "]";
	}
    
}
