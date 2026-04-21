package com.zh.dao.mapper;

import com.zh.bean.flight.SysLog;
import com.zh.bean.zuul.SysRequestLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysLogMapper {

    int deleteLog(SysLog sysLog);

    int deleteSysRequestLog(SysRequestLog sysRequestLog);


}
