package com.zh.dao.mapper.my;

import com.zh.bean.flight.FlowInfoCertificate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FlowInfoCertificateMapper {

    /**
     * 查询流量计基本信息
     *
     * @param flowInfoCertificate
     * @return
     */
    List<FlowInfoCertificate> getFlowInfoCertificate(FlowInfoCertificate flowInfoCertificate);

    /**
     * 新增流量计基本信息
     *
     * @param flowInfoCertificate
     * @return
     */
    int addFlowInfoCertificate(FlowInfoCertificate flowInfoCertificate);

    FlowInfoCertificate getOne(FlowInfoCertificate flowInfoCertificate);

    /**
     * 修改流量计基本信息
     *
     * @param flowInfoCertificate
     * @return
     */
    int updateFlowInfoCertificate(FlowInfoCertificate flowInfoCertificate);

    /**
     * 删除流量计基本信息
     *
     * @param id
     * @return
     */
    int deleteFlowInfoCertificate(@Param("id") Integer id);


}
