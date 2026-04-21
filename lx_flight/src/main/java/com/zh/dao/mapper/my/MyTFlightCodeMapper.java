package com.zh.dao.mapper.my;

import com.zh.bean.flight.TFlightCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface MyTFlightCodeMapper {
    int deleteByPrimaryKey(@Param("arcrRegn") String arcrRegn, @Param("arcrStartDate") Date arcrStartDate);

    int insert(TFlightCode record);

    TFlightCode selectByPrimaryKey(@Param("arcrRegn") String arcrRegn, @Param("arcrStartDate") Date arcrStartDate);

    List<TFlightCode> selectAll();

    int updateByPrimaryKey(TFlightCode record);

    TFlightCode selectByArcrRegn(@Param("arcrRegn") String arcrRegn);

    TFlightCode selectByArcrRegnAndFlno(@Param("arcrRegn") String arcrRegn, @Param("flno") String flno);
}
