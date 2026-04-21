package com.zh.dao.mapper.my;

import com.zh.bean.flight.SysDict;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysDictMapper {

    /**
     * 获取列表
     *
     * @param sysDict
     * @return
     */
    List<SysDict> selectList(SysDict sysDict);
}