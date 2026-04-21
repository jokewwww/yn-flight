package com.zh.controller;

import com.zh.bean.ReturnMsg;
import com.zh.bean.login.MyServAirport;
import com.zh.bean.login.MyStaff;
import com.zh.constant.Constant;
import com.zh.service.SerAirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 服务机场表
 * duquanhong
 */
@RestController
@RequestMapping(value = "/Fwflight")
public class ServAirportController extends BaseController {

    @Autowired
    private SerAirportService airportService;

    /**
     * 根据机位查询服务机场信息（跨库使用）
     */
    @PostMapping(value = "/findFwflight")
    public MyServAirport findfwflight(@RequestBody MyServAirport airport) {

        return airportService.findfwflight(airport.getSvapAptplacNo());
    }

    /**
     * 根据机位查询机场区域代码（跨库使用）
     */
    @PostMapping(value = "/findFlightAreaCode")
    public String getAirportAreaCode(@RequestBody MyServAirport airport) {
        return airportService.getAirportAreaCode(airport.getSvapAptplacNo(), airport.getSvapAirportCode());
    }

    /**
     * 根据机场代码，机场区域代码查询服务表
     */
    @PostMapping(value = "/selectAirport")
    public ReturnMsg<List<MyServAirport>> selecairport(@RequestBody MyStaff staff) {
        List<MyServAirport> airport = airportService.selectairport(staff);
        return new ReturnMsg<List<MyServAirport>>(Constant.CODE_OK, null, airport);
    }

    /**
     * 对机位服务表进行添加
     */
    @PostMapping(value = "/insertAirport")
    public ReturnMsg<MyServAirport> insertairport(@RequestBody MyServAirport servAirport) {
        airportService.insertairport(servAirport);
        return new ReturnMsg<MyServAirport>(Constant.CODE_OK, null, null);
    }


    /**
     * 根据机场代码，机场区域代码，机位号，地井编号删除服务表信息
     */
    @DeleteMapping(value = "/deleteAirport")
    public ReturnMsg<MyServAirport> deleteairport(@RequestBody MyServAirport servAirport, @RequestBody MyStaff staff) {
        airportService.deleteairport(servAirport, staff);
        return new ReturnMsg<MyServAirport>(Constant.CODE_OK, null, null);
    }

    /**
     * 同步服务表信息
     */
    @PostMapping(value = "/synchronizationAirport")
    public ReturnMsg<MyServAirport> synchronizationAirport(@RequestBody MyServAirport servAirport, @RequestBody MyStaff staff) {
        airportService.synchronizationAirport(servAirport, staff);
        return new ReturnMsg<MyServAirport>(Constant.CODE_OK, null, null);
    }

    /**
     * 查询服务表
     */
    @PostMapping(value = "/selectServAirport")
    public ReturnMsg<List<MyServAirport>> selectServAirport(@RequestBody MyServAirport servAirport) {
        List<MyServAirport> airport = airportService.selectServAirport(servAirport);
        return new ReturnMsg<List<MyServAirport>>(Constant.CODE_OK, null, airport);
    }
}
