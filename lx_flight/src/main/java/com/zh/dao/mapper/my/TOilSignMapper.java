package com.zh.dao.mapper.my;


import com.zh.bean.flight.TOilSign;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TOilSignMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TOilSign record);

    int insertSelective(TOilSign record);

    TOilSign selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TOilSign record);

    int updateByPrimaryKeyWithBLOBs(TOilSign record);

    int updateByPrimaryKey(TOilSign record);

    List<TOilSign> select();

    TOilSign selectByFlgtId(String flgtId);
}