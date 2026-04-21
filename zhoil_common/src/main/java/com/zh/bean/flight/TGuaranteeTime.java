package com.zh.bean.flight;

public class TGuaranteeTime {
    private String id;

    private String regnType;

    private Integer numTime;

    private String flgtAirportCode;

    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getRegnType() {
        return regnType;
    }

    public void setRegnType(String regnType) {
        this.regnType = regnType == null ? null : regnType.trim();
    }

    public Integer getNumTime() {
        return numTime;
    }

    public void setNumTime(Integer numTime) {
        this.numTime = numTime;
    }

    public String getFlgtAirportCode() {
        return flgtAirportCode;
    }

    public void setFlgtAirportCode(String flgtAirportCode) {
        this.flgtAirportCode = flgtAirportCode == null ? null : flgtAirportCode.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}