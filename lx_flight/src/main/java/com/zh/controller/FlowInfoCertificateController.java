package com.zh.controller;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.FlowInfoCertificate;
import com.zh.bean.login.MyStaff;
import com.zh.constant.Constant;
import com.zh.service.FlowInfoCertificateService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流量计检验合格证信息流量计基本信息
 */
@RestController
@RequestMapping(value = "/flowInfoCertificateController")
@Api(tags = "流量计检验合格证信息流量计基本信息")
public class FlowInfoCertificateController extends BaseController {

    @Autowired
    private FlowInfoCertificateService flowInfoCertificateService;

    /**
     * 查询流量计检验合格证信息
     */
    @GetMapping(value = "/getFlowInfoCertificate")
    public ReturnMsg<List<FlowInfoCertificate>> getFlowInfoCertificate(FlowInfoCertificate flowInfoCertificate, MyStaff staff) {
        List<FlowInfoCertificate> vehiType = flowInfoCertificateService.getFlowInfoCertificate(flowInfoCertificate);
        ReturnMsg<List<FlowInfoCertificate>> msg = new ReturnMsg<List<FlowInfoCertificate>>(Constant.CODE_OK, null, vehiType);
        return msg;
    }

    /**
     * 新增流量计检验合格证信息
     */
    @PostMapping(value = "/addFlowInfoCertificate")
    public ReturnMsg<Object> addVehiType(@RequestBody FlowInfoCertificate flowInfoCertificate) {
        FlowInfoCertificate entity = flowInfoCertificateService.addFlowInfoCertificate(flowInfoCertificate);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, entity);
        return msg;
    }

    /**
     * 修改流量计检验合格证信息
     */
    @PostMapping(value = "/updateFlowInfoCertificate")
    public ReturnMsg<Object> updateFlowInfoCertificate(@RequestBody FlowInfoCertificate flowInfoCertificate) {
        int entity = flowInfoCertificateService.updateFlowInfoCertificate(flowInfoCertificate);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, entity);
        return msg;
    }

    /**
     * 删除流量计检验合格证信息
     */
    @PostMapping(value = "/deleteFlowInfoCertificate")
    public ReturnMsg<Object> deleteFlowInfoCertificate(@RequestBody FlowInfoCertificate flowInfoCertificate) {
        int entity = flowInfoCertificateService.deleteFlowInfoCertificate(flowInfoCertificate);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, entity);
        return msg;
    }

}
