package com.zh.controller;

import com.zh.bean.ReturnMsg;
import com.zh.bean.login.MyStaff;
import com.zh.bean.login.StaffBind;
import com.zh.bean.login.VehiType;
import com.zh.bean.login.VehiTypeStaff;
import com.zh.constant.Constant;
import com.zh.service.VehiTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 准驾车型
 */
@RestController
@RequestMapping(value = "/vehiTypeController")
@Api(tags = "人员绑定不准驾车型")
public class VehiTypeController extends BaseController {

    @Autowired
    private VehiTypeService vehiTypeService;


    /**
     * 获取人员和不准驾车型
     */
    @PostMapping(value = "/getVehiNoByStaffId")
    @ApiOperation(value = "获取人员和不准驾车型")
    public ReturnMsg<List<VehiTypeStaff>> getVehiNoByStaffId(@RequestParam("staffId") String staffId) {
        List<VehiTypeStaff> vehiNoByStaffId = vehiTypeService.getVehiNoByStaffId(staffId);
        ReturnMsg<List<VehiTypeStaff>> msg = new ReturnMsg<List<VehiTypeStaff>>(Constant.CODE_OK, null, vehiNoByStaffId);
        return msg;
    }

    /**
     * 绑定人员和不准驾车型
     */
    @PostMapping(value = "/bindVehiNoByStaffId")
    @ApiOperation(value = "绑定人员和不准驾车型")
    public ReturnMsg<Object> bindVehiNoByStaffId(@RequestBody StaffBind staffBind) {
        int a = vehiTypeService.bindVehiNoByStaffId(staffBind);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, a);
        return msg;
    }


    /**
     * 查询车型
     */
    @GetMapping(value = "/getVehiType")
    public ReturnMsg<List<VehiType>> getVehiType(VehiType vehiType, MyStaff staff) {
        List<VehiType> list = vehiTypeService.getVehiType(vehiType);
        ReturnMsg<List<VehiType>> msg = new ReturnMsg<List<VehiType>>(Constant.CODE_OK, null, list);
        return msg;
    }

    /**
     * 新增车型
     */
    @PostMapping(value = "/addVehiType")
    public ReturnMsg<Object> addVehiType(@RequestBody VehiType flgtType) {
        VehiType entity = vehiTypeService.addVehiType(flgtType);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, entity);
        return msg;
    }

    /**
     * 修改车型
     */
    @PostMapping(value = "/updateVehiType")
    public ReturnMsg<Object> updateVehiType(@RequestBody VehiType flgtType) {
        int entity = vehiTypeService.updateVehiType(flgtType);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, entity);
        return msg;
    }

    /**
     * 删除车型
     */
    @PostMapping(value = "/deleteVehiType")
    public ReturnMsg<Object> deleteVehiType(@RequestBody VehiType flgtType) {
        int entity = vehiTypeService.deleteVehiType(flgtType);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, entity);
        return msg;
    }

    /**
     * pad获取人员和不准驾车型
     */
    @PostMapping(value = "/getNoVehiNoByStaffId")
    @ApiOperation(value = "根据人员id获取不准驾车型")
    public ReturnMsg<List<VehiTypeStaff>> getNoVehiNoByStaffId(@RequestBody MyStaff staff) {
        List<VehiTypeStaff> vehiNoByStaffId = vehiTypeService.getNoVehiNoByStaffId(staff.getLoginUserIn().getStaffId());
        ReturnMsg<List<VehiTypeStaff>> msg = new ReturnMsg<List<VehiTypeStaff>>(Constant.CODE_OK, null, vehiNoByStaffId);
        return msg;
    }

}
