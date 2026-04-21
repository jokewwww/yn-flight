package com.zh.service.impl;

import com.zh.bean.flight.FlowInfo;
import com.zh.dao.mapper.my.FlowInfoMapper;
import com.zh.service.FlowInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/9/13 11:26
 * @Description:
 */
@Service
public class FlowInfoServiceImpl implements FlowInfoService {

    @Autowired
    private FlowInfoMapper flowInfoMapper;

    @Override
    public List<FlowInfo> getFlowInfo(FlowInfo flowInfo) {
        return flowInfoMapper.getFlowInfo(flowInfo);
    }

    @Override
    public FlowInfo addFlowInfo(FlowInfo flowInfo) {
//        FlowInfo one = flowInfoMapper.getOne(flowInfo);
//        if(ObjectUtil.isNotNull(one)){
//            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.TYPE_REPEAT, null));
//        }
        flowInfoMapper.addFlowInfo(flowInfo);
        return flowInfo;
    }

    @Override
    public int updateFlowInfo(FlowInfo flowInfo) {
//        FlowInfo one = flowInfoMapper.getOne(flowInfo);
//        if(ObjectUtil.isNotNull(one)){
//            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.TYPE_REPEAT, null));
//        }
        return flowInfoMapper.updateFlowInfo(flowInfo);
    }

    @Override
    public int deleteFlowInfo(FlowInfo flowInfo) {
        return flowInfoMapper.deleteFlowInfo(flowInfo.getId());
    }
}
