package com.zh.bean.login;

import java.io.Serializable;

/**
 * 车辆信息接口（提供给地图）
 */
public class TVehiInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 车号
	 */
	private String carID;
	
	/**
	 * 车辆类型
	 */
	private String carType;
	
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 车牌号
	 */
	private String carNum;
	
	/**
	 * 司机
	 */
	private String driver;
	
	/**
	 * 司机电话
	 */
	private String driverPhone;
	
	/**
	 * 加油员
	 */
	private String fMan;
	
	/**
	 * 加油员电话
	 */
	private String fPhone;

	/**
     * 车高
     */
    private Double vehiHeight;
	
    /**
     * 车高
     */
	public Double getVehiHeight() {
		return vehiHeight;
	}

	/**
     * 车高
     */
	public void setVehiHeight(Double vehiHeight) {
		this.vehiHeight = vehiHeight;
	}
    
	/**
	 * 车号
	 */
	public String getCarID() {
		return carID;
	}

	/**
	 * 车号
	 */
	public void setCarID(String carID) {
		this.carID = carID;
	}

	/**
	 * 车辆类型
	 */
	public String getCarType() {
		return carType;
	}

	/**
	 * 车辆类型
	 */
	public void setCarType(String carType) {
		this.carType = carType;
	}

	/**
	 * 状态
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 车牌号
	 */
	public String getCarNum() {
		return carNum;
	}

	/**
	 * 车牌号
	 */
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	/**
	 * 司机
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * 司机
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * 司机电话
	 */
	public String getDriverPhone() {
		return driverPhone;
	}

	/**
	 * 司机电话
	 */
	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}

	/**
	 * 加油员
	 */
	public String getfMan() {
		return fMan;
	}

	/**
	 * 加油员
	 */
	public void setfMan(String fMan) {
		this.fMan = fMan;
	}

	/**
	 * 加油员电话
	 */
	public String getfPhone() {
		return fPhone;
	}

	/**
	 * 加油员电话
	 */
	public void setfPhone(String fPhone) {
		this.fPhone = fPhone;
	}
}
