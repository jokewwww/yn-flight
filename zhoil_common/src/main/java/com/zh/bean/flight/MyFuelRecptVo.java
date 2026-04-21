package com.zh.bean.flight;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.zh.format.YYYYMMDD;
import com.zh.format.YYYYMMDD_HHMM;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 油单表
 * T_FUEL_RECPT
 */
public class MyFuelRecptVo implements Serializable {
    /**
     * T_FUEL_RECPT
     */
    private static final long serialVersionUID = 1L;

    /**
     * 加油日期
     */
    @JsonDeserialize(using = YYYYMMDD.class)
    private Date flrcDate;

    private String flrcAirlName;
    /**
     * 机场三字码
     */
    private String apcdIataCode;

    /**
     * 航班号
     */
    private String flrcFlightNo;

    /**
     * 飞机号码
     */
    private String flrcAircrftNo;

    /**
     * 飞机类型
     */
    private String flrcAircrftType;

    /**
     * 体积（升）
     */
    private BigDecimal flrcFuelVol;

    /**
     * 加油质量（千克）
     */
    private BigDecimal flrcQuantity;

    /**
     * 保税类型
     */
    private String  flrcBwtar;

    /**
     * 油单号
     */
    private String flrcNo;

    /**
     * 航线（中文地名-中文地名（-中文地名））
     */
    private String flgtVialc;

    /**
     * 航线简称
     */
    private String flightValic;

    /**
     * 保税类型
     */
    private String  arcrCustomNum;

    /**
     * 加油开始时间
     */
    @JsonDeserialize(using = YYYYMMDD_HHMM.class)
    private Date flrcStatTime;

    public String getArcrCustomNum() {
        return arcrCustomNum;
    }

    public void setArcrCustomNum(String arcrCustomNum) {
        this.arcrCustomNum = arcrCustomNum;
    }

    public Date getFlrcDate() {
        return flrcDate;
    }

    public void setFlrcDate(Date flrcDate) {
        this.flrcDate = flrcDate;
    }

    public String getApcdIataCode() {
        return apcdIataCode;
    }

    public void setApcdIataCode(String apcdIataCode) {
        this.apcdIataCode = apcdIataCode;
    }

    public String getFlrcFlightNo() {
        return flrcFlightNo;
    }

    public void setFlrcFlightNo(String flrcFlightNo) {
        this.flrcFlightNo = flrcFlightNo;
    }

    public String getFlrcAircrftNo() {
        return flrcAircrftNo;
    }

    public void setFlrcAircrftNo(String flrcAircrftNo) {
        this.flrcAircrftNo = flrcAircrftNo;
    }

    public String getFlrcAircrftType() {
        return flrcAircrftType;
    }

    public void setFlrcAircrftType(String flrcAircrftType) {
        this.flrcAircrftType = flrcAircrftType;
    }

    public BigDecimal getFlrcFuelVol() {
        return flrcFuelVol;
    }

    public void setFlrcFuelVol(BigDecimal flrcFuelVol) {
        this.flrcFuelVol = flrcFuelVol;
    }

    public BigDecimal getFlrcQuantity() {
        return flrcQuantity;
    }

    public void setFlrcQuantity(BigDecimal flrcQuantity) {
        this.flrcQuantity = flrcQuantity;
    }

    public Date getFlrcStatTime() {
        return flrcStatTime;
    }

    public void setFlrcStatTime(Date flrcStatTime) {
        this.flrcStatTime = flrcStatTime;
    }

    public String getFlrcAirlName() {
        return flrcAirlName;
    }

    public void setFlrcAirlName(String flrcAirlName) {
        this.flrcAirlName = flrcAirlName;
    }

    public String getFlrcBwtar() {
        return flrcBwtar;
    }

    public void setFlrcBwtar(String flrcBwtar) {
        this.flrcBwtar = flrcBwtar;
    }

    public String getFlrcNo() {
        return flrcNo;
    }

    public void setFlrcNo(String flrcNo) {
        this.flrcNo = flrcNo;
    }

    public String getFlightValic() {
        return flightValic;
    }

    public void setFlightValic(String flightValic) {
        this.flightValic = flightValic;
    }
}