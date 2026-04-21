package com.zh.dao.mapper.my;

import com.zh.bean.flight.TAirportCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyTAirportCodeMapper {
    int deleteByPrimaryKey(String apcdIataCode);

    int insert(TAirportCode record);

    TAirportCode selectByPrimaryKey(String apcdIataCode);

    List<TAirportCode> selectAll();

    int updateByPrimaryKey(TAirportCode record);

    /**
     * 根据中航油内部机场编号查询机场代码信息
     *
     * @param apcdCnafAirportCode 中航油内部机场编号
     * @return
     */
    TAirportCode selectByCnafAirportCode(String apcdCnafAirportCode);

    List<TAirportCode> selectGroupApcdCnafAirportCode();
}
