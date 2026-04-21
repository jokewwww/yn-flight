package com.zh.bean.flight;

import java.io.Serializable;
import java.util.List;

/**
 * 机场代码表
 * T_AIRPORT_CODE
 */
public class MyAirportCode implements Serializable {
	
	/**
	 * T_AIRPORT_CODE
	 */
	private static final long serialVersionUID = 1L;
	
    /**
     * 机场三字码
     */
    private String apcdIataCode;

    /**
     * 机场四字码
     */
    private String apcdIcaoCode;

    /**
     * 机场名全称
     */
    private String apcdAirportName;

    /**
     * 机场名简称
     */
    private String apcdAirportNameS;

    /**
     * 中航油内部机场编号
     */
    private String apcdCnafAirportCode;
    
    /**
     * 使用接口协议（01：航二所接口，02：云南接口，03：昆明机场接口，04：新疆机场接口）
     */
    private String apcdProtocol;
    
    /**
     * 机场属性（D：国内，I：国际，R：地区）
     */
    private String apcdAirportProp;
    
    /**
     * 销售组织
     */
    private String apcdVkorg;

    /**
     * 分销渠道
     */
    private String apcdVtweg;

    /**
     * 评估类型
     */
    private String apcdBwtar;

    /**
     * 油库（工厂）
     */
    private String apcdWerks;

    /**
     * 销售办事处
     */
    private String apcdVkbur;

    /**
     * 销售组
     */
    private String apcdVkgrp;

    /**
     * 组织单位
     */
    private Integer apcdOrgeh;

    /**
     * 人事范围
     */
    private String apcdPersa;

    /**
     * 人事子范围
     */
    private String apcdBtrtl;


    /**
     * 代码标识
     */
    private String airportCodeId;
    
    /**
     * 中航油内部机场编号
     */
    public String getApcdCnafAirportCode() {
		return apcdCnafAirportCode;
	}
    /**
     * 中航油内部机场编号
     */
	public void setApcdCnafAirportCode(String apcdCnafAirportCode) {
		this.apcdCnafAirportCode = apcdCnafAirportCode;
	}
	/**
     * 使用接口协议（01：航二所接口，02：云南接口，03：昆明机场接口，04：新疆机场接口）
     */
	public String getApcdProtocol() {
		return apcdProtocol;
	}
	/**
     * 使用接口协议（01：航二所接口，02：云南接口，03：昆明机场接口，04：新疆机场接口）
     */
	public void setApcdProtocol(String apcdProtocol) {
		this.apcdProtocol = apcdProtocol;
	}

	/**
     * 机场三字码
     * @return apcd_iata_code 机场三字码
     */
    public String getApcdIataCode() {
        return apcdIataCode;
    }

    /**
     * 机场三字码
     * @param apcdIataCode 机场三字码
     */
    public void setApcdIataCode(String apcdIataCode) {
        this.apcdIataCode = apcdIataCode == null ? null : apcdIataCode.trim();
    }

    /**
     * 机场四字码
     * @return apcd_icao_code 机场四字码
     */
    public String getApcdIcaoCode() {
        return apcdIcaoCode;
    }

    /**
     * 机场四字码
     * @param apcdIcaoCode 机场四字码
     */
    public void setApcdIcaoCode(String apcdIcaoCode) {
        this.apcdIcaoCode = apcdIcaoCode == null ? null : apcdIcaoCode.trim();
    }

    /**
     * 机场名全称
     * @return apcd_airport_name 机场名全称
     */
    public String getApcdAirportName() {
        return apcdAirportName;
    }

    /**
     * 机场名全称
     * @param apcdAirportName 机场名全称
     */
    public void setApcdAirportName(String apcdAirportName) {
        this.apcdAirportName = apcdAirportName == null ? null : apcdAirportName.trim();
    }

    /**
     * 机场名简称
     * @return apcd_airport_name_s 机场名简称
     */
    public String getApcdAirportNameS() {
        return apcdAirportNameS;
    }

