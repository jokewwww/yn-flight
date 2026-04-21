package com.zh.bean.flight;

import java.io.Serializable;
import java.util.Date;

/**
 *  t_flight_alarm
 */
public class MyFlightAlarm implements Serializable {
    
	/**
     * 航班ID
     */
    private String flalId;

    /**
     * 闹钟创建员工ID
     */
    private String flalStaffId;
    
    /**
     * 设定字段（字段名）
     */
    private String flalSetCol;

    /**
     * 设定时间（分钟）
     */
    private Integer flalSetTime;

    /**
     * 闹钟创建日期
     */
    private Date flalCreDate;

    /**
     * 所属机场代码
     */
    private String flalAirportRcode;

    /**
     * 所属机场区域代码
     */
    private String flalAptareaRcode;

    /**
     * t_flight_alarm
     */
	private static final long serialVersionUID = 1L;
    
    /**
     * 航班ID
     * @return flal_id 航班ID
     */
    public String getFlalId() {
        return flalId;
    }

    /**
     * 航班ID
     * @param flalId 航班ID
     */
    public void setFlalId(String flalId) {
        this.flalId = flalId == null ? null : flalId.trim();
    }

    /**
     * 闹钟创建员工ID
     * @return flal_staff_id 闹钟创建员工ID
     */
    public String getFlalStaffId() {
        return flalStaffId;
    }

    /**
     * 闹钟创建员工ID
     * @param flalStaffId 闹钟创建员工ID
     */
    public void setFlalStaffId(String flalStaffId) {
        this.flalStaffId = flalStaffId == null ? null : flalStaffId.trim();
    }
    
    /**
     * 设定字段（字段名）
     * @return flal_set_col 设定字段（字段名）
     */
    public String getFlalSetCol() {
        return flalSetCol;
    }

    /**
     * 设定字段（字段名）
     * @param flalSetCol 设定字段（字段名）
     */
    public void setFlalSetCol(String flalSetCol) {
        this.flalSetCol = flalSetCol;
    }

    /**
     * 设定时间（分钟）
     * @return flal_set_time 设定时间（分钟）
     */
    public Integer getFlalSetTime() {
        return flalSetTime;
    }

    /**
     * 设定时间（分钟）
     * @param flalSetTime 设定时间（分钟）
     */
    public void setFlalSetTime(Integer flalSetTime) {
        this.flalSetTime = flalSetTime;
    }

    /**
     * 闹钟创建日期
     * @return flal_cre_date 闹钟创建日期
     */
    public Date getFlalCreDate() {
        return flalCreDate;
    }

    /**
     * 闹钟创建日期
     * @param flalCreDate 闹钟创建日期
     */
    public void setFlalCreDate(Date flalCreDate) {
        this.flalCreDate = flalCreDate;
    }

    /**
     * 所属机场代码
     * @return flal_airport_rcode 所属机场代码
     */
    public String getFlalAirportRcode() {
        return flalAirportRcode;
    }

    /**
     * 所属机场代码
     * @param flalAirportRcode 所属机场代码
     */
    public void setFlalAirportRcode(String flalAirportRcode) {
        this.flalAirportRcode = flalAirportRcode == null ? null : flalAirportRcode.trim();
    }

    /**
     * 所属机场区域代码
     * @return flal_aptarea_rcode 所属机场区域代码
     */
    public String getFlalAptareaRcode() {
        return flalAptareaRcode;
    }

    /**
     * 所属机场区域代码
     * @param flalAptareaRcode 所属机场区域代码
     */
    public void setFlalAptareaRcode(String flalAptareaRcode) {
        this.flalAptareaRcode = flalAptareaRcode == null ? null : flalAptareaRcode.trim();
    }
}