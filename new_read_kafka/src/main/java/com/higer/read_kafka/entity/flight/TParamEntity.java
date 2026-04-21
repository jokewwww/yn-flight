package com.higer.read_kafka.entity.flight;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "T_PARAM")
@NamedQuery(name = "TParamEntity.findAll", query = "SELECT a FROM TParamEntity a")
public class TParamEntity {
    private Integer id;
    private String pName;
    private String value1;
    private String value2;
    private String remark;
    private Integer pType;
    private String flgtAirportCode;

    @Id
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "p_name")
    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    @Basic
    @Column(name = "value1")
    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    @Basic
    @Column(name = "value2")
    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    @Basic
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "p_type")
    public Integer getpType() {
        return pType;
    }

    public void setpType(Integer pType) {
        this.pType = pType;
    }

    @Basic
    @Column(name = "flgt_airport_code")
    public String getFlgtAirportCode() {
        return flgtAirportCode;
    }

    public void setFlgtAirportCode(String flgtAirportCode) {
        this.flgtAirportCode = flgtAirportCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TParamEntity that = (TParamEntity) o;
        return id == that.id && Objects.equals(pName, that.pName) && Objects.equals(value1, that.value1) && Objects.equals(value2, that.value2) && Objects.equals(remark, that.remark) && Objects.equals(pType, that.pType) && Objects.equals(flgtAirportCode, that.flgtAirportCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pName, value1, value2, remark, pType, flgtAirportCode);
    }
}
