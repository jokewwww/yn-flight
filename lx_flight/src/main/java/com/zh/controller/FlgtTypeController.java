package com.zh.controller;

import com.zh.bean.ReturnMsg;
import com.zh.bean.login.*;
import com.zh.constant.Constant;
import com.zh.service.FlgtTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 保障机型
 */
@RestController
@RequestMapping(value = "/flgtTypeController")
@Api(tags = "人员绑定不保障机型")
public class FlgtTypeController extends BaseController {

    @Autowired
    private FlgtTypeService flgtTypeService;

    /**
     * 获取人员和不保障机型
     */
    @PostMapping(value = "/getFlgtTypeByStaffId")
    @ApiOperation(value = "获取人员和不保障机型")
    public ReturnMsg<List<FlgtTypeStaff>> getFlgtTypeByStaffId(@RequestParam("staffId") String staffId) {
        List<FlgtTypeStaff> FlgtTypeByStaffId = flgtTypeService.getFlgtTypeByStaffId(staffId);
        ReturnMsg<List<FlgtTypeStaff>> msg = new ReturnMsg<List<FlgtTypeStaff>>(Constant.CODE_OK, null, FlgtTypeByStaffId);
        return msg;
    }

    /**
     * 绑定人员和不保障机型
     */
    @PostMapping(value = "/bindFlgtTypeByStaffId")
    @ApiOperation(value = "绑定人员和不保障机型")
    public ReturnMsg<Object> bindFlgtTypeByStaffId(@RequestBody StaffBind staffBind) {
        int a = flgtTypeService.bindFlgtTypeByStaffId(staffBind);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, a);
        return msg;
    }


    /**
     * 获取机型列表
     */
    @GetMapping(value = "/getFlgtType")
    public ReturnMsg<List<FlgtType>> getFlgtType(FlgtType flgtType, MyStaff staff) {
        List<FlgtType> vehiType = flgtTypeService.getFlgtType(flgtType);
        ReturnMsg<List<FlgtType>> msg = new ReturnMsg<List<FlgtType>>(Constant.CODE_OK, null, vehiType);
        return msg;
    }

    /**
     * 新增机型列表
     */
    @PostMapping(value = "/addFlgtType")
    public ReturnMsg<Object> addFlgtType(@RequestBody FlgtType flgtType) {
        FlgtType entity = flgtTypeService.addFlgtType(flgtType);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, entity);
        return msg;
    }

    /**
     * 修改
     */
    @PostMapping(value = "/updateFlgtType")
    public ReturnMsg<Object> updateFlgtType(@RequestBody FlgtType flgtType) {
        int entity = flgtTypeService.updateFlgtType(flgtType);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, entity);
        return msg;
    }

    /**
     * 删除机型列表
     */
    @PostMapping(value = "/deleteFlgtType")
    public ReturnMsg<Object> deleteFlgtType(@RequestBody FlgtType flgtType) {
        int entity = flgtTypeService.deleteFlgtType(flgtType);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, entity);
        return msg;
    }


    /**
     * 获取人员和不保障机型
     */
    @PostMapping(value = "/getNoFlgtTypeByStaffId")
    @ApiOperation(value = "根据人员id获取不保障机型")
    public ReturnMsg<List<FlgtTypeStaff>> getNoFlgtTypeByStaffId(@RequestParam("staffId") String staffId) {
        List<FlgtTypeStaff> FlgtTypeByStaffId = flgtTypeService.getNoFlgtTypeByStaffId(staffId);
        ReturnMsg<List<FlgtTypeStaff>> msg = new ReturnMsg<List<FlgtTypeStaff>>(Constant.CODE_OK, null, FlgtTypeByStaffId);
        return msg;
    }

    /**
     * 获取车型和不保障机型
     */
    @GetMapping(value = "/getFlgtTypeByVehi")
    @ApiOperation(value = "获取车型和不保障机型")
    public ReturnMsg<List<FlgtTypeVehi>> getFlgtTypeByVehi(@RequestParam String flgtTypeId) {
        List<FlgtTypeVehi> FlgtTypeByStaffId = flgtTypeService.getFlgtTypeByVehi(flgtTypeId);
        ReturnMsg<List<FlgtTypeVehi>> msg = new ReturnMsg<List<FlgtTypeVehi>>(Constant.CODE_OK, null, FlgtTypeByStaffId);
        return msg;
    }

    /**
     * 绑定保障机型车型关系
     */
    @PostMapping(value = "/bindFlgtTypeBySVehi")
    @ApiOperation(value = "绑定保障机型车型关系")
    public ReturnMsg<Object> bindFlgtTypeBySVehi(@RequestBody VehiBind vehiBind) {
        int a = flgtTypeService.bindFlgtTypeBySVehi(vehiBind);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, a);
        return msg;
    }

    /**
     * 根据人员id获取是否已设置不可保障该机型 0可以下发，1人员原因不能下发2是车型原因不能下发
     */
    @PostMapping(value = "/getNoFlgtNoByStaffId")
    @ApiOperation(value = "根据人员id获取是否已设置不可保障该机型")
    public ReturnMsg<Object> getNoFlgtNoByStaffId(@RequestParam("staffId") String staffId, @RequestParam("type") String type) {
        Integer noFlgtNoByStaffId = flgtTypeService.getNoFlgtNoByStaffId(staffId, type);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, noFlgtNoByStaffId);
        return msg;
    }

}
