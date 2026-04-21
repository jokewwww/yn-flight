package com.zh.bean.flight;

import java.io.Serializable;

/**
 * 未分配任务报警的航班
 */
public class MyNoTaskAlarmFlight implements Serializable {

	/**
	 * T_FLIGHT
	 */
	private static final long serialVersionUID = 1L;


	private String taskId;

	private String flgtId;

	private String flgtAcname;

	private String staffId;

	private Long time;

    // 0: 发送报警；1：取消报警
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type == null ? 0:type;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getFlgtId() {
        return flgtId;
    }

    public void setFlgtId(String flgtId) {
        this.flgtId = flgtId;
    }

    public String getFlgtAcname() {
        return flgtAcname;
    }

    public void setFlgtAcname(String flgtAcname) {
        this.flgtAcname = flgtAcname;
    }
}