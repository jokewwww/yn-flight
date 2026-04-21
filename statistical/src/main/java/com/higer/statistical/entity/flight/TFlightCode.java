package com.higer.statistical.entity.flight;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name="T_FLIGHT_CODE")
@NamedQuery(name = "TFlightCode.findAll", query = "SELECT a FROM TFlightCode a")
public class TFlightCode {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, name = "id", nullable = false)
    private Integer id;

    /**
     * 飞机号码
     */
    @Column( name = "arcr_regn", nullable = false)
    private String arcrRegn;

    /**
     * 飞机类型
     */
    @Column(name = "arcr_acname")
    private String arcrAcname;

    /**
     * 购货方编号
     */
    @Column(name = "arcr_custom_num")
    private String arcrCustomNum;

    /**
     * 开始日期
     */
    @Column(name = "arcr_start_date", nullable = false)
    private Date arcrStartDate;

    /**
     * 结束日期
     */
    @Column(name = "arcr_end_date")
    private Date arcrEndDate;


    /**
     * 指定的航班号
     */
    @Column(name = "flno")
    private String flno;

    /**
     * 删除标识 0-未删除 1-删除
     */
    @Column(name = "del_flag")
    private Integer delFlag;

    /**
     * 中航油飞机号码ID
     */
    @Column(name = "cid")
    private Integer cid;

    /**
     * 记录创建时间-中航油创建时间
     */
    @Column(name = "cnaf_create_time")
    private Date cnafCreateTime;

    /**
     * 记录更新时间-中航油更新时间
     */
    @Column(name = "cnaf_update_time")
    private Date cnafUpdateTime;

    /**
     * 记录创建时间-系统
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 记录更新时间-系统
     */
    @Column(name = "update_time")
    private Date updateTime;
}
