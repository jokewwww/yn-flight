package com.zh.bean.flight;

import java.io.Serializable;

/**
 * 飞机信息接口（提供给地图）
 */
public class TFlightInfoList implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 飞机唯一ID
	 */
	private String aircraftID;
	
	/**
	 * 机号
	 */
	private String aircraftNum;
	
	/**
	 * 机位号
	 */
	private String seatID;
	
	/**
	 * 落地航班号
	 */
	private String landFlightN;
	
	/**
	 * 起飞航班号
	 */
	private String takeOffFlightN;
	
	/**
	 * 远近状态
	 */
	private String farNear;
	
	/**
	 * 机型
	 */
	private String model;
	
	/**
	 * Vip标识
	 */
	private String vip;
	
	/**
	 * 航班状态
	 */
	private String flightState;
	
	/**
	 * 航空公司名称
	 */
	private String companyName;

	/**
	 * 飞机类型
	 */
	private String airPlaneType;
	

	private String  flgtDStot;
	private String  flgtAStot;
	/**
	 * @return the flgtAAtot
	 */
	public String getFlgtAAtot() {
		return flgtAAtot;
	}

	/**
	 * @return the flgtDStot
	 */
	public String getFlgtDStot() {
		return flgtDStot;
	}

	/**
	 * @param flgtDStot the flgtDStot to set
	 */
	public void setFlgtDStot(String flgtDStot) {
		this.flgtDStot = flgtDStot;
	}

	/**
	 * @return the flgtAStot
	 */
	public String getFlgtAStot() {
		return flgtAStot;
	}

	/**
	 * @param flgtAStot the flgtAStot to set
	 */
	public void setFlgtAStot(String flgtAStot) {
		this.flgtAStot = flgtAStot;
	}

	/**
	 * @param flgtAAtot the flgtAAtot to set
	 */
	public void setFlgtAAtot(String flgtAAtot) {
		this.flgtAAtot = flgtAAtot;
	}

	/**
	 * @return the flgtDAtot
	 */
	public String getFlgtDAtot() {
		return flgtDAtot;
	}

	/**
	 * @param flgtDAtot the flgtDAtot to set
	 */
	public void setFlgtDAtot(String flgtDAtot) {
		this.flgtDAtot = flgtDAtot;
	}

	/**
	 * 落地时间
	 */
	private String flgtAAtot;
	/**
	 * 离地时间
	 */
	private String flgtDAtot;
	
	/**
	 * 任务状态
	 */
	private String taskStatus;
	
	/**
	 * 飞机类型
	 */
	public String getAirPlaneType() {
		return airPlaneType;
	}

	/**
	 * 飞机类型
	 */
	public void setAirPlaneType(String airPlaneType) {
		this.airPlaneType = airPlaneType;
	}

	/**
	 * 任务状态
	 */
	public String getTaskStatus() {
		return taskStatus;
	}

	/**
	 * 任务状态
	 */
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	/**
	 * 飞机唯一ID
	 */
	public String getAircraftID() {
		return aircraftID;
	}

	/**
	 * 飞机唯一ID
	 */
	public void setAircraftID(String aircraftID) {
		this.aircraftID = aircraftID;
	}

	/**
	 * 机号
	 */
	public String getAircraftNum() {
		return aircraftNum;
	}

	/**
	 * 机号
	 */
	public void setAircraftNum(String aircraftNum) {
		this.aircraftNum = aircraftNum;
	}

	/**
	 * 机位号
	 */
	public String getSeatID() {
		return seatID;
	}

	/**
	 * 机位号
	 */
	public void setSeatID(String seatID) {
		this.seatID = seatID;
	}

	/**
	 * 落地航班号
	 */
	public String getLandFlightN() {
		return landFlightN;
	}

	/**
	 * 落地航班号
	 */
	public void setLandFlightN(String landFlightN) {
		this.landFlightN = landFlightN;
	}

	/**
	 * 起飞航班号
	 */
	public String getTakeOffFlightN() {
		return takeOffFlightN;
	}

	/**
	 * 起飞航班号
	 */
	public void setTakeOffFlightN(String takeOffFlightN) {
		this.takeOffFlightN = takeOffFlightN;
	}

	/**
	 * 远近状态
	 */
	public String getFarNear() {
		return farNear;
	}

	/**
	 * 远近状态
	 */
	public void setFarNear(String farNear) {
		this.farNear = farNear;
	}

	/**
	 * 机型
	 */
	public String getModel() {
		return model;
	}

	/**
	 * 机型
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Vip标识
	 */
	public String getVip() {
		return vip;
	}

	/**
	 * Vip标识
	 */
	public void setVip(String vip) {
		this.vip = vip;
	}

	/**
	 * 航班状态
	 */
	public String getFlightState() {
		return flightState;
	}

	/**
	 * 航班状态
	 */
	public void setFlightState(String flightState) {
		this.flightState = flightState;
	}

	/**
	 * 航空公司名称
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * 航空公司名称
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
