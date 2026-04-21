package com.zh.controller;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyFuelRecptOld;
import com.zh.constant.Constant;
import com.zh.service.FuelRecptOldService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping(value = "/recpt/old")
@Api(tags = "历史油单")
public class FuelRecptOldController extends BaseController {

    @Autowired
    private FuelRecptOldService fuelRecptOldService;

    /**
     * 查询历史油单
     */
    @PostMapping(value = "/list")
    @ApiOperation(value = "查询历史油单")
    public ReturnMsg<List<MyFuelRecptOld>> getFuelHistory(@RequestBody MyFuelRecptOld myFuelRecptOld) {
        List<MyFuelRecptOld> list = fuelRecptOldService.getFuelHistory(myFuelRecptOld);
        return new ReturnMsg<List<MyFuelRecptOld>>(Constant.CODE_OK, null, list);
    }

}
