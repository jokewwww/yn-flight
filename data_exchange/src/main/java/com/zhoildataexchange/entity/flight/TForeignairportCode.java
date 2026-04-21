package com.zhoildataexchange.entity.flight;

import javax.persistence.*;
import java.util.Objects;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/10/6 12:26
 * @Description:
 */
@Entity
@Table(name = "T_FOREIGNAIRPORT_CODE", schema = "flight_login", catalog = "")
public class TForeignairportCode {
    private String id;
    private String alcdIcaoCode;
    private String alcdArlnName;
    private String cstmNum;

    @Id
    @Column(name = "id", nullable = false, length = 8)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "alcd_icao_code", nullable = true, length = 8)
    public String getAlcdIcaoCode() {
        return alcdIcaoCode;
    }

    public void setAlcdIcaoCode(String alcdIcaoCode) {
        this.alcdIcaoCode = alcdIcaoCode;
    }

    @Basic
    @Column(name = "alcd_arln_name", nullable = true, length = 64)
    public String getAlcdArlnName() {
        return alcdArlnName;
    }

    public void setAlcdArlnName(String alcdArlnName) {
        this.alcdArlnName = alcdArlnName;
    }

    @Basic
    @Column(name = "cstm_num", nullable = true, length = 20)
    public String getCstmNum() {
        return cstmNum;
    }

    public void setCstmNum(String cstmNum) {
        this.cstmNum = cstmNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TForeignairportCode that = (TForeignairportCode) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(alcdIcaoCode, that.alcdIcaoCode) &&
                Objects.equals(alcdArlnName, that.alcdArlnName) &&
                Objects.equals(cstmNum, that.cstmNum);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, alcdIcaoCode, alcdArlnName, cstmNum);
    }
}
