package com.higer.flightinfo.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

/**
 * 扩展属性
 */
@Data
@ToString(callSuper = true)
public class TFlightExtend extends TFlight {

    private String opration;//操作类型
    private Integer trsNum;//经停机场数量
    private String linkFfid;//关联航班ID
    private String sharedFfidStr;//共享航班号，/分割
    private String createTime;

    @JSONField(serialize = false)
    private String cacheId;
}
