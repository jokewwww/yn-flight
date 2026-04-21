package com.zh.controller;


import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyTFlightInfo;
import com.zh.bean.login.MyStaff;
import com.zh.service.TFlightInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flightInfo")
public class TFlightInfoController {

    @Autowired
    private TFlightInfoService tFlightInfoService;

    @PostMapping(value = "/getAll")
    public ReturnMsg<Object> getAll(@RequestBody MyTFlightInfo myTFlightInfo, @RequestBody MyStaff staff) {
        return tFlightInfoService.getAll(myTFlightInfo, staff);
    }


    @PostMapping(value = "/insert")
    public ReturnMsg<Object> insert(@RequestBody MyTFlightInfo myTFlightInfo, @RequestBody MyStaff staff) {
        return tFlightInfoService.insert(myTFlightInfo, staff);
    }


    @PostMapping(value = "/update")
    public ReturnMsg<Object> update(@RequestBody MyTFlightInfo myTFlightInfo, @RequestBody MyStaff staff) {
        return tFlightInfoService.update(myTFlightInfo, staff);
    }

    @PostMapping(value = "/delete")
    public ReturnMsg<Object> delete(@RequestBody MyTFlightInfo myTFlightInfo, @RequestBody MyStaff staff) {
        return tFlightInfoService.delete(myTFlightInfo, staff);
    }


    @PostMapping(value = "/sendOne")
    public ReturnMsg<Object> sendOne(@RequestBody MyTFlightInfo myTFlightInfo, @RequestBody MyStaff staff) {
        return tFlightInfoService.sendOne(myTFlightInfo, staff);
    }


}
