package com.zh.bean.flight;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *  流量计基本信息
 */
@Data
public class FlowInfoCertificate implements Serializable {

    private Integer id;
    
    /**
     * 编号
     */
    private String code;

    /**
     * 厂家
     */
    private String manufactor;

    /**
     * 开始日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDate;

    /**
     * 结束日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endDate;
}