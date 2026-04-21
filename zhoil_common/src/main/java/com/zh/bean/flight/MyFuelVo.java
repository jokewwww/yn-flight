package com.zh.bean.flight;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zh.format.Decimal4bit;
import com.zh.format.YYYYMMDD_HHMMSS;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 油料表
 * T_FUEL
 */
public class MyFuelVo implements Serializable {


    /**
     * 油单日期
     */
    private String flrcDate;

    /**
     *  飞机号
     */
   
    private String flrcAircrftNo;

    /**
     * 航班号
     */
    private String flrcFlightNo;

    /**
     * 所属机场代码
     */
    private String flrcAirportCode;

    /**
     * 所属机场代码
     */
    private String adid;

    public String getFlrcDate() {
        return flrcDate;
    }

    public void setFlrcDate(String flrcDate) {
        this.flrcDate = flrcDate;
    }

    public String getFlrcAircrftNo() {
        return flrcAircrftNo;
    }

    public void setFlrcAircrftNo(String flrcAircrftNo) {
        this.flrcAircrftNo = flrcAircrftNo;
    }

    public String getFlrcFlightNo() {
        return flrcFlightNo;
    }

    public void setFlrcFlightNo(String flrcFlightNo) {
        this.flrcFlightNo = flrcFlightNo;
    }

    public String getFlrcAirportCode() {
        return flrcAirportCode;
    }

    public void setFlrcAirportCode(String flrcAirportCode) {
        this.flrcAirportCode = flrcAirportCode;
    }

    public String getAdid() {
        return adid;
    }

    public void setAdid(String adid) {
        this.adid = adid;
    }
}