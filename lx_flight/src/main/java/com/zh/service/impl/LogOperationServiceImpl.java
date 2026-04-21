package com.zh.service.impl;


import com.zh.bean.OperationLog;
import com.zh.dao.mapper.my.LogMapper;
import com.zh.service.LogOperationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogOperationServiceImpl implements LogOperationService {

    private final static Logger log = LoggerFactory.getLogger(LogOperationServiceImpl.class);

    @Autowired
    private LogMapper logMapper;

    @Override
    public int logOperationInsert(OperationLog log) {
        return logMapper.logOperationInsert(log);
    }

    @Override
    public List<OperationLog> selectAll() {
        return logMapper.selectAll();
    }


}
