package com.zh.controller;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.TFlight;
import com.zh.bean.login.MyStaff;
import com.zh.bean.login.MyVehi;
import com.zh.bean.login.TStaff;
import com.zh.bean.login.TVehiInfo;
import com.zh.constant.Constant;
import com.zh.service.VehiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 车辆表(童雪俊)
 */
@RestController
@RequestMapping(value = "/vehiController")
public class VehiController extends BaseController {

    @Autowired
    private VehiService vehiService;

    /**
     * 获取车辆数量（总数，在线，保障中）
     */
    @PostMapping(value = "/getVehiSum")
    public ReturnMsg<Map<String, Object>> getVehiSum(@RequestBody TStaff staff) {
        //使用map集合接受查出来的各个航班数量
        Map<String, Object> vehiAmountMap = vehiService.getVehiSum(staff);
        ReturnMsg<Map<String, Object>> msg = new ReturnMsg<Map<String, Object>>(Constant.CODE_OK, null, vehiAmountMap);
        return msg;
    }

    /**
     * 车辆信息接口
     * 以（所属机场代码＝输入_机场ID）的条件，取得车辆表（DB）中对象记录的如下字段，写入输出接口
     */
    @PostMapping(value = "/getVehiList")
    public ReturnMsg<List<TVehiInfo>> getVehiList(@RequestBody TFlight flight) {
        List<TVehiInfo> vehiList = vehiService.getVehiList(flight);
        ReturnMsg<List<TVehiInfo>> msg = new ReturnMsg<List<TVehiInfo>>(Constant.CODE_OK, null, vehiList);
        return msg;
    }

    /**
     * 手动添加车辆信息接口
     */
    @PostMapping(value = "/addVehiInfo")
    public ReturnMsg<Object> addVehiInfo(@RequestBody MyVehi vehi, @RequestBody MyStaff staff) {
        Integer addVehiInfo = vehiService.addVehiInfo(vehi, staff);
        if (addVehiInfo == 0) {
            return new ReturnMsg<Object>(Constant.CODE_ERR, "车辆编号已存在！", null);
        } else if (addVehiInfo == 2) {
            return new ReturnMsg<Object>(Constant.CODE_ERR, "车牌号已存在！", null);
        } else {
            return new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        }
    }

    /**
     * 根据机场代码和车辆号获取车辆油单编号
     */
    @PostMapping(value = "/getFuelRecptNo")
    public ReturnMsg<Map<String, Object>> getFuelRecptNo(@RequestBody MyVehi vehi) {
        Map<String, Object> map = vehiService.getFuelRecptNo(vehi);
        ReturnMsg<Map<String, Object>> msg = new ReturnMsg<Map<String, Object>>(Constant.CODE_OK, null, map);
        return msg;
    }

    /**
     * 根据机场代码和车辆号获取车辆油单编号和序号
     */
    @PostMapping(value = "/getFuelRecptNoAndFuelSno")
    public ReturnMsg<Map<String, Object>> getFuelRecptNoAndFuelSno(@RequestBody MyVehi vehi) {
        Map<String, Object> map = vehiService.getFuelRecptNoAndFuelSno(vehi);
        ReturnMsg<Map<String, Object>> msg = new ReturnMsg<Map<String, Object>>(Constant.CODE_OK, null, map);
        return msg;
    }

    /**
     * 根据机场代码和车辆号获取车辆更新油单序列号
     *
     * @return 1;//成功 -1;//查无此车 2;//更新的序号小于数据库已有序号无需更新 -2;//更新数据库失败
     */
    @PostMapping(value = "/updateVehiFuelSno")
    public Integer updateVehiFuelSno(@RequestBody MyVehi vehi) {
        return vehiService.updateVehiFuelSno(vehi);
    }
}
