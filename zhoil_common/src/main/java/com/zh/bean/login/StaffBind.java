package com.zh.bean.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 人员绑定关系
 */
@Data
@ApiModel(value = "人员绑定关系入参")
public class StaffBind implements Serializable {

    /**
     * 绑定的关系
     */
    @ApiModelProperty(value = "绑定的关系(车辆编号)")
    private List<String> ids;

    /**
     * 员工id
     */
    @ApiModelProperty(value = "员工id")
    private String staffId;

}