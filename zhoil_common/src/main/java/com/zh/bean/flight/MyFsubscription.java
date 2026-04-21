package com.zh.bean.flight;

import java.util.Date;

/**
 * 订阅航班表
 * @author 
 *
 */
public class MyFsubscription {
	/**
	 * 订阅航班号
	 */
	private String  flgtRflno;
	/**
	 * 订阅航班日期
	 */
	private String flgtRflop;
    /**
     * 订阅员工ID
     */
	private String staffRid;
	/**
	 * 所属机场代码
	 */
	private String flgtAirportRcode;
	/**
	 * 所属机场区域代码
	 */
	private String flgtAptareaRcode;
	public String getFlgtRflno() {
		return flgtRflno;
	}
	public void setFlgtRflno(String flgtRflno) {
		this.flgtRflno = flgtRflno;
	}
	public String getFlgtRflop() {
		return flgtRflop;
	}
	public void setFlgtRflop(String flgtRflop) {
		this.flgtRflop = flgtRflop;
	}
	public String getStaffRid() {
		return staffRid;
	}
	public void setStaffRid(String staffRid) {
		this.staffRid = staffRid;
	}
	public String getFlgtAirportRcode() {
		return flgtAirportRcode;
	}
	public void setFlgtAirportRcode(String flgtAirportRcode) {
		this.flgtAirportRcode = flgtAirportRcode;
	}
	public String getFlgtAptareaRcode() {
		return flgtAptareaRcode;
	}
	public void setFlgtAptareaRcode(String flgtAptareaRcode) {
		this.flgtAptareaRcode = flgtAptareaRcode;
	}
	

}
