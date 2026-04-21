package com.example.jobschedual.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.jobschedual.entity.TScheduling;
import com.example.jobschedual.entity.TStaff;
import com.example.jobschedual.service.TSchedulingService;
import com.example.jobschedual.util.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "排班人员查询修改接口")
@RestController
@RequestMapping("/scheduling")
public class TSchedulingController {

    @Autowired
     private TSchedulingService tSchedulingService;

    @PostMapping("/modify")
    @ApiOperation(value = "根据组修改排班人员")
    public ResponseObject modify(
            @ApiParam(value = "组名",required = true) @RequestParam(name = "groupId") String groupId,
            @ApiParam(value = "人员集合",required = true)@RequestBody List<TScheduling> schedulings){
//        tSchedulingService.modify(groupId, JSONArray.parseArray(schedulings, TScheduling.class));
        tSchedulingService.modify(groupId,schedulings);
        return ResponseObject.success(null,"修改成功");
    }
}
