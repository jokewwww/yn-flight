package cn.iwen.duty.entity;

import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
@Table(name="T_STAFF")
public class Staff {
	
	/*
	 * 机位号
	 * */
	private String flgtPlacecode;
	
	
	/**
     * 加油员状态（0：未登录，1：空闲，2：工作）
     */
    private Integer sfvhStaffStatus;
    
    private Integer taskStatus = 0;
	/**
     * 航班号
     */
    private String flgtFlno;
	
    /**
     * 任务id
     */
    private String taskId;
    
    /**
     * 人员绑定车辆:车牌号
     */
    private String vehiPlateNo;
    
	/**
	 * 连续工作时间
	 */
	private String staffDateIng;
	
	/**
	 * 连续休息时间
	 */
	private String staffDateFree;
	
	/**
	 * 连续任务数量
	 */
	private Integer staffingTaskCount = 0;

	/**
	 * 当天工作数量
	 */
	private Integer staffEndTaskCount = 0;
	
	/**
	*
	*/
	@Id
	@Column(name = "staff_id",nullable=false)
	private String sfvhStaffId;

	/**
	*
	
	@Column(name = "staff_pwd")
	private String staffPwd;*/

	/**
	*员工姓名
	*/
	@Column(name = "staff_name")
	private String staffName;

	/**
	*员工年龄
	
	@Column(name = "staff_age")
	private Integer staffAge;
	 */
	/**
	*员工性别（0：女，1：男）*/
	
	@Column(name = "staff_gender")
	private Integer staffGender;

	/**
	*岗位（0:超级管理员，1:管理员（所有机场），2:调度员，3:加油员，4加油员队长，5加油员副队长）
	*/
	@Column(name = "staff_type",nullable=false)
	@JSONField(serialize = false)
	private String staffType;

	/**
	*电话
	
	@Column(name = "staff_phone")
	private String staffPhone;*/

	/**
	*子公司
	
	@Column(name = "staff_company")
	private String staffCompany;*/

	/**
	*航空加油站
	
	@Column(name = "staff_serv_station")
	private String staffServStation;*/

	/**
	*所属机场代码
	*/
	@Column(name = "staff_airport_code")
	private String staffAirportCode;

	/**
	*所属机场区域代码
	*/
	@Column(name = "staff_aptarea_code")
	private String staffAptareaCode;

	/**
	*分组ID（uuid）
	*/
	@Column(name = "staff_group_id")
	private String staffGroupId;

	/**
	*排序字段
	
	@Column(name = "staff_level")
	private Integer staffLevel;*/


	
	
	public void addTaskCount() {
		if(staffEndTaskCount == null) 
			staffEndTaskCount = 1;
		else
			staffEndTaskCount++;
	}


	public Integer getStaffGender() {
		return staffGender;
	}

	public void setStaffGender(Integer staffGender) {
		this.staffGender = staffGender;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName=staffName;
	}

	public String getStaffAirportCode() {
		return staffAirportCode;
	}

	public void setStaffAirportCode(String staffAirportCode) {
		this.staffAirportCode=staffAirportCode;
	}

	public String getStaffAptareaCode() {
		return staffAptareaCode;
	}

	public void setStaffAptareaCode(String staffAptareaCode) {
		this.staffAptareaCode=staffAptareaCode;
	}

	public String getStaffGroupId() {
		return staffGroupId;
	}

	public void setStaffGroupId(String staffGroupId) {
		this.staffGroupId=staffGroupId;
	}

	public String getFlgtPlacecode() {
		return flgtPlacecode;
	}

	public void setFlgtPlacecode(String flgtPlacecode) {
		this.flgtPlacecode = flgtPlacecode;
	}

	public Integer getSfvhStaffStatus() {
		return sfvhStaffStatus;
	}

	public void setSfvhStaffStatus(Integer sfvhStaffStatus) {
		this.sfvhStaffStatus = sfvhStaffStatus;
	}

	public String getFlgtFlno() {
		return flgtFlno;
	}

	public void setFlgtFlno(String flgtFlno) {
		this.flgtFlno = flgtFlno;
	}

	public String getStaffDateIng() {
		return staffDateIng;
	}

	public void setStaffDateIng(String staffDateIng) {
		this.staffDateIng = staffDateIng;
	}

	public String getStaffDateFree() {
		return staffDateFree;
	}

	public void setStaffDateFree(String staffDateFree) {
		this.staffDateFree = staffDateFree;
	}

	public Integer getStaffingTaskCount() {
		return staffingTaskCount;
	}

	public void setStaffingTaskCount(Integer staffingTaskCount) {
		this.staffingTaskCount = staffingTaskCount;
	}

	public String getSfvhStaffId() {
		return sfvhStaffId;
	}

	public void setSfvhStaffId(String sfvhStaffId) {
		this.sfvhStaffId = sfvhStaffId;
	}

	public Integer getStaffEndTaskCount() {
		return staffEndTaskCount;
	}

	public void setStaffEndTaskCount(Integer staffEndTaskCount) {
		this.staffEndTaskCount = staffEndTaskCount;
	}

	public String getStaffType() {
		return staffType;
	}

	public void setStaffType(String staffType) {
		this.staffType = staffType;
	}

	public Integer getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getVehiPlateNo() {
		if(vehiPlateNo == null) return "";
		return vehiPlateNo;
	}

	public void setVehiPlateNo(String vehiPlateNo) {
		this.vehiPlateNo = vehiPlateNo;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
}
