package com.higer.higerservice.entity.flight;

import javax.persistence.*;
import java.util.Objects;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/4/12 17:04
 * @Description:
 */
@Entity
@Table(name = "T_AIRLINES_CODE")
@NamedQuery(name = "TAirlinesCode.findAll", query = "SELECT a FROM TAirlinesCode a")
public class TAirlinesCode {
    private String alcdIcaoCode;
    private String alcdArlnName;
    private String alcdArlnNameS;

    @Id
    @Column(name = "alcd_icao_code", nullable = false, length = 2)
    public String getAlcdIcaoCode() {
        return alcdIcaoCode;
    }

    public void setAlcdIcaoCode(String alcdIcaoCode) {
        this.alcdIcaoCode = alcdIcaoCode;
    }

    @Basic
    @Column(name = "alcd_arln_name", nullable = true, length = 36)
    public String getAlcdArlnName() {
        return alcdArlnName;
    }

    public void setAlcdArlnName(String alcdArlnName) {
        this.alcdArlnName = alcdArlnName;
    }

    @Basic
    @Column(name = "alcd_arln_name_s", nullable = true, length = 10)
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
