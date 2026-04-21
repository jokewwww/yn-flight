package com.zh.service;

import com.zh.bean.ReturnMsg;
import com.zh.bean.login.TParam;
import com.zh.bean.login.TStaff;

import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/9/20 15:18
 * @Description:
 */
public interface TParamService {
    List<TParam> getAllTParam(TStaff staff);

    TParam saveTParam(TStaff staff, TParam tParam);

    TParam updateTParam(TStaff staff, TParam tParam);

    String deleteTParam(TStaff staff, TParam tParam);

    //PC 登录获取所有设置
    List<TParam> getAllByPcTparam(String staffAirportCode);

    ReturnMsg<List<TParam>> getOneTParam(TStaff staff);
}
