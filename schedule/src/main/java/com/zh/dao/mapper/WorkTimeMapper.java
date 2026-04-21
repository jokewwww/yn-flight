package com.zh.dao.mapper;

import com.zh.entity.SetEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkTimeMapper {

    SetEntity getSetting();


}
