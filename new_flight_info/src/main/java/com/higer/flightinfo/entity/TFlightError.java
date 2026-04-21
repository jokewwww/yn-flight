package com.higer.flightinfo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name="tflight_error")
@Entity
@Data
public class TFlightError {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 失败时间
     */
    @Column(name="error_time")
    private Date errorTime;

    /**
     * 失败原因
     */
    @Column(name="error_reason")
    private String errorReason;

    /**
     * 失败数据
     */
    @Column(name="error_data")
    private String errorData;

    /**
     * 状态
     */
    @Column(name="status")
    private Integer status;
}
