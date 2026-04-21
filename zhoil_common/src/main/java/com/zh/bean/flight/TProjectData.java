package com.zh.bean.flight;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 规划数据（地图）
 */
public class TProjectData implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 飞机实际降落时间
	 */
	private Date flgtAAtot;
	
	/**
	 * 机组到位时间
	 */
	private Date flgtCrewInPlace;
	
	/**
	 * 持续工作时间（MAP）
	 */
	private Map<String,Long> workTime;
	
	/**
	 * 工作任务数（MAP）
	 */
	private Map<String,Integer> taskCount;

	/**
	 * 飞机实际降落时间
	 */
	public Date getFlgtAAtot() {
		return flgtAAtot;
	}

	/**
	 * 飞机实际降落时间
	 */
	public void setFlgtAAtot(Date flgtAAtot) {
		this.flgtAAtot = flgtAAtot;
	}

	/**
	 * 机组到位时间
	 */
	public Date getFlgtCrewInPlace() {
		return flgtCrewInPlace;
	}

	/**
	 * 机组到位时间
	 */
	public void setFlgtCrewInPlace(Date flgtCrewInPlace) {
		this.flgtCrewInPlace = flgtCrewInPlace;
	}

	/**
	 * 持续工作时间（MAP）
	 */
	public Map<String, Long> getWorkTime() {
		return workTime;
	}

	/**
	 * 持续工作时间（MAP）
	 */
	public void setWorkTime(Map<String, Long> workTime) {
		this.workTime = workTime;
	}
	
	/**
	 * 工作任务数（MAP）
	 */
	public Map<String, Integer> getTaskCount() {
		return taskCount;
	}

	/**
	 * 工作任务数（MAP）
	 */
	public void setTaskCount(Map<String, Integer> taskCount) {
		this.taskCount = taskCount;
	}
}
