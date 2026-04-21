package com.zh.bean.flight;

import java.util.Date;

public class TFuelDistribution {
    private Integer id;

    /**
     * 人员id
     */
    private String staffId;

    /**
     * padid
     */
    private String padId;

    /**
     * 油单号
     */
    private String flrcNo;

    /**
     * 油单状态0未使用，1正在使用，2使用完成,3分配中
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 所属机场代码
     */
    private String flgtAirportCode;

    /**
     * 油单类型3国内，2离境，1外航
     */
    private Integer fuelType;

    private Date createDate;

    /**
     * 毕姐要的标识 默认为1提示,其它值都不提示
     */
    private Integer logo;

    public Integer getLogo() {
        return logo;
    }

    public void setLogo(Integer logo) {
        this.logo = logo;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getPadId() {
        return padId;
    }

    public void setPadId(String padId) {
        this.padId = padId;
    }

    public String getFlrcNo() {
        return flrcNo;
    }

    public void setFlrcNo(String flrcNo) {
        this.flrcNo = flrcNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFlgtAirportCode() {
        return flgtAirportCode;
    }

    public void setFlgtAirportCode(String flgtAirportCode) {
        this.flgtAirportCode = flgtAirportCode;
    }

    public Integer getFuelType() {
        return fuelType;
    }

    public void setFuelType(Integer fuelType) {
        this.fuelType = fuelType;
    }
}