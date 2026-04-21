package com.zh.service.impl;

import com.zh.bean.flight.SysDict;
import com.zh.dao.mapper.my.SysDictMapper;
import com.zh.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/9/13 11:26
 * @Description:
 */
@Service
public class SysDictServiceImpl implements SysDictService {

    @Autowired
    private SysDictMapper sysDictMapper;

    @Override
    public List<SysDict> selectList(SysDict sysDict) {
        return sysDictMapper.selectList(sysDict);
    }

}
