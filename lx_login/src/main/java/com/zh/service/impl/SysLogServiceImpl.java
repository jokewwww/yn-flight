package com.zh.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.zh.bean.flight.SysLog;
import com.zh.dao.mapper.my.SysLogMapper;
import com.zh.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public void insert(SysLog sysLog) {
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
