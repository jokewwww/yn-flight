package com.example.jobschedual.controller;

import com.example.jobschedual.entity.RedisEntity;
import com.example.jobschedual.factory.QueueInterface;
import com.example.jobschedual.redis.RedisService;
import com.example.jobschedual.service.TaskService;
import com.example.jobschedual.service.WorkStaffService;
import com.example.jobschedual.util.ResponseObject;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/3/13 13:55
 * @Description:
 */
@Api(description = "排班接口")
@RestController
@RequestMapping("/jobschedual")
public class WorkStaffController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private WorkStaffService workStaffService;

    @ApiOperation("设置班组人员")
    @PostMapping("/set")
    public ResponseObject setClasses(
        @RequestBody RedisEntity redisEntity
            ){
        redisService.setStr(redisEntity.getClassName(),redisEntity.getStaffId());
        return ResponseObject.success(null,"更新成功");
    }


    @ApiOperation("删除指定班组")
    @PostMapping("/delete")
    public ResponseObject delete(
            @RequestBody RedisEntity redisEntity
    ){
        if(StringUtils.isEmpty(redisEntity.getClassName())){
            return ResponseObject.error("班组名称不可为空");
        }
        redisService.del(redisEntity.getClassName());
        return ResponseObject.success(null,"更新成功");
    }


    @ApiOperation("查询全部班组人员")
    @GetMapping("/getAll")
    public ResponseObject getAllClasses(){
        return ResponseObject.success(redisService.findAll(),"查询成功");
       // return ResponseObject.success(new HashMap<>(),"查询成功");
    }

    @ApiOperation("查询指定班组人员")
    @GetMapping("/getOne")
    public ResponseObject getOneClasses(
            @ApiParam(value="班组名称" ,required = true) @RequestParam("classes") String classes
    ){
        HashMap<String, Object> map = Maps.newHashMap();
        map.put(classes,redisService.getStr(classes));
        return ResponseObject.success(map,"查询成功");
    }

    @ApiOperation("sipang")
    @GetMapping("/sipang")
    public String sipang(
    ){
       return "test";
    }


    @ApiOperation("test")
    @GetMapping("/test")
    public void test(
    ){
        workStaffService.test();
    }

    @ApiOperation("init初始化的方法")
    @GetMapping("/init")
    public ResponseObject init(
    ){
        workStaffService.init();
        return ResponseObject.success(null,"初始化成功");
    }
    @GetMapping("/test1")
    public String test1(
    ){
        return workStaffService.tests();
    }

    public static void main(String[] args) {
      String[] a = {"1","2","3","null","4"};
        String[] s1 =  Arrays.stream(a).filter(s -> !"null".equals(s)).toArray(String[]::new);
        String collect = Arrays.stream(s1).collect(Collectors.joining(","));

        System.out.println(collect);
    }


}
