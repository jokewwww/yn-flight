package com.zh.service;

import com.zh.bean.flight.SysLog;

import java.util.List;

public interface SysLogService {

    void insert(SysLog sysLog);

    /**
     * 获取列表
     *
     * @param sysLog
     * @return
     */
    List<SysLog> getLogs(SysLog sysLog);
}
