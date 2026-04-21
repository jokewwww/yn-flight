package com.zh.controller;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyTask;
import com.zh.bean.login.*;
import com.zh.constant.Constant;
import com.zh.service.FuelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 油料表
 * duquanhong
 */
@RestController
@RequestMapping(value = "/fuelopt")
public class FuelController extends BaseController {

    @Autowired
    private FuelService fuelService;

    /**
     * 根据机场码查询油料表最新一条数据
     *
     * @param
     * @return
     */
    @PostMapping(value = "/oilfuel")
    public ReturnMsg<TFuel> findOilfuel(@RequestBody TStaff tStaff) {
        TFuel findOilfuel = fuelService.findfuel(tStaff);
        return new ReturnMsg<TFuel>(Constant.CODE_OK, null, findOilfuel);

    }

    /**
     * 根据员工id查询油料表最新一条数据(垮库）
     *
     * @param
     * @return
     */
    @PostMapping(value = "/oilfuels")
    public TFuel findOilfuels(@RequestBody MyTask task) {
        return fuelService.findfuels(task);

    }

    /**
     * 查询油料最近七天的数据并且排序
     */
    @PostMapping(value = "/findFuel")
    public ReturnMsg<List<MyFuel>> findFuel(@RequestBody MyStaff staff) {
        List<MyFuel> findFuel = fuelService.findfuelist(staff);
        return new ReturnMsg<List<MyFuel>>(Constant.CODE_OK, null, findFuel);


    }

    /***
     * 油料录入
     */
    @PutMapping(value = "/setFuel")
    public ReturnMsg<String> setFuel(@RequestBody MyFuel fuel, @RequestBody MyStaff staff) {
        if (fuel.getFuelAirportCode() == null || "".equals(fuel.getFuelAirportCode())) {
            fuel.setFuelAirportCode(staff.getLoginUserIn().getStaffAirportCode());
        }
        String setFuel = fuelService.setFuel(fuel, staff);
        if ("已存在".equals(setFuel)) {
            return new ReturnMsg<String>(Constant.CODE_ERR, setFuel, null);
        } else {
            return new ReturnMsg<String>(Constant.CODE_OK, null, setFuel);
        }
    }

    /***
     * 新版本 油料录入
     */
    @PutMapping(value = "/setNewFuel")
    public ReturnMsg<String> setNewFuel(@RequestBody List<MyFuel> listfuel, @RequestBody MyStaff staff) {
        String setFuel = fuelService.setNewFuel(listfuel, staff);
        if (!"录入成功".equals(setFuel)) {
            return new ReturnMsg<String>(Constant.CODE_ERR, setFuel, null);
        } else {
            return new ReturnMsg<String>(Constant.CODE_OK, null, setFuel);
        }
    }

    /**
     * 修改人员表（垮库）
     *
     * @param staff
     * @return
     */
    @PostMapping(value = "/upstaff")
    public ReturnMsg<MyStaff> updatestaff(@RequestBody MyStaff staff) {
        fuelService.updatestaff(staff);
        return new ReturnMsg<MyStaff>(Constant.CODE_OK, null, null);

    }

    /**
     * 修改 区域代码
     *
     * @param task
     * @return
     */
    @PutMapping(value = "/updateAirprtcodeServ")
    public ReturnMsg<String> updateAirprtcodeServ(@RequestBody MyStaff staff, @RequestBody MyTask task) {
        fuelService.updateAiprortSer(staff, task);
        return new ReturnMsg<String>(Constant.CODE_OK, null, null);

    }

    /**
     * 根据加油员ID查询加油车编号（垮库）
     */
    @PostMapping(value = "/selectstaffvehi")
    public MyStaffVehi selectstaffvehi(@RequestBody MyStaffVehi staffvehi) {
        return fuelService.selectstaffvehi(staffvehi);
    }

    /**
     * 逻辑删除
     *
     * @param fuel
     * @return
     */
    @PutMapping(value = "/updatefuelLogicDelFlg")
    public ReturnMsg<MyFuel> updatefuelLogicDelFlg(@RequestBody MyFuel fuel) {
        fuelService.updatefuelLogicDelFlg(fuel);
        return new ReturnMsg<MyFuel>(Constant.CODE_OK, null, null);
    }

}
