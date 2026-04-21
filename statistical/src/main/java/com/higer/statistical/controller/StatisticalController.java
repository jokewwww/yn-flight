package com.higer.statistical.controller;

import com.higer.statistical.entity.MyFuel;
import com.higer.statistical.entity.flight.TTask;
import com.higer.statistical.service.StatisticalService;
import com.higer.statistical.util.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/1/24 09:30
 * @Description: 统计模块
 */
@RestController
@RequestMapping("/system/")
@Api(tags = "API统计分析管理")
public class StatisticalController {

    @Autowired
    private StatisticalService statisticalService;

    @GetMapping(value = "getStatisticalUserMore")
    @ApiOperation(value = "根据加油员统计数据(多人)")
    public ResponseObject<Object> getStatisticalUserMore(
            @ApiParam(value = "taskOpeStaffId 加油员工ID") @RequestParam(value = "taskOpeStaffId",defaultValue = "") List<String> taskOpeStaffId,
            @ApiParam(value = "dateStr type = 1 传输格式 yyyy-MM type = 2 传输格式 yyyy") @RequestParam(value = "dateStr",defaultValue = "") String dateStr,
            @ApiParam(value = "类型type 1按照月查天 2 按照年查月",required = true) @RequestParam("type") Integer type
    ) {
        return statisticalService.getStatisticalUserMore(taskOpeStaffId,type,dateStr);
    }
    @GetMapping(value = "getStatisticalUserOne")
    @ApiOperation(value = "根据加油员统计数据(单人)")
    public ResponseObject<Object> getStatisticalUserOne(
            @ApiParam(value = "taskOpeStaffId 加油员工ID",required = true) @RequestParam(value = "taskOpeStaffId") String taskOpeStaffId,
            @ApiParam(value = "dateStr type = 1 传输格式 yyyy-MM type = 2 传输格式 yyyy",required = true) @RequestParam(value = "dateStr") String dateStr,
            @ApiParam(value = "类型type 1按照月查天 2 按照年查月",required = true) @RequestParam("type") Integer type
    ) {
        return statisticalService.getStatisticalUserOne(taskOpeStaffId,type,dateStr);
    }
    @GetMapping(value = "getStatisticalCompanyMore")
    @ApiOperation(value = "根据航空公司统计数据(多个)")
    public ResponseObject<Object> getStatisticalCompany(
            @ApiParam(value = "flrcAirlName 机场二字码") @RequestParam(value = "flrcAirlName",defaultValue = "") List<String> flrcAirlCode,
            @ApiParam(value = "dateStr type = 1 传输格式 yyyy-MM type = 2 传输格式 yyyy") @RequestParam(value = "dateStr",defaultValue = "") String dateStr,
            @ApiParam(value = "类型type 1按照月查天 2 按照年查月",required = true) @RequestParam("type") Integer type
    ) {
        return statisticalService.getStatisticalCompany(flrcAirlCode,type,dateStr);
    }
    @GetMapping(value = "getStatisticalCompanyMoreDay")
    @ApiOperation(value = "根据  某天  航空公司统计数据(多个)")
    public ResponseObject<Object> getStatisticalCompanyMoreDay(
            @ApiParam(value = "date 传输格式 yyyy-MM-dd") @RequestParam(value = "date",defaultValue = "") String date
    ) {
        return statisticalService.getStatisticalCompanyMoreDay(date);
    }

    @GetMapping(value = "getStatisticalCompanyOne")
    @ApiOperation(value = "根据航空公司统计数据(单个)")
    public ResponseObject<Object> getStatisticalCompanyOne(
            @ApiParam(value = "flrcAirlName 机场二字码",required = true) @RequestParam(value = "flrcAirlName") String flrcAirlCode,
            @ApiParam(value = "dateStr type = 1 传输格式 yyyy-MM type = 2 传输格式 yyyy",required = true) @RequestParam(value = "dateStr") String dateStr,
            @ApiParam(value = "类型type 1按照月查天 2 按照年查月",required = true) @RequestParam("type") Integer type
    ) {
        return statisticalService.getStatisticalCompanyOne(flrcAirlCode,type,dateStr);
    }
    @GetMapping(value = "Test")
    @ApiOperation(value = "Test")
    public ResponseObject<Object> test(
            @RequestParam("taskList[]") List<Integer> list
    ) {
        return statisticalService.test();
    }

    @PostMapping(value = "Test1")
    @ApiOperation(value = "Test1")
    public ResponseObject<Object> test1(
            @RequestBody List<MyFuel> list
    ) {
        return statisticalService.test();
    }
    public static int compute(int a, Function<Integer, Integer> function1, Function<Integer, Integer> function2) {
        return function1.compose(function2).apply(a);
    }
    public static void main(String[] args) {
        int compute = compute(2, value -> value * 3, value -> value * value);
        System.out.println(compute);
    }

}
