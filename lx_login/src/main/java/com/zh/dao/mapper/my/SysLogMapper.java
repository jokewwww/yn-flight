package com.zh.dao.mapper.my;

import com.zh.bean.flight.SysLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysLogMapper {

    int insert(SysLog sysLog);

    int updateLog(SysLog sysLog);

    List<SysLog> getLogs(SysLog sysLog);

}
