package com.zh.service.impl;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyAirlinesCode;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.AirlinesCodeMapper;
import com.zh.exception.CustomException;
import com.zh.service.AirlinesCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AirlinesCodeServiceImpl implements AirlinesCodeService {

    @Autowired
    private AirlinesCodeMapper airlinesCodeMapper;

    /**
     * 航空公司增加
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public Integer insertAirlinesCode(MyAirlinesCode airlinesCode) {
        //添加前先根据航班公司二字码查询是否存在此信息，如果存在就不添加，不存在才添加
        MyAirlinesCode airlinesInfo = airlinesCodeMapper.selectAirlinesCodeFind(airlinesCode.getAlcdIcaoCode());
        if (airlinesInfo == null) {
            if (airlinesCodeMapper.insertAirlinesCode(airlinesCode) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
            }
            //如果已添加返回0
            return 0;
        } else {
            //如果已存在返回1
            return 1;
        }
    }

    /**
     * 修改航空公司
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void updateAirlinesCode(MyAirlinesCode airlinesCode) {
        if (airlinesCodeMapper.updateAirlinesCode(airlinesCode) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 删除航空公司
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void deleteAirlinesCode(MyAirlinesCode airlinesCode) {
        if (airlinesCodeMapper.deleteAirlinesCode(airlinesCode.getAlcdIcaoCode()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 查询航空公司
     */
    @Override
    public List<MyAirlinesCode> selectAirlinesCode() {
        return airlinesCodeMapper.selectAirlinesCode();
    }

    /**
     * 查询航空公司详情
     */
    @Override
    public MyAirlinesCode selectAirlinesCodeFind(MyAirlinesCode airlinesCode) {
        return airlinesCodeMapper.selectAirlinesCodeFind(airlinesCode.getAlcdIcaoCode());
    }

}
