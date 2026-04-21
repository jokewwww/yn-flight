package com.zh.controller;

import com.alibaba.fastjson.JSON;
import com.zh.bean.ResponseObject;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.InTFuelNo;
import com.zh.bean.flight.MyTask;
import com.zh.bean.flight.TFuelNo;
import com.zh.bean.login.MyStaff;
import com.zh.constant.Constant;
import com.zh.service.TFuelNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/9/13 13:44
 * @Description:
 */
@RestController
@RequestMapping(value = "/tFuelNoController")
public class TFuelNoController {

    @Autowired
    private TFuelNoService tFuelNoService;

    /**
     * 根据航班号查询预创建航班表
     */
    @PostMapping(value = "/getFuelNo")
    public ResponseObject<Object> getFlightTemp(@RequestBody MyStaff staff, @RequestBody InTFuelNo tFuelNo) {
        return tFuelNoService.selectAll(staff, tFuelNo);
    }

    /**
     * pad 获取 3 种不同的单号
     */
    @PostMapping(value = "/getFuelNoByType")
    public ReturnMsg<List<TFuelNo>> getFuelNoByType(@RequestBody MyStaff staff) {
        List<TFuelNo> tFuelNos = tFuelNoService.getFuelNoByType(staff);
        ReturnMsg<List<TFuelNo>> msg = new ReturnMsg<List<TFuelNo>>(Constant.CODE_OK, null, tFuelNos);
        return msg;
    }

    /**
     * pad 获取 3 种不同的单号
     */
    @PostMapping(value = "/getMaxFuelNo")
    public ReturnMsg<Map<Integer, String>> getMaxFuelNo(@RequestBody MyStaff staff) {
        Map<Integer, String> tFuelNos = tFuelNoService.getMaxFuelNo(staff);
        ReturnMsg<Map<Integer, String>> msg = new ReturnMsg<Map<Integer, String>>(Constant.CODE_OK, null, tFuelNos);
        return msg;
    }

    @PostMapping(value = "/generateFuelNo")
    public ReturnMsg<String> generateFuelNo(
            @RequestBody MyStaff staff,
            @RequestBody InTFuelNo tFuelNo) {
        return tFuelNoService.generateFuelNo(staff, tFuelNo);
    }

    /**
     * 任务结束把任务状态改为7同时把任务中的任务完成时间更新Pad
     */
    @PostMapping(value = "/recyclingNo")
    public ReturnMsg<Object> recyclingNo(@RequestBody MyTask task, @RequestBody MyStaff staff) {
        System.out.println("主动归还单号---->" + JSON.toJSONString(staff));
        // tFuelNoService.recyclingNo(task);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, "逻辑已经注释掉了", null);
        return msg;

    }


    @GetMapping("/test")
    public String test() {
        return tFuelNoService.test();
    }


    /**
     * pad 登录 获取 30个单号
     */
    @PostMapping(value = "/padGetNo")
    public ReturnMsg<Object> padGetNo(@RequestBody TFuelNo tFuelNo, @RequestBody MyStaff staff) {
        System.out.println("pad登录获取单号");
        List<Map<String, Object>> lists = tFuelNoService.padGetNo(tFuelNo, staff);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, "逻辑已经注释掉了", null);
        return msg;

    }
}
