package com.zh.controller;

import cn.hutool.core.util.ObjectUtil;
import com.zh.annotation.TaskLogs;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.RecycleFuelRequest;
import com.zh.bean.flight.SysDict;
import com.zh.bean.flight.TFuelDistribution;
import com.zh.bean.login.MyStaff;
import com.zh.constant.Constant;
import com.zh.service.SysDictService;
import com.zh.service.TFuelDistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * duquanhong
 */
@RestController
@RequestMapping(value = "/tFuelDistribution")
public class TFuelDistributionController extends BaseController {

    @Autowired
    private TFuelDistributionService tFuelDistributionService;

    @Autowired
    private SysDictService sysDictService;

    /**
     * PC端回收油单
     *
     * @param request
     * @param staff
     * @return
     */
    @TaskLogs("PC端回收油单")
    @PostMapping(value = "/recycle")
    public ReturnMsg<String> recycleFuelPC(@RequestBody RecycleFuelRequest request, @RequestBody MyStaff staff) {
        System.out.println("--PC端回收油单---油单号{}" + request.gettFuelNo() + "----操作人{}----" + staff.getLoginUserIn().getStaffId());
        SysDict sysDict = new SysDict();
        sysDict.setDataCode("recycle_fuel_pw");
        List<SysDict> sysDicts = sysDictService.selectList(sysDict);
        boolean present = sysDicts.stream().filter(dto -> ObjectUtil.equal(request.getPassword(), dto.getDataValue()))
                .findFirst()
                .isPresent();
        if (present) {
            Integer fuelData = tFuelDistributionService.updateList(request.gettFuelNo());
            return new ReturnMsg<>(Constant.CODE_OK, null, "本次回收油单" + fuelData + "条");
        } else {
            return new ReturnMsg<>(Constant.CODE_ERR, null, "请输入回收密码");
        }
    }

    /**
     * pc获取pad占用油单号
     *
     * @param staff
     * @return
     */
    @PostMapping(value = "/list")
    public ReturnMsg<List<TFuelDistribution>> getPadFulePc(@RequestBody TFuelDistribution tFuelDistribution, @RequestBody MyStaff staff) {
        return new ReturnMsg<>(Constant.CODE_ERR, null, tFuelDistributionService.selectList(tFuelDistribution));
    }

}
