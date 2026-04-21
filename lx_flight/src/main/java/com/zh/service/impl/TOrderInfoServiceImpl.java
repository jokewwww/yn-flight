package com.zh.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.TAirportCode;
import com.zh.bean.flight.TOrderInfo;
import com.zh.bean.login.MyStaff;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.MyTAirportCodeMapper;
import com.zh.dao.mapper.my.TOrderInfoMapper;
import com.zh.exception.CustomException;
import com.zh.service.TOrderInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class TOrderInfoServiceImpl implements TOrderInfoService {

    private final static Logger log = LoggerFactory.getLogger(TOrderInfoServiceImpl.class);
    @Autowired
    public TOrderInfoMapper orderInfoMapper;
    @Autowired
    private MyTAirportCodeMapper myTAirportCodeMapper;

    /**
     * 新建
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insertOrderInfo(TOrderInfo orderInfo) {
        System.out.println("----------------新增----------------------");
        System.out.println("订单号:" + orderInfo.getOrderNo() + "Json:" + JSON.toJSONString(orderInfo));

        // 插入
        int i = orderInfoMapper.insert(orderInfo);
        if (i != 1) {
            System.out.println("插入数据失败:返回值:" + i + "Json:" + JSON.toJSONString(orderInfo));
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
        }

        return 0;
    }

    @Override
    @Transactional
    public Integer updateOrderInfo(TOrderInfo orderInfo) {
        System.out.println("修改id" + orderInfo.getOrderId());
        System.out.println("订单号:" + orderInfo.getOrderNo() + "Json:" + JSON.toJSONString(orderInfo));
        try {
            return orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询
     */
    @Override
    public List<TOrderInfo> selectOrderInfo() {
        return orderInfoMapper.selectOrderInfo();
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void deleteOrderInfo(TOrderInfo orderInfo) {
        if (orderInfoMapper.deleteByPrimaryKey(orderInfo.getOrderId()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    @Override
    public List<TOrderInfo> selectOrderInfoByAir(MyStaff staff) {
        TAirportCode airportCode = myTAirportCodeMapper.selectByCnafAirportCode(staff.getLoginUserIn().getStaffAirportCode());
        if (airportCode != null && StringUtils.isNotEmpty(airportCode.getApcdIataCode())) {
            List<TOrderInfo> list = orderInfoMapper.selectOrderInfoByAir(airportCode.getApcdIataCode());
            list.forEach(dto -> {
                if (ObjectUtil.equal(dto.getAddoilType(), "0")) {
                    dto.setAddoilType("机坪加注");
                } else if (ObjectUtil.equal(dto.getAddoilType(), "1")) {
                    dto.setAddoilType("自提");
                } else if (ObjectUtil.equal(dto.getAddoilType(), "2")) {
                    dto.setAddoilType("油品配送");
                } else if (ObjectUtil.equal(dto.getAddoilType(), "3")) {
                    dto.setAddoilType("野外保障加油");
                } else if (ObjectUtil.equal(dto.getAddoilType(), "4")) {
                    dto.setAddoilType("自助加油");
                } else if (ObjectUtil.equal(dto.getAddoilType(), "5")) {
                    dto.setAddoilType("外航临时来华");
                }
            });
            return list;
        } else {
            return null;
        }
    }
}
