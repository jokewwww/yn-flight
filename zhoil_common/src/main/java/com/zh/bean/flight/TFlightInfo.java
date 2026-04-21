package com.zh.bean.flight;

import java.io.Serializable;

/**
 * 飞机详细信息接口（提供给地图）
 */
public class TFlightInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 形状
	 */
    private String shape;

	/**
	 * 机号
	 */
	private String aircraftID;
	
	/**
	 * 航线
	 */
	private String airRoute;
	
	/**
	 * 加油信息
	 */
	private String refuelInfo;
	
	/**
	 * 进港状态
	 */
	private String statusA;
	
	/**
	 * 进港航班号
	 */
	private String flightNA;
	
	/**
	 * 进港范围
	 */
	private String rangeA;
	
	/**
	 * 转盘口
	 */
	private String gateA;
	
	/**
	 * 进港时间
	 */
	private String timeA;
	
	/**
	 * 出港状态
	 */
	private String statusD;
	
	/**
	 * 出港航班号
	 */
	private String flightND;
	
	/**
	 * 出港范围
	 */
	private String rangeD;
	
	/**
	 * 登机口
	 */
	private String gateD;
	
	/**
	 * 出港时间
	 */
	private String timeD;

	private String flgtAirportCode;
	
	public String getFlgtAirportCode() {
		return flgtAirportCode;
	}

	public void setFlgtAirportCode(String flgtAirportCode) {
		this.flgtAirportCode = flgtAirportCode;
	}

	/**
	 * 机号
	 */
	public String getAircraftID() {
		return aircraftID;
	}

	/**
	 * 机号
	 */
	public void setAircraftID(String aircraftID) {
		this.aircraftID = aircraftID;
	}
	
	/**
	 * 航线
	 */
	public String getAirRoute() {
		return airRoute;
	}
	
	/**
	 * 航线
	 */
	public void setAirRoute(String airRoute) {
		this.airRoute = airRoute;
	}

	/**
	 * 加油信息
	 */
	public String getRefuelInfo() {
		return refuelInfo;
	}

	/**
	 * 加油信息
	 */
	public void setRefuelInfo(String refuelInfo) {
		this.refuelInfo = refuelInfo;
	}

	/**
	 * 进港状态
	 */
	public String getStatusA() {
		return statusA;
	}

	/**
	 * 进港状态
	 */
	public void setStatusA(String statusA) {
		this.statusA = statusA;
	}

	/**
	 * 进港航班号
	 */
	public String getFlightNA() {
		return flightNA;
	}

	/**
	 * 进港航班号
	 */
	public void setFlightNA(String flightNA) {
		this.flightNA = flightNA;
	}

	/**
	 * 进港范围
	 */
	public String getRangeA() {
		return rangeA;
	}

	/**
	 * 进港范围
	 */
	public void setRangeA(String rangeA) {
		this.rangeA = rangeA;
	}

	/**
	 * 转盘口
	 */
	public String getGateA() {
		return gateA;
	}

	/**
	 * 转盘口
	 */
	public void setGateA(String gateA) {
		this.gateA = gateA;
	}

	/**
	 * 进港时间
	 */
	public String getTimeA() {
		return timeA;
	}

	/**
	 * 进港时间
	 */
	public void setTimeA(String timeA) {
		this.timeA = timeA;
	}

	/**
	 * 出港状态
	 */
	public String getStatusD() {
		return statusD;
	}

	/**
	 * 出港状态
	 */
	public void setStatusD(String statusD) {
		this.statusD = statusD;
	}

	/**
	 * 出港航班号
	 */
	public String getFlightND() {
		return flightND;
	}

	/**
	 * 出港航班号
	 */
	public void setFlightND(String flightND) {
		this.flightND = flightND;
	}

	/**
	 * 出港范围
	 */
	public String getRangeD() {
		return rangeD;
	}

	/**
	 * 出港范围
	 */
	public void setRangeD(String rangeD) {
		this.rangeD = rangeD;
	}

	/**
	 * 登机口
	 */
	public String getGateD() {
		return gateD;
	}

	/**
	 * 登机口
	 */
	public void setGateD(String gateD) {
		this.gateD = gateD;
	}

	/**
	 * 出港时间
	 */
	public String getTimeD() {
		return timeD;
	}

	/**
	 * 出港时间
	 */
	public void setTimeD(String timeD) {
		this.timeD = timeD;
	}
	/**
	 * 形状
	 */

	public String getShape() {
		return shape;
	}
	/**
	 * 形状
	 */
	public void setShape(String shape) {
		this.shape = shape;
	}
	
}
