package com.zh.dao.mapper.my;

import com.zh.bean.zuul.SysRequestLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysRequestLogMapper {
    int insertLog(SysRequestLog log);
}