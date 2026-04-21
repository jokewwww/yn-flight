package com.zh.bean.login;

import java.io.Serializable;

/**
 * 人员表和车辆表和任务表
 * T_STAFF 和 T_VEHI 和 T_TASK
 */
public class MyStaffVehiTask implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
     * 加油员ID
     */
    private String sfvhStaffId;

    /**
     * 加油员姓名
     */
    private String staffName;
    
    /**
     * 加油员姓名
     */
	public String getStaffName() {
		return staffName;
	}

	/**
     * 加油员姓名
     */
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	/**
     * 加油员电话
     */
    private String staffPhone;
    
	/**
     * 加油员状态（0：未登录，1：空闲，2：工作）
     */
    private Integer sfvhStaffStatus;
	
    /**
     * 车辆ID（uuid）
     */
    private String vehiId;
	
	/**
     * 车辆编号（车辆的唯一标识）
     */
    private String vehiNo;

    /**
     * 车辆别名
     */
    private String vehiNickname;

	/**
     * 车牌号
     */
    private String vehiPlateNo;
	
	/**
     * 航班号
     */
    private String flgtFlno;
	/**
	 * 分组id
	 */
	private String staffGroupId;
	
	/**
	 * 连续工作时间
	 */
	private String staffDateIng;
	
	/**
	 * 当天工作数量
	 */
	private Integer staffEndTaskCount;
	
	/**
	 * 连续休息时间
	 */
	private String staffDateFree;
	
	/**
	 * 连续任务数量
	 */
	private Integer staffingTaskCount;


	private String alpha;

	private String className;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getAlpha() {
		return alpha;
	}

	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}

	/**
	 * 正在工作时长
	 */
	public String getStaffDateIng() {
		return staffDateIng;
	}

	private String taskId;

	private Integer taskStatus;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Integer getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}

	/**
	 * 正在工作时长
	 */
	public void setStaffDateIng(String staffDateIng) {
		this.staffDateIng = staffDateIng;
	}

	/**
	 * 当天完成任务数量
	 */
	public Integer getStaffEndTaskCount() {
		return staffEndTaskCount;
	}

	/**
	 * 当天完成任务数量
	 */
	public void setStaffEndTaskCount(Integer staffEndTaskCount) {
		this.staffEndTaskCount = staffEndTaskCount;
	}

	/**
	 * @return the staffDateFree
	 */
	public String getStaffDateFree() {
		return staffDateFree;
	}

	/**
	 * @param staffDateFree the staffDateFree to set
	 */
	public void setStaffDateFree(String staffDateFree) {
		this.staffDateFree = staffDateFree;
	}

	/**
	 * @return the staffingTaskCount
	 */
	public Integer getStaffingTaskCount() {
		return staffingTaskCount;
	}

	/**
	 * @param staffingTaskCount the staffingTaskCount to set
	 */
	public void setStaffingTaskCount(Integer staffingTaskCount) {
		this.staffingTaskCount = staffingTaskCount;
	}

	/**
	 * 分组名
	 */
	public String getStaffGroupId() {
		return staffGroupId;
	}

	/**
	 * 分组名
	 */
	public void setStaffGroupId(String staffGroupId) {
		this.staffGroupId = staffGroupId;
	}
	/**
	 * @return the isAnimate
	 */
	public int getIsAnimate() {
		return isAnimate;
	}

	/**
	 * @param isAnimate the isAnimate to set
	 */
	public void setIsAnimate(int isAnimate) {
		this.isAnimate = isAnimate;
	}

	/**
     * 断线动画
     */
    private int isAnimate = 0;

    
    /**
     * 加油员ID
     */
	public String getSfvhStaffId() {
		return sfvhStaffId;
	}

	/**
     * 加油员ID
     */
	public void setSfvhStaffId(String sfvhStaffId) {
		this.sfvhStaffId = sfvhStaffId;
	}

	/**
     * 加油员电话
     */
	public String getStaffPhone() {
		return staffPhone;
	}

	/**
     * 加油员电话
     */
	public void setStaffPhone(String staffPhone) {
		this.staffPhone = staffPhone;
	}

	/**
     * 加油员状态（0：未登录，1：空闲，2：工作）
     */
	public Integer getSfvhStaffStatus() {
		return sfvhStaffStatus;
	}

	/**
     * 加油员状态（0：未登录，1：空闲，2：工作）
     */
	public void setSfvhStaffStatus(Integer sfvhStaffStatus) {
		this.sfvhStaffStatus = sfvhStaffStatus;
	}

	/**
     * 车辆ID（uuid）
     */
	public String getVehiId() {
		return vehiId;
	}

	/**
     * 车辆ID（uuid）
     */
	public void setVehiId(String vehiId) {
		this.vehiId = vehiId;
	}

	/**
     * 车辆编号（车辆的唯一标识）
     */
	public String getVehiNo() {
		return vehiNo;
	}

	/**
     * 车辆编号（车辆的唯一标识）
     */
	public void setVehiNo(String vehiNo) {
		this.vehiNo = vehiNo;
	}

	/**
     * 车辆别名
     */
	public String getVehiNickname() {
		return vehiNickname;
	}

	/**
     * 车辆别名
     */
	public void setVehiNickname(String vehiNickname) {
		this.vehiNickname = vehiNickname;
	}

	/**
     * 车牌号
     */
	public String getVehiPlateNo() {
		return vehiPlateNo;
	}

	/**
     * 车牌号
     */
	public void setVehiPlateNo(String vehiPlateNo) {
		this.vehiPlateNo = vehiPlateNo;
	}

	/**
     * 航班号
     */
	public String getFlgtFlno() {
		return flgtFlno;
	}

	/**
     * 航班号
     */
	public void setFlgtFlno(String flgtFlno) {
		this.flgtFlno = flgtFlno;
	}
}