package com.zh.bean.flight;

import java.util.Date;

public class RefuelingTask {
	private  String id; //任务id
	private  String bizkey; //航班唯一标
	private  String filghtNo;//航班号拼接
	private  Date pulldownTime; //'2019-01-22 15:36:18', //任务下发时间
	private  Date  receviceTime; //'2019-01-22 15:36:24', //接受任务时间
	private  Date  placeTime; //'2019-01-22 15:36:28', //车辆到位时间
	private  Date  beginOilTime; //'2019-01-22 15:36:32', //开始加油时间
	private  Date endOilTime; //'2019-01-22 15:36:34', //结束加油时间
	private  Date completeTime; //'2019-01-22 15:36:37', //打印油单完成时间
	private  Date endTime; //'2019-01-22 15:36:41', //任务结束时间
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFilghtNo() {
		return filghtNo;
	}
	public void setFilghtNo(String filghtNo) {
		this.filghtNo = filghtNo;
	}
	public String getBizkey() {
		return bizkey;
	}
	public void setBizkey(String bizkey) {
		this.bizkey = bizkey;
	}
	public Date getPulldownTime() {
		return pulldownTime;
	}
	public void setPulldownTime(Date pulldownTime) {
		this.pulldownTime = pulldownTime;
	}
	public Date getReceviceTime() {
		return receviceTime;
	}
	public void setReceviceTime(Date receviceTime) {
		this.receviceTime = receviceTime;
	}
	public Date getPlaceTime() {
		return placeTime;
	}
	public void setPlaceTime(Date placeTime) {
		this.placeTime = placeTime;
	}
	public Date getBeginOilTime() {
		return beginOilTime;
	}
	public void setBeginOilTime(Date beginOilTime) {
		this.beginOilTime = beginOilTime;
	}
	public Date getEndOilTime() {
		return endOilTime;
	}
	public void setEndOilTime(Date endOilTime) {
		this.endOilTime = endOilTime;
	}
	public Date getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	
}
