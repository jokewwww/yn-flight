package com.zh.service.impl;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.TForeignairportCode;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.TForeignairportCodeMapper;
import com.zh.exception.CustomException;
import com.zh.service.TForeignairPortCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @classname: TForeignairPortCodeServiceImpl
 * @author: zhaojiacan
 * @description: 外国航空公司信息维护
 * @date: 2023/5/15 16:08
 * @version:1.0
 */
@Service
public class TForeignairPortCodeServiceImpl implements TForeignairPortCodeService {
    @Autowired
    private TForeignairportCodeMapper foreignairportCodeMapper;

    /**
     * 功能描述：插入外国航空公司信息
     *
     * @param foreignairportCode
     * @return java.lang.Integer
     * @author zhaojiacan
     * @date 2023/5/15
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insert(TForeignairportCode foreignairportCode) {
        //添加前先根据航班公司二字码查询是否存在此信息，如果存在就不添加，不存在才添加
        TForeignairportCode tForeignairportCode = foreignairportCodeMapper.selectByAlcdIcaoCode(foreignairportCode.getAlcdIcaoCode());
        if (tForeignairportCode == null) {
            int maxId = foreignairportCodeMapper.getMaxId();
            foreignairportCode.setId(String.valueOf(maxId + 1));
            if (foreignairportCodeMapper.insert(foreignairportCode) != 1) {
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
     * 功能描述：更新外国航空公司信息
     *
     * @param foreignairportCode
     * @return void
     * @author zhaojiacan
     * @date 2023/5/15
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(TForeignairportCode foreignairportCode) {
        if (foreignairportCodeMapper.updateByPrimaryKeySelective(foreignairportCode) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 功能描述：根据主键ID删除外国航空公司信息
     *
     * @param foreignairportCode
     * @return void
     * @author zhaojiacan
     * @date 2023/5/15
     */
    @Override
    public void delete(TForeignairportCode foreignairportCode) {
        if (foreignairportCodeMapper.deleteByPrimaryKey(foreignairportCode.getId()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 功能描述：获取所有外国航空公司
     *
     * @param
     * @return java.util.List<com.zh.bean.flight.TForeignairportCode>
     * @author zhaojiacan
     * @date 2023/5/15
     */
    @Override
    public List<TForeignairportCode> selectList() {
        return foreignairportCodeMapper.selectAll();
    }

    /**
     * 功能描述：根据航空公司二字码获取航空公司详情
     *
     * @param foreignairportCode
     * @return com.zh.bean.flight.TForeignairportCode
     * @author zhaojiacan
     * @date 2023/5/15
     */
    @Override
    public TForeignairportCode selectByCode(TForeignairportCode foreignairportCode) {
        return foreignairportCodeMapper.selectByAlcdIcaoCode(foreignairportCode.getAlcdIcaoCode());
    }
}
