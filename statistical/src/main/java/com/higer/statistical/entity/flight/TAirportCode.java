package com.higer.statistical.entity.flight;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name="T_AIRPORT_CODE")
@NamedQuery(name = "TAirportCode.findAll", query = "SELECT a FROM TAirportCode a")
public class TAirportCode implements Serializable {
    @Id
    @Column(name="apcd_iata_code")
    private String apcdIataCode;
    @Column(name="apcd_icao_code")
    private String apcdIcaoCode;
    @Column(name="apcd_airport_prop")
    private String apcdAirportProp;
    @Column(name="apcd_airport_name")
    private String apcdAirportName;
    @Column(name="apcd_airport_name_s")
    private String apcdAirportNameS;
    @Column(name="apcd_cnaf_airport_code")
    private String apcdCnafAirportCode;
    @Column(name="apcd_protocol")
    private String apcdProtocol;
    @Column(name="apcd_vkorg")
    private String apcdVkorg;
    @Column(name="apcd_vtweg")
    private String apcdVtweg;
    @Column(name="apcd_bwtar")
    private String apcdBwtar;
    @Column(name="apcd_werks")
    private String apcdWerks;
    @Column(name="apcd_vkbur")
    private String apcdVkbur;
    @Column(name="apcd_vkgrp")
    private String apcdVkgrp;
    @Column(name="apcd_orgeh")
    private Integer apcdOrgeh;
    @Column(name="apcd_persa")
    private String apcdPersa;
    @Column(name="apcd_btrtl")
    private String apcdBtrtl;

}
