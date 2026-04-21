package com.higer.read_kafka.entity.flight;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name="T_CUSTOM")
@NamedQuery(name = "TCustom.findAll", query = "SELECT a FROM TCustom a")
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
