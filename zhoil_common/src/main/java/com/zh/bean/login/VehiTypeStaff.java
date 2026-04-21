package com.zh.bean.login;

import lombok.Data;

import java.io.Serializable;

/**
 * 人员不准驾车型关系表
 * T_VEHI_TYPE_STAFF
 */
@Data
public class VehiTypeStaff implements Serializable {

    /**
     * 主键
     */
    private int id;

    /**
     * 员工id
     */
    private String staffId;

    /**
     * 不准驾车型id
     */
    private int vehiTypeId;

    /**
     * 车型
     */
    private String vehiType;

    /**
     * 车号
     */
    private String vehiNo;

    /**
     * 是否绑定1绑定0未绑定
     */
    private String bind;

}