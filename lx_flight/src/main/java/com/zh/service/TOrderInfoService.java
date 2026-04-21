package com.zh.service;

import com.zh.bean.flight.TOrderInfo;
import com.zh.bean.login.MyStaff;

import java.util.List;


public interface TOrderInfoService {


    List<TOrderInfo> selectOrderInfo();

    /**
     * 新建
     *
     * @param orderInfo
     */
    Integer insertOrderInfo(TOrderInfo orderInfo);

    Integer updateOrderInfo(TOrderInfo orderInfo);

    void deleteOrderInfo(TOrderInfo orderInfo);

    List<TOrderInfo> selectOrderInfoByAir(MyStaff staff);
}
