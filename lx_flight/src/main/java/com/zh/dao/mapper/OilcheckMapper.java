package com.zh.dao.mapper;

import com.zh.bean.auto.Oilcheck;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OilcheckMapper {
    int deleteByPrimaryKey(String id);

    int insert(Oilcheck record);

    int insertSelective(Oilcheck record);

    Oilcheck selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Oilcheck record);

    int updateByPrimaryKeyWithBLOBs(Oilcheck record);

    int updateByPrimaryKey(Oilcheck record);
}