package com.zh.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.zh.bean.flight.SysLog;
import com.zh.dao.mapper.my.SysLogMapper;
import com.zh.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    public static void main(String[] args) {
        LocalDate a = LocalDate.now();
        LocalDate b = LocalDate.parse("2017-12-31");
        System.out.println(b);
    }

    @Override
    public void insert(SysLog sysLog) {
        String requestBody = sysLog.getRequestBody();
        requestBody = requestBody.replaceAll("\"flrcSign.+?\",\"", "\"")
                .replaceAll("\"flrcSingle.+?\",\"", "\"")
                .replaceAll("\"flrcSingleNew.+?\",\"", "\"");
        sysLog.setRequestBody(requestBody);

        String responseBody = sysLog.getResponseBody();
        responseBody = responseBody.replaceAll("\"flrcSign.+?\",\"", "\"")
                .replaceAll("\"flrcSingle.+?\",\"", "\"")
                .replaceAll("\"flrcSingleNew.+?\",\"", "\"");
        sysLog.setResponseBody(responseBody);

        sysLogMapper.insert(sysLog);
        if (ObjectUtil.isNotNull(sysLog.getFlNo()) && ObjectUtil.isNotNull(sysLog.getTaskId())) {
            sysLogMapper.updateLog(sysLog);
        }

    }

    @Override
    public List<SysLog> getLogs(SysLog sysLog) {
        return sysLogMapper.getLogs(sysLog);
    }
}
