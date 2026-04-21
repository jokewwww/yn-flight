package com.higer.oildataexchange.entity.flight;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "T_FLIGHT_CODE_HISTORY")
@Entity
@Data
public class TFlightCodeHistory implements Serializable {
    private static final long serialVersionUID = 1L;

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
    @Column(name = "arcr_regn", nullable = false)
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
}