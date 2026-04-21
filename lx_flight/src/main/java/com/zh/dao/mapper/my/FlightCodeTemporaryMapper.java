package com.zh.dao.mapper.my;

import com.zh.bean.flight.MyCustom;
import com.zh.bean.flight.MyFlightCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FlightCodeTemporaryMapper {

    List<MyFlightCode> getCstmNameAndArcrAcnameByArcrRegnTemporary(@Param("flgtRegn") String flgtRegn, @Param("flgtFlno") String flgtFlno);

    MyCustom getCstmName(@Param("cstmNum") String cstmNum);

}
