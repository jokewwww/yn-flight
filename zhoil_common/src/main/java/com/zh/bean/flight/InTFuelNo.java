package com.zh.bean.flight;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/10/22 10:08
 * @Description:
 */
public class InTFuelNo {

    private String flrcNo; // 开始油单号

    private Integer page;

    private Integer size;

    private Integer fuelType;

    private String endFlrcNo; // 结束油单号

    public String getEndFlrcNo() {
        return endFlrcNo;
    }

    public void setEndFlrcNo(String endFlrcNo) {
        this.endFlrcNo = endFlrcNo;
    }

    public Integer getFuelType() {
        return fuelType;
    }

    public void setFuelType(Integer fuelType) {
        this.fuelType = fuelType;
    }

    public String getFlrcNo() {
        return flrcNo;
    }

    public void setFlrcNo(String flrcNo) {
        this.flrcNo = flrcNo;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
