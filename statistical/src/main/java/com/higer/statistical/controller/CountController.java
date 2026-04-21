package com.higer.statistical.controller;

import com.higer.statistical.service.CountService;
import com.higer.statistical.util.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;

@Api(tags = "统计查询通用接口")
@RestController
public class CountController {

    @Autowired
    private CountService countService;


    @ApiOperation("按月统计：任务数、共加多少油（同比、环比）")
    @GetMapping("/countByMonth")
    public ResponseObject countByMonth(@ApiParam("机场代码") @RequestParam("code") String airportCode){
        return ResponseObject.success(countService.countByMonth(airportCode),"成功");
    }

    @ApiOperation("峰值统计")
    @GetMapping("/crestValue")
    public ResponseObject countCrestValue(@ApiParam("机场代码") @RequestParam("code") String airportCode) {
        return ResponseObject.success(countService.countCrestValue(airportCode,Calendar.getInstance()),"成功");
    }

    @ApiOperation("排名统计")
    @GetMapping("/ranking")
    public ResponseObject countRanking5(@ApiParam("机场代码") @RequestParam("code") String airportCode){
        return ResponseObject.success(countService.countRanking5(airportCode),"成功");
    }

    @ApiOperation("油单类型统计")
    @GetMapping("/flrctype")
    public ResponseObject countFlrcType(@ApiParam("机场代码") @RequestParam("code") String airportCode) {
        return ResponseObject.success(countService.countFlrcType(airportCode),"成功");
    }

    @ApiOperation("占比统计")
    @GetMapping("/ratio")
    public ResponseObject countByRatio(){
        return ResponseObject.success(countService.countByRatio(),"成功");
    }
}
