package com.zh.service;

import com.zh.bean.flight.SysDict;

import java.util.List;

/**
 * @Auther:
 * @Date: 2019/9/13 11:25
 * @Description:
 */
public interface SysDictService {

    /**
     * 获取列表
     *
     * @param sysDict
     * @return
     */
    List<SysDict> selectList(SysDict sysDict);
}
