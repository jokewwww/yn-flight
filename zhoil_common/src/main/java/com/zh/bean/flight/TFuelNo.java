package com.zh.bean.flight;

public class TFuelNo {
    private String id;

    private String flrcNo;

    private Integer fuelType;

    private Integer status;

    private String remark;

    private String note;

    private String staffId;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlrcNo() {
        return flrcNo;
    }

    public void setFlrcNo(String flrcNo) {
        this.flrcNo = flrcNo;
    }

    public Integer getFuelType() {
        return fuelType;
    }

    public void setFuelType(Integer fuelType) {
        this.fuelType = fuelType;
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
        this.remark = remark == null ? null : remark.trim();
    }

    public TFuelNo(String id, String flrcNo, Integer fuelType, Integer status, String remark) {
        this.id = id;
        this.flrcNo = flrcNo;
        this.fuelType = fuelType;
        this.status = status;
        this.remark = remark;
    }
    public TFuelNo(){}
}