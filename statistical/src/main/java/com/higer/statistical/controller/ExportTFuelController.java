package com.higer.statistical.controller;

import com.higer.statistical.service.ExportFuelTxtService;
import com.higer.statistical.util.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * 油单导出
 */
@Controller
@Api(tags = "油单导出接口")
public class ExportTFuelController {

    @Autowired
    private ExportFuelTxtService exportFuelTxtService;
    @ApiOperation(value="导出txt",notes = "导出指定格式的txt油单文件")
    @PostMapping("/exportTxt")
    public void exportFuel( @ApiParam(value="导出油单编号",required = true) @RequestBody String[] ids,
            HttpServletRequest request, HttpServletResponse response){
        exportFuelTxtService.exportTxt(ids,request,response);
    }


    @ApiOperation(value="导出txtadvance",notes = "导出指定格式的txt油单文件")
    @PostMapping("/exportTxtAdvance")
    @ResponseBody
    public ResponseObject<Object> exportTxtAdvance(@ApiParam(value="导出油单编号",required = true) @RequestBody String[] ids,
                                                   HttpServletRequest request, HttpServletResponse response){
       return exportFuelTxtService.exportTxtAdvance(ids,request,response);
    }
}
