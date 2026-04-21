package com.zh.bean.flight;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class MyTFlightInfo {
    private Long id;

    private String flgtFlno;

    private String flgtRegn;

    private String flgtFlti;

    private String flgtDes3c;

    private String flgtDesnm;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss")
    private Date flgtDEtot;

    private String flgtPlacecode;

    private Integer isAuto;

    private String times;

    private Integer isEffective;

    private String flgtAirportCode;

    private String flgtAcname;

    public String getFlgtAcname() {
        return flgtAcname;
    }

    public void setFlgtAcname(String flgtAcname) {
        this.flgtAcname = flgtAcname;
    }

    public String getFlgtAirportCode() {
        return flgtAirportCode;
    }

    public void setFlgtAirportCode(String flgtAirportCode) {
        this.flgtAirportCode = flgtAirportCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlgtFlno() {
        return flgtFlno;
    }

    public void setFlgtFlno(String flgtFlno) {
        this.flgtFlno = flgtFlno == null ? null : flgtFlno.trim();
    }

    public String getFlgtRegn() {
        return flgtRegn;
    }

    public void setFlgtRegn(String flgtRegn) {
        this.flgtRegn = flgtRegn == null ? null : flgtRegn.trim();
    }

    public String getFlgtFlti() {
        return flgtFlti;
    }

    public void setFlgtFlti(String flgtFlti) {
        this.flgtFlti = flgtFlti == null ? null : flgtFlti.trim();
    }

    public String getFlgtDes3c() {
        return flgtDes3c;
    }

    public void setFlgtDes3c(String flgtDes3c) {
        this.flgtDes3c = flgtDes3c == null ? null : flgtDes3c.trim();
    }

    public String getFlgtDesnm() {
        return flgtDesnm;
    }

    public void setFlgtDesnm(String flgtDesnm) {
        this.flgtDesnm = flgtDesnm == null ? null : flgtDesnm.trim();
    }

    public Date getFlgtDEtot() {
        return flgtDEtot;
    }

    public void setFlgtDEtot(Date flgtDEtot) {
        this.flgtDEtot = flgtDEtot;
    }

    public String getFlgtPlacecode() {
        return flgtPlacecode;
    }

    public void setFlgtPlacecode(String flgtPlacecode) {
        this.flgtPlacecode = flgtPlacecode == null ? null : flgtPlacecode.trim();
    }

    public Integer getIsAuto() {
        return isAuto;
    }

    public void setIsAuto(Integer isAuto) {
        this.isAuto = isAuto;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times == null ? null : times.trim();
    }

    public Integer getIsEffective() {
        return isEffective;
    }

    public void setIsEffective(Integer isEffective) {
        this.isEffective = isEffective;
    }
}