package com.zh.service;

import com.zh.bean.OperationLog;

import java.util.List;

public interface LogOperationService {
    int logOperationInsert(OperationLog fuel);

    List<OperationLog> selectAll();

}
