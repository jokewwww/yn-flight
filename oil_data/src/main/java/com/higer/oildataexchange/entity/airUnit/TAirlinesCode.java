package com.higer.oildataexchange.entity.airUnit;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "T_AIRLINES_CODE")
@Entity
@Data
public class TAirlinesCode implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 航空公司二字码
     */
    @Id
    @Column(insertable = false, name = "alcd_icao_code", nullable = false)
    private String alcdIcaoCode;

    /**
     * 航空公司全称
     */
    @Column(name = "alcd_arln_name")
    private String alcdArlnName;

    /**
     * 航空公司简称
     */
    @Column(name = "alcd_arln_name_s")
    private String alcdArlnNameS;

    /**
     * 内航外航 0 内 1外
     */
    @Column(name = "alcd_arln_nw")
    private Integer alcdArlnNw;

    /**
     * 航空公司简称
     */
    @Column(name = "cstm_num")
    private String cstmNum;

}