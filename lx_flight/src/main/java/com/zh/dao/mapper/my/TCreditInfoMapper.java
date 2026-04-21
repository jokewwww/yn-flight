package com.zh.dao.mapper.my;

import com.zh.bean.flight.TCreditInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TCreditInfoMapper {
    int deleteByPrimaryKey(String cstno);

    int insert(TCreditInfo record);

    int insertSelective(TCreditInfo record);

    TCreditInfo selectByPrimaryKey(String cstno);

    int updateByPrimaryKeySelective(TCreditInfo record);

    int updateByPrimaryKey(TCreditInfo record);

    List<TCreditInfo> selectCreditInfo(TCreditInfo record);
}