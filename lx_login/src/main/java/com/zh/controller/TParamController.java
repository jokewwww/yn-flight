package com.zh.controller;

import com.zh.bean.ReturnMsg;
import com.zh.bean.login.TParam;
import com.zh.bean.login.TStaff;
import com.zh.constant.Constant;
import com.zh.service.TParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/9/20 15:17
 * @Description:
 */
@RestController
@RequestMapping(value = "/tParamController")
public class TParamController {

    @Autowired
    private TParamService tParamService;


    /**
     * 获取所有
     */
    @PostMapping(value = "/getAllTParam")
    public ReturnMsg<List<TParam>> getAllTParam(@RequestBody TStaff staff) {
        List<TParam> vehiAmountMap = tParamService.getAllTParam(staff);
        ReturnMsg<List<TParam>> msg = new ReturnMsg<List<TParam>>(Constant.CODE_OK, null, vehiAmountMap);
        return msg;
    }

    /**
     * 新增
     */
    @PostMapping(value = "/saveTParam")
    public ReturnMsg<TParam> saveTParam(@RequestBody TStaff staff, @RequestBody TParam tParam) {
        TParam vehiAmountMap = tParamService.saveTParam(staff, tParam);
        ReturnMsg<TParam> msg = new ReturnMsg<TParam>(Constant.CODE_OK, null, vehiAmountMap);
        return msg;
    }

    /* *//**
     * 修改
     *//*
    @PostMapping(value = "/updateTParam")
    public ReturnMsg<TParam> updateTParam(@RequestBody TStaff staff , @RequestBody TParam tParam) {
        TParam vehiAmountMap = tParamService.updateTParam(staff,tParam);
        ReturnMsg<TParam> msg = new ReturnMsg<TParam>(Constant.CODE_OK, null, vehiAmountMap);
        return msg;
    }*/

    /**
     * 删除
     */
    @PostMapping(value = "/deleteTParam")
    public ReturnMsg<String> deleteTParam(@RequestBody TStaff staff, @RequestBody TParam tParam) {
        String vehiAmountMap = tParamService.deleteTParam(staff, tParam);
        ReturnMsg<String> msg = new ReturnMsg<String>(Constant.CODE_OK, null, vehiAmountMap);
        return msg;
    }


    /**
     * 获取指定的参数
     */
    @PostMapping(value = "/getOneTParam")
    public ReturnMsg<List<TParam>> getOneTParam(@RequestBody TStaff staff) {
        return tParamService.getOneTParam(staff);
    }

}
