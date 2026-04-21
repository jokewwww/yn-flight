package com.zh.bean.login;

import lombok.Data;

import java.io.Serializable;

/**
 * 车型不保障机型关系表
 * T_FLGT_TYPE_VEHI
 */
@Data
public class FlgtTypeVehi implements Serializable {

    /**
     * 主键
     */
    private int id;

    /**
     * 车型id
     */
    private int vehiTypeId;

    /**
     * 车型
     */
    private String vehiType;

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