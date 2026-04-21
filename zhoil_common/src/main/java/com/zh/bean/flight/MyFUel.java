package com.zh.bean.flight;

public class MyFUel {
	
	/**
	 * 加油单ID
	 */
	private String flrcId;

	 /**
	  * 加油员ID
	  */
	private String token;
	
	/**
	 * 油单编号
	 */
	private String id;
	
	/**
	 * 日期
	 */
	private String date;
	
	/**
	 * 机场
	 */
	private String airport;
	
	/**
	 * 所属单位
	 */
	private String delivered;
	
	/**
	 * 航班号
	 */
	private String filghtNo;
	
	/**
	 * 飞机号
	 */
	private String aircraftNo;
	
	/**
	 * 飞机类型
	 */
	private String aircraftType;
	
	/**
	 * 起始
	 */
	private String departure;
	
	/**
	 * 经停（备降）
	 */
	private String transitStop;
	
	/**
	 * 终点
	 */
	
	private String destination;
	
	/**
	 * 化验单号码
	 */
	private String testBillNo;
	
	/**
	 * 油瓶名称与标准
	 */
	private String descriptionAndGrade;
	
	/**
	 * 温度：摄氏度
	 */
	private Double temperature;
	
	/**
	 * 实际密度
	 */
	private Double actualDensity;
	
	/**
	 * 计量表开始读数
	 */
	private Integer meterStart;
	
	/**
	 * 计量表结束读数
	 */
	private Integer meterFinish;
	
	/**
	 * 加油数量小写
	 */
	private Integer figures;
	
	/**
	 * 加油量大写
	 */
	private String  figuresWords;
	
	/**
	 * 加油数量
	 */
	private Double quantity;
	
	/**
	 * 加油地井编号
	 */
	private String hydrantPitNo;
	
	/**
	 * 加油车车号
	 */
	private String vehicleTypeAndNo;
	
	/**
	 * 加油开始时间
	 */
	private String timeStart;
	
	/**
	 * 加油结束时间
	 */
	private String timeFinish;
	
	/**
	 * 签名照片
	 */
	private String signPhoto;
	
	/**
	 * 加油员
	 */
	private String signName;
	/**
	 * 油单类型
	 */
	private int flrcType;
	
	/**
	 * @return the flrcType
	 */
	public int getFlrcType() {
		return flrcType;
	}

	/**
	 * @param flrcType the flrcType to set
	 */
	public void setFlrcType(int flrcType) {
		this.flrcType = flrcType;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

  



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAirport() {
		return airport;
	}

	public void setAirport(String airport) {
		this.airport = airport;
	}

	public String getDelivered() {
		return delivered;
	}

	public void setDelivered(String delivered) {
		this.delivered = delivered;
	}

	public String getFilghtNo() {
		return filghtNo;
	}

	public void setFilghtNo(String filghtNo) {
		this.filghtNo = filghtNo;
	}

	public String getAircraftNo() {
		return aircraftNo;
	}

	public void setAircraftNo(String aircraftNo) {
		this.aircraftNo = aircraftNo;
	}

	public String getAircraftType() {
		return aircraftType;
	}

	public void setAircraftType(String aircraftType) {
		this.aircraftType = aircraftType;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getTransitStop() {
		return transitStop;
	}

	public void setTransitStop(String transitStop) {
		this.transitStop = transitStop;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getTestBillNo() {
		return testBillNo;
	}

	public void setTestBillNo(String testBillNo) {
		this.testBillNo = testBillNo;
	}

	public String getDescriptionAndGrade() {
		return descriptionAndGrade;
	}

	public void setDescriptionAndGrade(String descriptionAndGrade) {
		this.descriptionAndGrade = descriptionAndGrade;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Double getActualDensity() {
		return actualDensity;
	}

	public void setActualDensity(Double actualDensity) {
		this.actualDensity = actualDensity;
	}

	public Integer getMeterStart() {
		return meterStart;
	}

	public void setMeterStart(Integer meterStart) {
		this.meterStart = meterStart;
	}

	public Integer getMeterFinish() {
		return meterFinish;
	}

	public void setMeterFinish(Integer meterFinish) {
		this.meterFinish = meterFinish;
	}

	public Integer getFigures() {
		return figures;
	}

	public void setFigures(Integer figures) {
		this.figures = figures;
	}

	public String getFiguresWords() {
		return figuresWords;
	}

	public void setFiguresWords(String figuresWords) {
		this.figuresWords = figuresWords;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getHydrantPitNo() {
		return hydrantPitNo;
	}

	public void setHydrantPitNo(String hydrantPitNo) {
		this.hydrantPitNo = hydrantPitNo;
	}

	public String getVehicleTypeAndNo() {
		return vehicleTypeAndNo;
	}

	public void setVehicleTypeAndNo(String vehicleTypeAndNo) {
		this.vehicleTypeAndNo = vehicleTypeAndNo;
	}

	public String getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}

	public String getTimeFinish() {
		return timeFinish;
	}

	public void setTimeFinish(String timeFinish) {
		this.timeFinish = timeFinish;
	}

	public String getSignPhoto() {
		return signPhoto;
	}

	public void setSignPhoto(String signPhoto) {
		this.signPhoto = signPhoto;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	
	/**
	 * @return the flrcId
	 */
	public String getFlrcId() {
		return flrcId;
	}

	/**
	 * @param flrcId the flrcId to set
	 */
	public void setFlrcId(String flrcId) {
		this.flrcId = flrcId;
	}


	
	
}
