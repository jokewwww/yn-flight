package com.zh.bean.flight;

import lombok.Data;

import java.io.Serializable;

/**
 *  流量计基本信息
 */
@Data
public class FlowInfo implements Serializable {

    /**
     *
     */
    private Integer id;

    /**
     * 流量计检验编号
     */
    private String code;

    /**
     * 型号
     */
    private String model;

    /**
     * 厂家
     */
    private String manufactor;
}