    /**
     * 机场名简称
     * @param apcdAirportNameS 机场名简称
     */
    public void setApcdAirportNameS(String apcdAirportNameS) {
        this.apcdAirportNameS = apcdAirportNameS == null ? null : apcdAirportNameS.trim();
    }
    /**
     * 机场属性（D：国内，I：国际，R：地区）
     * @return
     */
	public String getApcdAirportProp() {
		return apcdAirportProp;
	}
	/**
	 * 机场属性（D：国内，I：国际，R：地区）
	 * @param apcdAirportProp
	 */
	public void setApcdAirportProp(String apcdAirportProp) {
		this.apcdAirportProp = apcdAirportProp;
	}
    
    /**
     * 销售组织
     * @return apcd_vkorg 销售组织
     */
    public String getApcdVkorg() {
        return apcdVkorg;
    }

    /**
     * 销售组织
     * @param apcdVkorg 销售组织
     */
    public void setApcdVkorg(String apcdVkorg) {
        this.apcdVkorg = apcdVkorg == null ? null : apcdVkorg.trim();
    }

    /**
     * 分销渠道
     * @return apcd_vtweg 分销渠道
     */
    public String getApcdVtweg() {
        return apcdVtweg;
    }

    /**
     * 分销渠道
     * @param apcdVtweg 分销渠道
     */
    public void setApcdVtweg(String apcdVtweg) {
        this.apcdVtweg = apcdVtweg == null ? null : apcdVtweg.trim();
    }

    /**
     * 评估类型
     * @return apcd_bwtar 评估类型
     */
    public String getApcdBwtar() {
        return apcdBwtar;
    }

    /**
     * 评估类型
     * @param apcdBwtar 评估类型
     */
    public void setApcdBwtar(String apcdBwtar) {
        this.apcdBwtar = apcdBwtar == null ? null : apcdBwtar.trim();
    }

    /**
     * 油库（工厂）
     * @return apcd_werks 油库（工厂）
     */
    public String getApcdWerks() {
        return apcdWerks;
    }

    /**
     * 油库（工厂）
     * @param apcdWerks 油库（工厂）
     */
    public void setApcdWerks(String apcdWerks) {
        this.apcdWerks = apcdWerks == null ? null : apcdWerks.trim();
    }

    /**
     * 销售办事处
     * @return apcd_vkbur 销售办事处
     */
    public String getApcdVkbur() {
        return apcdVkbur;
    }

    /**
     * 销售办事处
     * @param apcdVkbur 销售办事处
     */
    public void setApcdVkbur(String apcdVkbur) {
        this.apcdVkbur = apcdVkbur == null ? null : apcdVkbur.trim();
    }

    /**
     * 销售组
     * @return apcd_vkgrp 销售组
     */
    public String getApcdVkgrp() {
        return apcdVkgrp;
    }

    /**
     * 销售组
     * @param apcdVkgrp 销售组
     */
    public void setApcdVkgrp(String apcdVkgrp) {
        this.apcdVkgrp = apcdVkgrp == null ? null : apcdVkgrp.trim();
    }

    /**
     * 组织单位
     * @return apcd_orgeh 组织单位
     */
    public Integer getApcdOrgeh() {
        return apcdOrgeh;
    }

    /**
     * 组织单位
     * @param apcdOrgeh 组织单位
     */
    public void setApcdOrgeh(Integer apcdOrgeh) {
        this.apcdOrgeh = apcdOrgeh;
    }

    /**
     * 人事范围
     * @return apcd_persa 人事范围
     */
    public String getApcdPersa() {
        return apcdPersa;
    }

    /**
     * 人事范围
     * @param apcdPersa 人事范围
     */
    public void setApcdPersa(String apcdPersa) {
        this.apcdPersa = apcdPersa == null ? null : apcdPersa.trim();
    }

    /**
     * 人事子范围
     * @return apcd_btrtl 人事子范围
     */
    public String getApcdBtrtl() {
        return apcdBtrtl;
    }

    /**
     * 人事子范围
     * @param apcdBtrtl 人事子范围
     */
    public void setApcdBtrtl(String apcdBtrtl) {
        this.apcdBtrtl = apcdBtrtl == null ? null : apcdBtrtl.trim();
    }


    public String getAirportCodeId() {
        return airportCodeId;
    }

    public void setAirportCodeId(String airportCodeId) {
        this.airportCodeId = airportCodeId;
    }
}