package com.zh.bean.login;

import lombok.Data;

import java.io.Serializable;

/**
 * 车型表
 * T_VEHI_TYPE
 */
@Data
public class VehiType implements Serializable {

    /**
     * 主键
     */
    private int id;


    /**
     * 车型
     */
    private String vehiType;

}