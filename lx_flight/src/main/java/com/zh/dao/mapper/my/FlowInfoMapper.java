package com.zh.dao.mapper.my;

import com.zh.bean.flight.FlowInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FlowInfoMapper {

    /**
     * 查询流量计基本信息
     *
     * @param flowInfo
     * @return
     */
    List<FlowInfo> getFlowInfo(FlowInfo flowInfo);

    FlowInfo getOne(FlowInfo flowInfo);

    /**
     * 新增流量计基本信息
     *
     * @param flowInfo
     * @return
     */
    int addFlowInfo(FlowInfo flowInfo);

    /**
     * 修改流量计基本信息
     *
     * @param flowInfo
     * @return
     */
    int updateFlowInfo(FlowInfo flowInfo);

    /**
     * 删除流量计基本信息
     *
     * @param id
     * @return
     */
    int deleteFlowInfo(@Param("id") Integer id);


}
