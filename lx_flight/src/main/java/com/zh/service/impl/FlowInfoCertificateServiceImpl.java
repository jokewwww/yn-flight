package com.zh.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.FlowInfoCertificate;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.FlowInfoCertificateMapper;
import com.zh.exception.CustomException;
import com.zh.service.FlowInfoCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/9/13 11:26
 * @Description:
 */
@Service
public class FlowInfoCertificateServiceImpl implements FlowInfoCertificateService {

    @Autowired
    private FlowInfoCertificateMapper flowInfoCertificateMapper;

    @Override
    public List<FlowInfoCertificate> getFlowInfoCertificate(FlowInfoCertificate flowInfoCertificate) {
        return flowInfoCertificateMapper.getFlowInfoCertificate(flowInfoCertificate);
    }

    @Override
    public FlowInfoCertificate addFlowInfoCertificate(FlowInfoCertificate flowInfoCertificate) {
        FlowInfoCertificate one = flowInfoCertificateMapper.getOne(flowInfoCertificate);
        if (ObjectUtil.isNotNull(one)) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.CODE_REPEAT, null));
        }
        flowInfoCertificateMapper.addFlowInfoCertificate(flowInfoCertificate);
        return flowInfoCertificate;
    }

    @Override
    public int updateFlowInfoCertificate(FlowInfoCertificate flowInfoCertificate) {
        FlowInfoCertificate one = flowInfoCertificateMapper.getOne(flowInfoCertificate);
        if (ObjectUtil.isNotNull(one) && ObjectUtil.notEqual(one.getId(), flowInfoCertificate.getId())) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.CODE_REPEAT, null));
        }
        return flowInfoCertificateMapper.updateFlowInfoCertificate(flowInfoCertificate);
    }

    @Override
    public int deleteFlowInfoCertificate(FlowInfoCertificate flowInfoCertificate) {
        return flowInfoCertificateMapper.deleteFlowInfoCertificate(flowInfoCertificate.getId());
    }
}
