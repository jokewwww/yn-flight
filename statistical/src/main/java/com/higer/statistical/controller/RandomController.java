package com.higer.statistical.controller;

import com.higer.statistical.service.RandomService;
import com.higer.statistical.util.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "随机数接口")
@RestController
public class RandomController {

    @Autowired
    private RandomService randomService;

    @GetMapping("/random")
    @ApiOperation(value = "生成随机数")
    public ResponseObject test(){
        randomService.test();
        return ResponseObject.success(null,"生成成功");
    }

}
