package com.zh.bean.login;

import lombok.Data;

import java.io.Serializable;

/**
 * 人员不保障机型关系表
 * T_FLGT_TYPE_STAFF
 */
@Data
public class FlgtTypeStaff implements Serializable {

    /**
     * 主键
     */
    private int id;

    /**
     * 员工id
     */
    private String staffId;

    /**
     * 不保障机型id
     */
    private int flgtTypeId;

    /**
     * 机型
     */
    private String type;

    /**
     * 是否绑定1绑定0未绑定
     */
    private String bind;

}