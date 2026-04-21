package com.higer.statistical.controller;

import com.higer.statistical.service.ExportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "Excel导出相关接口")
@Controller
public class ExportController {

    @Autowired
    private ExportService exportService;

    @ApiOperation(value = "Excel导出接口",produces="application/octet-stream")
    @GetMapping("/export")
    public void export1(
            @ApiParam(value = "查询的taskOpeStaffId或者flrcAirlCode") @RequestParam(value = "id",defaultValue = "") String id,
            @ApiParam(value = "类型type 1按照月查天 2 按照年查月",required = true) @RequestParam("type") Integer type,
            @ApiParam(value="导出类型 1-导出需要的 加油员数据,2-导出需要的航空公司数据",required = true) @RequestParam("exportType") Integer exportType,
            HttpServletRequest request, HttpServletResponse response){
        exportService.export(id,type,exportType,request,response);
    }
}
