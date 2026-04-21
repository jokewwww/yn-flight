package com.zh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.zh.annotation.TaskLogs;
import com.zh.bean.ReturnMsg;
import com.zh.bean.login.MyFuel;
import com.zh.bean.login.MyStaff;
import com.zh.service.NewFuelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fuel")
public class NewFuelController {

    @Autowired
    private NewFuelService fuelService;


    /**
     * （pad查询油料接口）查询最近有效的油料参数
     *
     * @param staff
     * @return
     */
    @PostMapping("/param")
    public ReturnMsg<List<MyFuel>> findNewFuelParam(@RequestBody MyStaff staff) {
        List<MyFuel> newFuelParam = fuelService.findNewFuelParam(staff);
        newFuelParam.forEach(o -> {
            o.setOld(true);
        });

        List<MyFuel> fuelFutures = fuelService.findNewFuelFuture(staff);
        fuelFutures.forEach(o -> {
            o.setOld(true);
            o.setFuture(true);
        });
        newFuelParam.addAll(fuelFutures);
        return ReturnMsg.getInstanceOKz(newFuelParam);
    }


    ///**
    // * 查询当前时间之前生效的油料信息和即将生效的油料信息
    // * @param staff
    // * @return
    // */
    //@PostMapping("/all")
    //public ReturnMsg<JSONObject> findNewFuelParamAll(@RequestBody MyStaff staff){
    //    List<MyFuel> newFuelParam = fuelService.findNewFuelParam(staff);
    //    newFuelParam.forEach(o->{o.setOld(true);});
    //    JSONObject data=new JSONObject();
    //    data.put("effect",newFuelParam);
    //
    //    List<MyFuel> fuelFuture = fuelService.findNewFuelFuture(staff);
    //    fuelFuture.forEach(o->{o.setOld(true);});
    //    data.put("future",fuelFuture);
    //    return ReturnMsg.getInstanceOKz(data);
    //}


    /**
     * 查询油料计划
     *
     * @param staff
     * @return
     */
    @PostMapping("/finds")
    public ReturnMsg<List<MyFuel>> findNewFuelPlans(@RequestBody MyStaff staff) {
        return ReturnMsg.getInstanceOKz(fuelService.findNewfuels(staff));
    }

    /**
     * 保存油料计划
     *
     * @param staff
     * @param fuelList
     * @return
     */
    @PostMapping("/save")
    @TaskLogs("保存油料计划")
    public ReturnMsg<MyFuel> saveNewFuels(@RequestBody MyStaff staff, @RequestBody String fuelList) {
        JSONObject jsonObject = JSONObject.parseObject(fuelList);
        String fuels = jsonObject.getString("oilParamList");
        if (StringUtils.isNotEmpty(fuels)) {
            List<MyFuel> list = JSON.parseObject(fuels, new TypeReference<ArrayList<MyFuel>>() {
            });
            int res = fuelService.saveNewFuels(list, staff);
            return res > 0 ? ReturnMsg.getInstanceOKz(list.size() > 0 ? list.get(0) : null) : ReturnMsg.getInstanceNGz(null);
        } else {
            return ReturnMsg.getInstanceNGz("油料参数key错误", null);
        }
    }

    @RequestMapping("/saveOne")
    public ReturnMsg<MyFuel> saveOne(@RequestBody MyStaff staff, @RequestBody MyFuel fuel) {
        int res = fuelService.saveNewFuels(Lists.newArrayList(fuel), staff);
        return res > 0 ? ReturnMsg.getInstanceOKz(fuel) : ReturnMsg.getInstanceNGz(null);
    }


}
