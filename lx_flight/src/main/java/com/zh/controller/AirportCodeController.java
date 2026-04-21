package com.zh.controller;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyAirportCode;
import com.zh.constant.Constant;
import com.zh.service.AirportCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 机场代码表
 */
@RestController
@RequestMapping(value = "/air")
public class AirportCodeController extends BaseController {

    @Autowired
    private AirportCodeService airportCodeService;

    /**
     * 机场增加
     *
     * @param airportCode
     * @return
     */
    @PostMapping(value = "/insertAirportCode")
    public ReturnMsg<MyAirportCode> insertAirportCode(@RequestBody MyAirportCode airportCode) {
        Integer insertCode = airportCodeService.insertAirportCode(airportCode);
        if (insertCode == 1) {
            return new ReturnMsg<MyAirportCode>(Constant.CODE_ERR, "已存在!", null);
        } else {
            return new ReturnMsg<MyAirportCode>(Constant.CODE_OK, null, null);
        }
    }

    /**
     * 查询机场
     */
    @PostMapping(value = "/selectAirportCode")
    public ReturnMsg<List<MyAirportCode>> selectAirportCode() {
        List<MyAirportCode> aircode = airportCodeService.selectAirportCode();
        return new ReturnMsg<List<MyAirportCode>>(Constant.CODE_OK, null, aircode);
    }

    /**
     * 级联查询机场
     */
    @PostMapping(value = "/selectAirportCodeCascade")
    public ReturnMsg<Map<String, Map<String, Map<String, Object>>>> selectAirportCodeCascade() {
        Map<String, Map<String, Map<String, Object>>> aircode = airportCodeService.selectAirportCodeCascade();
        return new ReturnMsg<>(Constant.CODE_OK, null, aircode);
    }

    /**
     * 级联查询机场forAndroid
     */
    @PostMapping(value = "/selectAirportCodeCascadeAndroid")
    public ReturnMsg<List<Map<String, Object>>> selectAirportCodeCascadeAndroid() {
        List<Map<String, Object>> aircode = airportCodeService.selectAirportCodeCascadeAndroid();
        return new ReturnMsg<>(Constant.CODE_OK, null, aircode);
    }


    /**
     * 修改机场
     */
    @PutMapping(value = "/updateAirportCode")
    public ReturnMsg<MyAirportCode> updateAirportCode(@RequestBody MyAirportCode airportCode) {
        airportCodeService.updateAirportCode(airportCode);
        return new ReturnMsg<MyAirportCode>(Constant.CODE_OK, null, null);

    }

    /**
     * 删除机场
     */
    @DeleteMapping(value = "/deleteAirportCode")
    public ReturnMsg<MyAirportCode> deleteAirportCode(@RequestBody MyAirportCode airportCode) {
        airportCodeService.deleteAirportCode(airportCode);
        return new ReturnMsg<MyAirportCode>(Constant.CODE_OK, null, null);

    }

    /**
     * 查询机场详情
     */
    @PostMapping(value = "/selectAirportCodeFind")
    public ReturnMsg<MyAirportCode> selectAirportCodeFind(@RequestBody MyAirportCode airportCode) {
        MyAirportCode aircode = airportCodeService.selectAirportCodeFind(airportCode);
        return new ReturnMsg<MyAirportCode>(Constant.CODE_OK, null, aircode);
    }

    /**
     * 查询机场代码
     */
    @PostMapping(value = "/selectAirportCodePropD")
    public ReturnMsg<List<MyAirportCode>> selectAirportCodePropD() {
        List<MyAirportCode> airportCodeList = airportCodeService.selectAirportCodePropD();
        return new ReturnMsg<List<MyAirportCode>>(Constant.CODE_OK, null, airportCodeList);
    }
}
