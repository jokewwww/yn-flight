package com.example.contrast_entity.controller;

import com.example.contrast_entity.service.FlightChangeLogsService;
import com.example.contrast_entity.util.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/3/7 15:43
 * @Description:
 */
@RestController
@Api(description = "航显记录管理")
public class FlightChangeLogsController {

    @Autowired
    private FlightChangeLogsService flightChangeLogsService;

    /*@PostMapping("/saveFlightChangeLogs")
    public void saveFlightChangeLogs(
            @RequestParam("oldData") String oldData,
            @RequestParam("newData") String newData
    ) {
        flightChangeLogsService.saveFlightChangeLogs(oldData, newData);
    }*/
    @ApiOperation(value = "获取进出港航班记录")
    @GetMapping(value = "/getFlightChangeLogs")
    public ResponseObject getFlightChangeLogs(
            @ApiParam(value = "ffid",required = true)@RequestParam(value = "flgtFfid", required = true) String ffid,
            @ApiParam(value = "type 0 进港航班 1 出港航班",required = true) @RequestParam(value = "type", required = true) Integer type //0 进港航班  // 1 出港航班
    ) {
        return flightChangeLogsService.getFlightChangeLogs(ffid, type);
    }
    @ApiOperation(value = "获取关联航变记录")
    @GetMapping("/getFlightChangeLogsLink")
    public ResponseObject getFlightChangeLogsLink(
            @RequestParam(value = "flgtFfid", required = true) String ffid
    ) {
        return flightChangeLogsService.getFlightChangeLogsLink(ffid);
    }

    @GetMapping("/test1")
    public ResponseObject test1(
    ) {
        return flightChangeLogsService.test();
    }


}
