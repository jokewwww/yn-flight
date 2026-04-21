package com.higer.read_kafka.entity.flight;

import javax.persistence.*;
import java.util.Objects;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/2/15 09:52
 * @Description:
 */
@Entity
@Table(name = "T_AIRLINES_CODE")
@NamedQuery(name = "TAirlinesCode.findAll", query = "SELECT a FROM TAirlinesCode a")
public class TAirlinesCode {
    private String alcdIcaoCode;
    private String alcdArlnName;
    private String alcdArlnNameS;
    private Integer alcdArlnNw;

    @Basic
    @Column(name = "alcd_arln_nw")
    public Integer getAlcdArlnNw() {
        return alcdArlnNw;
    }

    public void setAlcdArlnNw(Integer alcdArlnNw) {
        this.alcdArlnNw = alcdArlnNw;
    }

    @Id
    @Column(name = "alcd_icao_code")
    public String getAlcdIcaoCode() {
        return alcdIcaoCode;
    }

    public void setAlcdIcaoCode(String alcdIcaoCode) {
        this.alcdIcaoCode = alcdIcaoCode;
    }

    @Basic
    @Column(name = "alcd_arln_name")
    public String getAlcdArlnName() {
        return alcdArlnName;
    }

    public void setAlcdArlnName(String alcdArlnName) {
        this.alcdArlnName = alcdArlnName;
    }

    @Basic
    @Column(name = "alcd_arln_name_s")
    public String getAlcdArlnNameS() {
        return alcdArlnNameS;
    }

    public void setAlcdArlnNameS(String alcdArlnNameS) {
        this.alcdArlnNameS = alcdArlnNameS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TAirlinesCode that = (TAirlinesCode) o;
        return Objects.equals(alcdIcaoCode, that.alcdIcaoCode) &&
                Objects.equals(alcdArlnName, that.alcdArlnName) &&
                Objects.equals(alcdArlnNameS, that.alcdArlnNameS);
    }

    @Override
    public int hashCode() {

        return Objects.hash(alcdIcaoCode, alcdArlnName, alcdArlnNameS);
    }
}
