package com.zh.bean.login;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

import java.io.Serializable;

/**
 * 机型类型表
 * T_FLGT_TYPE
 */
@Data
@Table
@Entity
public class FlgtType implements Serializable {

    /**
     * 主键
     */
    private Integer id;


    /**
     * 飞机类型
     */
    private String flgtAcname;

    /**
     * 报警时间
     */
    private String alarmTime;

    /**
     * 间隔时间
     */
    private String intervalTime;

}