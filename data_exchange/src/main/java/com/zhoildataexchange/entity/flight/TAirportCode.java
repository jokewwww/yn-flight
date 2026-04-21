package com.zhoildataexchange.entity.flight;

import javax.persistence.*;
import java.util.Objects;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/2/14 13:23
 * @Description:
 */
@Entity
@Table(name = "T_AIRPORT_CODE")
@NamedQuery(name = "TAirportCode.findAll", query = "SELECT a FROM TAirportCode a")
public class TAirportCode {
    private String apcdIataCode;
    private String apcdIcaoCode;
    private String apcdAirportProp;
    private String apcdAirportName;
    private String apcdAirportNameS;
    private String apcdCnafAirportCode;
    private String apcdProtocol;

    @Id
    @Column(name = "apcd_iata_code")
    public String getApcdIataCode() {
        return apcdIataCode;
    }

    public void setApcdIataCode(String apcdIataCode) {
        this.apcdIataCode = apcdIataCode;
    }

    @Basic
    @Column(name = "apcd_icao_code")
    public String getApcdIcaoCode() {
        return apcdIcaoCode;
    }

    public void setApcdIcaoCode(String apcdIcaoCode) {
        this.apcdIcaoCode = apcdIcaoCode;
    }

    @Basic
    @Column(name = "apcd_airport_prop")
    public String getApcdAirportProp() {
        return apcdAirportProp;
    }

    public void setApcdAirportProp(String apcdAirportProp) {
        this.apcdAirportProp = apcdAirportProp;
    }

    @Basic
    @Column(name = "apcd_airport_name")
    public String getApcdAirportName() {
        return apcdAirportName;
    }

    public void setApcdAirportName(String apcdAirportName) {
        this.apcdAirportName = apcdAirportName;
    }

    @Basic
    @Column(name = "apcd_airport_name_s")
    public String getApcdAirportNameS() {
        return apcdAirportNameS;
    }

    public void setApcdAirportNameS(String apcdAirportNameS) {
        this.apcdAirportNameS = apcdAirportNameS;
    }

    @Basic
    @Column(name = "apcd_cnaf_airport_code")
    public String getApcdCnafAirportCode() {
        return apcdCnafAirportCode;
    }

    public void setApcdCnafAirportCode(String apcdCnafAirportCode) {
        this.apcdCnafAirportCode = apcdCnafAirportCode;
    }

    @Basic
    @Column(name = "apcd_protocol")
    public String getApcdProtocol() {
        return apcdProtocol;
    }

    public void setApcdProtocol(String apcdProtocol) {
        this.apcdProtocol = apcdProtocol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TAirportCode that = (TAirportCode) o;
        return Objects.equals(apcdIataCode, that.apcdIataCode) &&
                Objects.equals(apcdIcaoCode, that.apcdIcaoCode) &&
                Objects.equals(apcdAirportProp, that.apcdAirportProp) &&
                Objects.equals(apcdAirportName, that.apcdAirportName) &&
                Objects.equals(apcdAirportNameS, that.apcdAirportNameS) &&
                Objects.equals(apcdCnafAirportCode, that.apcdCnafAirportCode) &&
                Objects.equals(apcdProtocol, that.apcdProtocol);
    }

    @Override
    public int hashCode() {

        return Objects.hash(apcdIataCode, apcdIcaoCode, apcdAirportProp, apcdAirportName, apcdAirportNameS, apcdCnafAirportCode, apcdProtocol);
    }
}
