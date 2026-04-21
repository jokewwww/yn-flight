package com.zh.dao.mapper.my;

import com.zh.bean.login.TParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TParamMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TParam record);

    int insertSelective(TParam record);

    TParam selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TParam record);

    int updateByPrimaryKey(TParam record);

    List<TParam> selectDefaultData();

    TParam selectByAirportCode(@Param("pName") String pName, @Param("flgtAirportCode") String flgtAirportCode, @Param("pType") Integer pType);

    //查全部
    List<TParam> findAll(@Param("flgtAirportCode") String flgtAirportCode);

    //根据 类型 和 机场代码 查询
    List<TParam> findByPTypeAndFlgtAirportCode(@Param("pType") Integer pType, @Param("flgtAirportCode") String flgtAirportCode);

    TParam selectByPName(@Param("pName") String pName);

}