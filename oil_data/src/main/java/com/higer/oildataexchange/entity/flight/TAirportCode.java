package com.higer.oildataexchange.entity.flight;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "T_AIRPORT_CODE")
@Entity
@Data
public class TAirportCode implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 机场三字码
     */
    @Id
    @Column(name = "apcd_iata_code", insertable = false, nullable = false)
    private String apcdIataCode;

    /**
     * 机场四字码
     */
    @Column(name = "apcd_icao_code")
    private String apcdIcaoCode;

    /**
     * 机场属性（D：国内，I：国际，R：地区）
     */
    @Column(name = "apcd_airport_prop")
    private String apcdAirportProp;

    /**
     * 机场名全称
     */
    @Column(name = "apcd_airport_name")
    private String apcdAirportName;

    /**
     * 机场名简称
     */
    @Column(name = "apcd_airport_name_s")
    private String apcdAirportNameS;

    /**
     * 中航油内部机场编号
     */
    @Column(name = "apcd_cnaf_airport_code")
    private String apcdCnafAirportCode;

    /**
     * 使用接口协议（01：航二所接口，02：云南接口，03：昆明机场接口，04：新疆机场接口）
     */
    @Column(name = "apcd_protocol")
    private String apcdProtocol;

    /**
     * 销售组织
     */
    @Column(name = "apcd_vkorg")
    private String apcdVkorg;

    /**
     * 分销渠道
     */
    @Column(name = "apcd_vtweg")
    private String apcdVtweg;

    /**
     * 评估类型
     */
    @Column(name = "apcd_bwtar")
    private String apcdBwtar;

    /**
     * 油库（工厂）
     */
    @Column(name = "apcd_werks")
    private String apcdWerks;

    /**
     * 销售办事处
     */
    @Column(name = "apcd_vkbur")
    private String apcdVkbur;

    /**
     * 销售组
     */
    @Column(name = "apcd_vkgrp")
    private String apcdVkgrp;

    /**
     * 组织单位
     */
    @Column(name = "apcd_orgeh")
    private Integer apcdOrgeh;

    /**
     * 人事范围
     */
    @Column(name = "apcd_persa")
    private String apcdPersa;

    /**
     * 人事子范围
     */
    @Column(name = "apcd_btrtl")
    private String apcdBtrtl;


}