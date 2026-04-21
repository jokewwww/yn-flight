package com.zh.dao.mapper.my;


import com.zh.bean.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface LogMapper {
    int logOperationInsert(@Param("operation") OperationLog fuel);

    List<OperationLog> selectAll();
}
