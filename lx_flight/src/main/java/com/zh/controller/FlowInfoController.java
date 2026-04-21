package com.zh.controller;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.FlowInfo;
import com.zh.bean.login.MyStaff;
import com.zh.constant.Constant;
import com.zh.service.FlowInfoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车型流量计基本信息流量计基本信息
 */
@RestController
@RequestMapping(value = "/flowInfoController")
@Api(tags = "车型流量计基本信息流量计基本信息")
public class FlowInfoController extends BaseController {

    @Autowired
    private FlowInfoService flowInfoService;

    /**
     * 查询车型流量计基本信息
     */
    @GetMapping(value = "/getFlowInfo")
    public ReturnMsg<List<FlowInfo>> getFlowInfo(FlowInfo flowInfo, MyStaff staff) {
        List<FlowInfo> vehiType = flowInfoService.getFlowInfo(flowInfo);
        ReturnMsg<List<FlowInfo>> msg = new ReturnMsg<List<FlowInfo>>(Constant.CODE_OK, null, vehiType);
        return msg;
    }

    /**
     * 新增车型流量计基本信息
     */
    @PostMapping(value = "/addFlowInfo")
    public ReturnMsg<Object> addVehiType(@RequestBody FlowInfo flowInfo) {
        FlowInfo entity = flowInfoService.addFlowInfo(flowInfo);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, entity);
        return msg;
    }

    /**
     * 修改车型流量计基本信息
     */
    @PostMapping(value = "/updateFlowInfo")
    public ReturnMsg<Object> updateFlowInfo(@RequestBody FlowInfo flowInfo) {
        int entity = flowInfoService.updateFlowInfo(flowInfo);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, entity);
        return msg;
    }

    /**
     * 删除车型流量计基本信息
     */
    @PostMapping(value = "/deleteFlowInfo")
    public ReturnMsg<Object> deleteFlowInfo(@RequestBody FlowInfo flowInfo) {
        int entity = flowInfoService.deleteFlowInfo(flowInfo);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, entity);
        return msg;
    }

}
