package com.zh.service;

import com.zh.bean.flight.FlowInfo;

import java.util.List;

/**
 * @Auther:
 * @Date: 2021/9/13 11:25
 * @Description:
 */
public interface FlowInfoService {

    /**
     * 车型流量计基本信息
     *
     * @return
     */
    List<FlowInfo> getFlowInfo(FlowInfo flowInfo);

    /**
     * 新增车型
     *
     * @param flowInfo
     * @return
     */
    FlowInfo addFlowInfo(FlowInfo flowInfo);

    /**
     * 修改车型
     *
     * @param flowInfo
     * @return
     */
    int updateFlowInfo(FlowInfo flowInfo);

    /**
     * 删除车型
     *
     * @param flowInfo
     * @return
     */
    int deleteFlowInfo(FlowInfo flowInfo);

}
