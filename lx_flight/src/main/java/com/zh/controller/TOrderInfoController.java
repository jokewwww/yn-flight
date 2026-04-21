package com.zh.controller;


import com.alibaba.fastjson.JSON;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.TOrderInfo;
import com.zh.bean.login.MyStaff;
import com.zh.constant.Constant;

import com.zh.service.TOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 */
@RestController
@RequestMapping(value = "/tOrderInfoController")
public class TOrderInfoController extends BaseController {

    @Autowired
    private TOrderInfoService orderInfoService;

    /**
     * 查询
     */
    @PostMapping(value = "/selectOrderInfo")
    public ReturnMsg<List<TOrderInfo>> selectOrderInfo() {
        List<TOrderInfo> orderInfo = orderInfoService.selectOrderInfo();
        return new ReturnMsg<List<TOrderInfo>>(Constant.CODE_OK, null, orderInfo);
    }

    @PostMapping(value = "/insertOrderInfo")
    public ReturnMsg<TOrderInfo> insertOrderInfo(@RequestBody TOrderInfo orderInfo) {
        System.out.println("新建--orderInfo---" + JSON.toJSONString(orderInfo));
        Integer integer = orderInfoService.insertOrderInfo(orderInfo);
        if (integer == 1) {
            return new ReturnMsg<TOrderInfo>(Constant.CODE_OK, "订单已经新增", null);
        }
        return new ReturnMsg<TOrderInfo>(Constant.CODE_OK, null, null);
    }

    @PostMapping(value = "/updateOrderInfo")
    public ReturnMsg<TOrderInfo> updateOrderInfo(
            @RequestBody TOrderInfo orderInfo) {
        // 修改
        Integer integer = orderInfoService.updateOrderInfo(orderInfo);
        if (integer == 1) {
            return new ReturnMsg<TOrderInfo>(Constant.CODE_OK, "修改成功", null);
        }
        return new ReturnMsg<TOrderInfo>(Constant.CODE_OK, null, null);
    }

    /**
     * 删除
     */
    @DeleteMapping(value = "/deleteOrderInfo")
    public ReturnMsg<TOrderInfo> deleteOrderInfo(@RequestBody TOrderInfo orderInfo) {
        orderInfoService.deleteOrderInfo(orderInfo);
        return new ReturnMsg<TOrderInfo>(Constant.CODE_OK, null, null);

    }

    /**
     * 查询当前机场
     */
    @PostMapping(value = "/selectOrderInfoByAir")
    public ReturnMsg<List<TOrderInfo>> selectOrderInfoByAir(@RequestBody MyStaff staff) {
        List<TOrderInfo> orderInfo = orderInfoService.selectOrderInfoByAir(staff);
        return new ReturnMsg<List<TOrderInfo>>(Constant.CODE_OK, null, orderInfo);
    }
}
