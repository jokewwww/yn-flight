package com.zh.bean.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 车型绑定关系
 */
@Data
@ApiModel(value = "车型绑定关系入参")
public class VehiBind implements Serializable {

    /**
     * 绑定的关系
     */
    @ApiModelProperty(value = "绑定的关系(车型id)")
    private List<Integer> ids;

    /**
     * 机型id
     */
    @ApiModelProperty(value = "机型id")
    private Integer flgtTypeId;

}