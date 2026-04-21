package com.zh.entity;

import com.zh.bean.login.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * (TCustom)实体类
 *
 * @author wgh
 * @since 2024-04-10 09:52:22
 */
@Entity
@Table
@ApiModel(value = "TCustom对象", description = "")
public class TCustom extends BaseBean implements Serializable {
    private static final long serialVersionUID = 831455995852048473L;
    /**
     * 购货方编号
     */
    @Id
    @ApiModelProperty(value = "购货方编号", name = "cstmNum")
    private String cstmNum;
    /**
     * 购货方国家
     */
    private String cstmRegion;
    /**
     * 购货方名称
     */
    private String cstmName;

    private Integer paginate;

    private String queryString;

    private Integer pageSize;


    public String getCstmNum() {
        return cstmNum;
    }

    public void setCstmNum(String cstmNum) {
        this.cstmNum = cstmNum;
    }

    public String getCstmRegion() {
        return cstmRegion;
    }

    public void setCstmRegion(String cstmRegion) {
        this.cstmRegion = cstmRegion;
    }

    public String getCstmName() {
        return cstmName;
    }

    public void setCstmName(String cstmName) {
        this.cstmName = cstmName;
    }

    public Integer getPaginate() {
        return paginate;
    }

    public void setPaginate(Integer paginate) {
        this.paginate = paginate;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}

