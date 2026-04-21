package com.zh.dao.mapper;

import com.zh.bean.flight.TFlightCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface TFlightCodeMapper {
    int deleteByPrimaryKey(@Param("arcrRegn") String arcrRegn, @Param("arcrStartDate") Date arcrStartDate);

    int insert(TFlightCode record);

    TFlightCode selectByPrimaryKey(@Param("arcrRegn") String arcrRegn, @Param("arcrStartDate") Date arcrStartDate);

    List<TFlightCode> selectAll();

    int updateByPrimaryKey(TFlightCode record);
}
