package com.zh.dao.mapper.my;

import com.zh.bean.flight.MyTFlightInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TFlightInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(MyTFlightInfo record);

    int insertSelective(MyTFlightInfo record);

    MyTFlightInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MyTFlightInfo record);

    int updateByPrimaryKey(MyTFlightInfo record);

    List<MyTFlightInfo> getAll(MyTFlightInfo record);

    List<MyTFlightInfo> getAllSelective(MyTFlightInfo record);
}