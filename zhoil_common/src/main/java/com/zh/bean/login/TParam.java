package com.zh.bean.login;

public class TParam {
    private Integer id;

    private String pName;

    private String value1;

    private String value2;

    private String remark;

    private Integer pType;

    private String flgtAirportCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName == null ? null : pName.trim();
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1 == null ? null : value1.trim();
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2 == null ? null : value2.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getpType() {
        return pType;
    }

    public void setpType(Integer pType) {
        this.pType = pType;
    }

    public String getFlgtAirportCode() {
        return flgtAirportCode;
    }

    public void setFlgtAirportCode(String flgtAirportCode) {
        this.flgtAirportCode = flgtAirportCode == null ? null : flgtAirportCode.trim();
    }
}