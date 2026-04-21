package com.zh.controller;

import cn.hutool.core.util.NumberUtil;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.FuelVolumeMonitor;
import com.zh.bean.flight.FuelVolumeMonitorSend;
import com.zh.bean.login.MyStaff;
import com.zh.constant.Constant;
import com.zh.util.SendMsg2Redis;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright: Copyright (c) 2021 hge
 *
 * @ClassName: FuelVolumeMonitorController.java
 * @Description:
 * @version: v1.0.0
 * @author: nimz
 * @date: 2021年12月06日 8:58
 */
@RestController
@RequestMapping(value = "/fuelVolumeMonitorController")
@Api(tags = "加油量实时监控")
public class FuelVolumeMonitorController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static void main(String[] args) {
        FuelVolumeMonitor fuelVolumeMonitor = new FuelVolumeMonitor();
        fuelVolumeMonitor.setRefuelProgress(NumberUtil.div(7, 3, 2) * 100);

        System.out.println(NumberUtil.round(2.33555, 2));
    }

    /**
     * 加油量实时监控
     */
    @PostMapping(value = "/fuelVolumeMonitor")
    public ReturnMsg<Object> fuelVolumeMonitor(@RequestBody FuelVolumeMonitor fuelVolumeMonitor, @RequestBody MyStaff staff) {
        String staffId = staff.getLoginUserIn().getStaffId();
        fuelVolumeMonitor.setCurrentSpeed(NumberUtil.round(fuelVolumeMonitor.getCurrentSpeed() * 60, 2).doubleValue());
        fuelVolumeMonitor.setCurrentSpeedT(NumberUtil.round(fuelVolumeMonitor.getCurrentSpeedT() * 60, 2).doubleValue());

        if (fuelVolumeMonitor.getEstimatedRefuel() == 0
                || (fuelVolumeMonitor.getCurrentSpeed() + fuelVolumeMonitor.getCurrentSpeedT()) == 0) {


        } else {
            // 实时加油量
            fuelVolumeMonitor.setFuel(fuelVolumeMonitor.getFuel());
            double estimatedRefuelVol = fuelVolumeMonitor.getEstimatedRefuelVol();
            // TODO 为了兼容低版本pad，之前代码是用加油量（升）除预加油量（KG）算加油进度，算错了，目前如果pad传了升就用升，没传还是原来的算法
            if (estimatedRefuelVol == 0) {
                estimatedRefuelVol = fuelVolumeMonitor.getEstimatedRefuel();
            }
            double refuelProgress = NumberUtil.div(fuelVolumeMonitor.getFuel(), estimatedRefuelVol, 2) * 100;
            refuelProgress = refuelProgress > 100 ? 100 : refuelProgress;
            fuelVolumeMonitor.setRefuelProgress(refuelProgress);
            double v = fuelVolumeMonitor.getEstimatedRefuel() - fuelVolumeMonitor.getFuel();
            // 不能存在负数
            v = v < 0 ? 0 : v;

            fuelVolumeMonitor.setEstimatedRefuelTime(NumberUtil.div(v, (fuelVolumeMonitor.getCurrentSpeed() + fuelVolumeMonitor.getCurrentSpeedT()), 2));
        }
        fuelVolumeMonitor.setStaffId(staffId);
        SendMsg2Redis.testDingYueToAllType(stringRedisTemplate, Constant.STAFF, Constant.FUEL_VOLUME_MONITOR, new FuelVolumeMonitorSend(fuelVolumeMonitor));
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, fuelVolumeMonitor);
        return msg;
    }

}
