package com.zh.service;

import com.zh.bean.flight.FlowInfoCertificate;

import java.util.List;

/**
 * @Auther:
 * @Date: 2021/9/13 11:25
 * @Description:
 */
public interface FlowInfoCertificateService {

    /**
     * 车型流量计基本信息
     *
     * @return
     */
    List<FlowInfoCertificate> getFlowInfoCertificate(FlowInfoCertificate flowInfoCertificate);

    /**
     * 新增车型
     *
     * @param flowInfoCertificate
     * @return
     */
    FlowInfoCertificate addFlowInfoCertificate(FlowInfoCertificate flowInfoCertificate);

    /**
     * 修改车型
     *
     * @param flowInfoCertificate
     * @return
     */
    int updateFlowInfoCertificate(FlowInfoCertificate flowInfoCertificate);

    /**
     * 删除车型
     *
     * @param flowInfoCertificate
     * @return
     */
    int deleteFlowInfoCertificate(FlowInfoCertificate flowInfoCertificate);

}
