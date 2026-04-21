package com.zh.controller;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.SysLog;
import com.zh.constant.Constant;
import com.zh.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * duquanhong
 */
@RestController
@RequestMapping(value = "/sys/log")
@Api(tags = "任务操作日志")
public class SysLogController extends BaseController {

    @Autowired
    private SysLogService sysLogService;

    /**
     * 查询列表
     */
    @PostMapping(value = "/list")
    @ApiOperation(value = "查询列表")
    public ReturnMsg<List<SysLog>> getList(@RequestBody SysLog sysLog) {
        List<SysLog> list = sysLogService.getLogs(sysLog);
        return new ReturnMsg<List<SysLog>>(Constant.CODE_OK, null, list);
    }

}
