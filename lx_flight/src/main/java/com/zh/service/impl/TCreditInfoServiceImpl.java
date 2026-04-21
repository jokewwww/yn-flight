package com.zh.service.impl;

import com.alibaba.fastjson.JSON;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.TCreditInfo;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.TCreditInfoMapper;
import com.zh.exception.CustomException;
import com.zh.service.TCreditInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class TCreditInfoServiceImpl implements TCreditInfoService {

    private final static Logger log = LoggerFactory.getLogger(TCreditInfoServiceImpl.class);
    @Autowired
    private TCreditInfoMapper creditInfoMapper;

    /**
     * 新建
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insertCreditInfo(TCreditInfo creditInfo) {
        System.out.println("----------------新增----------------------");
        System.out.println("客户代码:" + creditInfo.getCstno() + "Json:" + JSON.toJSONString(creditInfo));
        if (creditInfo.getCreateDate() == null) {
            creditInfo.setCreateDate(new Date());
        }
        // 插入
        int i = creditInfoMapper.insertSelective(creditInfo);
        if (i != 1) {
            System.out.println("插入数据失败:返回值:" + i + "Json:" + JSON.toJSONString(creditInfo));
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
        }

        return 0;
    }

    @Override
    @Transactional
    public Integer updateCreditInfo(TCreditInfo creditInfo) {
        System.out.println("修改:客户代码" + creditInfo.getCstno());
        System.out.println("客户代码:" + creditInfo.getCstno() + "Json:" + JSON.toJSONString(creditInfo));
        try {
            return creditInfoMapper.updateByPrimaryKeySelective(creditInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询
     */
    @Override
    public List<TCreditInfo> selectCreditInfo(TCreditInfo creditInfo) {
        return creditInfoMapper.selectCreditInfo(creditInfo);
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCreditInfo(TCreditInfo creditInfo) {
        if (creditInfoMapper.deleteByPrimaryKey(creditInfo.getCstno()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

}
