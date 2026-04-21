package com.zh.controller;


import com.alibaba.fastjson.JSON;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.TCreditInfo;
import com.zh.constant.Constant;
import com.zh.service.TCreditInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 */
@RestController
@RequestMapping(value = "/tCreditInfoController")
public class TCreditInfoController extends BaseController {

    @Autowired
    private TCreditInfoService creditInfoService;

    /**
     * 查询
     */
    @PostMapping(value = "/selectCreditInfo")
    public ReturnMsg<List<TCreditInfo>> selectCreditInfo(@RequestBody TCreditInfo creditInfo) {
        List<TCreditInfo> CreditInfo = creditInfoService.selectCreditInfo(creditInfo);
        return new ReturnMsg<List<TCreditInfo>>(Constant.CODE_OK, null, CreditInfo);
    }

    @PostMapping(value = "/insertCreditInfo")
    public ReturnMsg<TCreditInfo> insertCreditInfo(@RequestBody TCreditInfo creditInfo) {
        System.out.println("新建--CreditInfo---" + JSON.toJSONString(creditInfo));
        Integer integer = creditInfoService.insertCreditInfo(creditInfo);
        return new ReturnMsg<TCreditInfo>(Constant.CODE_OK, "订单已经新增", null);

    }

    @PostMapping(value = "/updateCreditInfo")
    public ReturnMsg<TCreditInfo> updateCreditInfo(
            @RequestBody TCreditInfo creditInfo) {
        // 修改
        Integer integer = creditInfoService.updateCreditInfo(creditInfo);
        return new ReturnMsg<TCreditInfo>(Constant.CODE_OK, "修改成功", null);
    }

    /**
     * 删除
     */
    @PostMapping(value = "/deleteCreditInfo")
    public ReturnMsg<TCreditInfo> deleteCreditInfo(@RequestBody TCreditInfo creditInfo) {
        creditInfoService.deleteCreditInfo(creditInfo);
        return new ReturnMsg<TCreditInfo>(Constant.CODE_OK, null, null);

    }

}
