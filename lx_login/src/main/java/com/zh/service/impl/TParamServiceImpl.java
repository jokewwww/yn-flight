package com.zh.service.impl;

import com.zh.bean.ReturnMsg;
import com.zh.bean.login.TParam;
import com.zh.bean.login.TStaff;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.TParamMapper;
import com.zh.exception.CustomException;
import com.zh.service.TParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/9/20 15:18
 * @Description:
 */
@Service
public class TParamServiceImpl implements TParamService {

    @Autowired
    private TParamMapper tParamMapper;

    @Override
    public List<TParam> getAllTParam(TStaff staff) {
        return tParamMapper.findAll(staff.getLoginUserIn().getStaffAirportCode());
    }

    @Override
    public TParam saveTParam(TStaff staff, TParam tParam) {
        try {
            Objects.requireNonNull(tParam, "新增实体不可为空");
            if (null != tParam.getId()) {
                return updateTParam(staff, tParam);
            }
            tParam.setFlgtAirportCode(staff.getLoginUserIn().getStaffAirportCode());
            int i = tParamMapper.insertSelective(tParam);
            if (i != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
            }
            return tParam;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public TParam updateTParam(TStaff staff, TParam tParam) {
        try {
            int i = tParamMapper.updateByPrimaryKeySelective(tParam);
            if (i != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tParam;
    }

    @Override
    public String deleteTParam(TStaff staff, TParam tParam) {
        Assert.notNull(tParam.getId(), "删除ID不可为空");
        int i = tParamMapper.deleteByPrimaryKey(tParam.getId());
        if (i != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
        return "success";
    }

    @Override
    public List<TParam> getAllByPcTparam(String staffAirportCode) {
        Assert.notNull(staffAirportCode, "staffAirportCode不可为空");
        return tParamMapper.findByPTypeAndFlgtAirportCode(4, staffAirportCode);
    }

    @Override
    public ReturnMsg<List<TParam>> getOneTParam(TStaff staff) {
        return null;
    }


}
