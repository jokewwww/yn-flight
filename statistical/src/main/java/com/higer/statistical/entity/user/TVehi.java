package com.higer.statistical.entity.user;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "T_VEHI")
@NamedQuery(name = "TVehi.findAll", query = "SELECT a FROM TVehi a")
public class TVehi {

    @Id
    @Column(name = "vehi_id")
    private String vehiId;

    @Column(name = "vehi_no")
    private String vehiNo;

    @Column(name="vehi_nickname")
    private String vehiNickname;

    @Column(name="vehi_type")
    private String vehiType;

    @Column(name="vehi_plate_no")
    private String vehiPlateNo;

    @Column(name="vehi_brand")
    private String vehiBrand;

    @Column(name="vehi_availability")
    private Integer vehiAvailability;

    @Column(name="vehi_fuel_figu")
    private Integer vehiFuelFigu;

    @Column(name="vehi_company")
    private String vehiCompany;

    @Column(name="vehi_serv_station")
    private String vehiServStation;

    @Column(name="vehi_airport_code")
    private String vehiAirportCode;

    @Column(name="vehi_aptarea_code")
    private String vehiAptareaCode;

    @Column(name="vehi_height")
    private String vehiHeight;

    @Column(name="vehi_fuel_code")
    private String vehiFuelCode;

    @Column(name="vehi_fuel_sno")
    private String vehiFuelSno;
}
