package com.higer.oildataexchange.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "T_CUSTOM")
@Entity
@Data
public class TCustom implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 购货方编号
     */
    @Id
    @Column(insertable = false, name = "cstm_num", nullable = false)
    private String cstmNum;

    /**
     * 购货方国家
     */
    @Column(name = "cstm_region")
    private String cstmRegion;

    /**
     * 购货方名称
     */
    @Column(name = "cstm_name")
    private String cstmName;


}