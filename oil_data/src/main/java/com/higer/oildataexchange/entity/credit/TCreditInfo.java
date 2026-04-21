package com.higer.oildataexchange.entity.credit;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "T_CREDIT_INFO")
@Entity
@Data
public class TCreditInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 必须，客户代码
     */
    @Id
    @Column(insertable = false, name = "cstno", nullable = false)
    private String cstno;

    /**
     * 必须，客户名称
     */
    @Column(name = "cstnm")
    private String cstnm;

    /**
     * 必须，客户类型（0-实时结算,1-赊销,2-预收款,3-金融方案）
     */
    @Column(name = "customer_type")
    private String customerType;

    /**
     * 必须，信用评级
     */
    @Column(name = "cilvl")
    private String cilvl;

    /**
     * 必须，信用状态：0-正常（默认）；1-提示（低风险）；2-警示（高风险）
     */
    @Column(name = "citst")
    private String citst;

    /**
     * 信用的描述信息
     */
    @Column(name = "cirmk")
    private String cirmk;


    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

}