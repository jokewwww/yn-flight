package com.zh.bean.login;

import java.io.Serializable;

/**
 *  T_SALES_INFO（销售信息表）
 */
public class MySalesInfo implements Serializable {
    /**
     * 销售信息ID（UUID）
     */
    private String salesId;

    /**
     * 机场代码
     */
    private String salesAirportCode;

    /**
     * 机场区域代码
     */
    private String salesAptareaCode;

    /**
     * 销售组织
     */
    private String salesVkorg;

    /**
     * 分销渠道
     */
    private String salesVtweg;

    /**
     * 部门
     */
    private String salesVtdpt;

    /**
     * 评估类型（Y:保税，N:非保税）
     */
    private String salesBwtar;

    /**
     * 油库（工厂）
     */
    private String salesWerks;

    /**
     * 机场（装运点）
     */
    private String salesVstel;

    /**
     * 油罐（库存地点）
     */
    private String salesLgort;

    /**
     * 物料（油品规格）
     */
    private String salesMatnr;

    /**
     * 销售办事处
     */
    private String salesVkbur;

    /**
     * 销售组
     */
    private String salesVkgrp;

    /**
     * T_SALES_INFO
     */
    private static final long serialVersionUID = 1L;

    /**
     * 销售信息ID（UUID）
     * @return sales_id 销售信息ID（UUID）
     */
    public String getSalesId() {
        return salesId;
    }

    /**
     * 销售信息ID（UUID）
     * @param salesId 销售信息ID（UUID）
     */
    public void setSalesId(String salesId) {
        this.salesId = salesId == null ? null : salesId.trim();
    }

    /**
     * 机场代码
     * @return sales_airport_code 机场代码
     */
    public String getSalesAirportCode() {
        return salesAirportCode;
    }

    /**
     * 机场代码
     * @param salesAirportCode 机场代码
     */
    public void setSalesAirportCode(String salesAirportCode) {
        this.salesAirportCode = salesAirportCode == null ? null : salesAirportCode.trim();
    }

    /**
     * 机场区域代码
     * @return sales_aptarea_code 机场区域代码
     */
    public String getSalesAptareaCode() {
        return salesAptareaCode;
    }

    /**
     * 机场区域代码
     * @param salesAptareaCode 机场区域代码
     */
    public void setSalesAptareaCode(String salesAptareaCode) {
        this.salesAptareaCode = salesAptareaCode == null ? null : salesAptareaCode.trim();
    }

    /**
     * 销售组织
     * @return sales_vkorg 销售组织
     */
    public String getSalesVkorg() {
        return salesVkorg;
    }

    /**
     * 销售组织
     * @param salesVkorg 销售组织
     */
    public void setSalesVkorg(String salesVkorg) {
        this.salesVkorg = salesVkorg == null ? null : salesVkorg.trim();
    }

    /**
     * 分销渠道
     * @return sales_vtweg 分销渠道
     */
    public String getSalesVtweg() {
        return salesVtweg;
    }

    /**
     * 分销渠道
     * @param salesVtweg 分销渠道
     */
    public void setSalesVtweg(String salesVtweg) {
        this.salesVtweg = salesVtweg == null ? null : salesVtweg.trim();
    }

    /**
     * 部门
     * @return sales_vtdpt 部门
     */
    public String getSalesVtdpt() {
        return salesVtdpt;
    }

    /**
     * 部门
     * @param salesVtdpt 部门
     */
    public void setSalesVtdpt(String salesVtdpt) {
        this.salesVtdpt = salesVtdpt == null ? null : salesVtdpt.trim();
    }

    /**
     * 评估类型（Y:保税，N:非保税）
     * @return sales_bwtar 评估类型（Y:保税，N:非保税）
     */
    public String getSalesBwtar() {
        return salesBwtar;
    }

    /**
     * 评估类型（Y:保税，N:非保税）
     * @param salesBwtar 评估类型（Y:保税，N:非保税）
     */
    public void setSalesBwtar(String salesBwtar) {
        this.salesBwtar = salesBwtar == null ? null : salesBwtar.trim();
    }

    /**
     * 油库（工厂）
     * @return sales_werks 油库（工厂）
     */
    public String getSalesWerks() {
        return salesWerks;
    }

    /**
     * 油库（工厂）
     * @param salesWerks 油库（工厂）
     */
    public void setSalesWerks(String salesWerks) {
        this.salesWerks = salesWerks == null ? null : salesWerks.trim();
    }

    /**
     * 机场（装运点）
     * @return sales_vstel 机场（装运点）
     */
    public String getSalesVstel() {
        return salesVstel;
    }

    /**
     * 机场（装运点）
     * @param salesVstel 机场（装运点）
     */
    public void setSalesVstel(String salesVstel) {
        this.salesVstel = salesVstel == null ? null : salesVstel.trim();
    }

    /**
     * 油罐（库存地点）
     * @return sales_lgort 油罐（库存地点）
     */
    public String getSalesLgort() {
        return salesLgort;
    }

    /**
     * 油罐（库存地点）
     * @param salesLgort 油罐（库存地点）
     */
    public void setSalesLgort(String salesLgort) {
        this.salesLgort = salesLgort == null ? null : salesLgort.trim();
    }

    /**
     * 物料（油品规格）
     * @return sales_matnr 物料（油品规格）
     */
    public String getSalesMatnr() {
        return salesMatnr;
    }

    /**
     * 物料（油品规格）
     * @param salesMatnr 物料（油品规格）
     */
    public void setSalesMatnr(String salesMatnr) {
        this.salesMatnr = salesMatnr == null ? null : salesMatnr.trim();
    }

    /**
     * 销售办事处
     * @return sales_vkbur 销售办事处
     */
    public String getSalesVkbur() {
        return salesVkbur;
    }

    /**
     * 销售办事处
     * @param salesVkbur 销售办事处
     */
    public void setSalesVkbur(String salesVkbur) {
        this.salesVkbur = salesVkbur == null ? null : salesVkbur.trim();
    }

    /**
     * 销售组
     * @return sales_vkgrp 销售组
     */
    public String getSalesVkgrp() {
        return salesVkgrp;
    }

    /**
     * 销售组
     * @param salesVkgrp 销售组
     */
    public void setSalesVkgrp(String salesVkgrp) {
        this.salesVkgrp = salesVkgrp == null ? null : salesVkgrp.trim();
    }
}