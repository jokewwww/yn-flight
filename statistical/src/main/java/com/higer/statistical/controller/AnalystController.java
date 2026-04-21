package com.higer.statistical.controller;

import com.higer.statistical.service.AnalystService;
import com.higer.statistical.util.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "数字大屏接口设计文档")
@RestController("/API")
public class AnalystController {

    @Autowired
    private AnalystService analystService;

    @ApiOperation("获取航加站统计信息接口")
    @GetMapping("/analyst")
    public ResponseObject analyst(@ApiParam(name="code") @RequestParam(value="code",required = false) String code){
        return ResponseObject.success(analystService.getCountInfo(code),"查询成功");

    }

    @ApiOperation("财务流水")
    @GetMapping("/finance")
    public ResponseObject finance(@ApiParam(name="code") @RequestParam(value="code",required = false) String code){
        return ResponseObject.success(analystService.getFinanceInfo(code),"查询成功");
    }

    @ApiOperation("当前任务地区")
    @GetMapping("/task")
    public ResponseObject task(@ApiParam(name="code") @RequestParam(value="code",required = false) String code){
       return ResponseObject.success( analystService.getTaskInfo(code),"查询成功");
    }

    @ApiOperation("任务峰值")
    @GetMapping("/taskAmount")
    public ResponseObject taskAmount(@ApiParam(name="code") @RequestParam(value="code",required = false) String code){
        return ResponseObject.success(analystService.getTaskAmount(code),"查询成功");
    }
}
