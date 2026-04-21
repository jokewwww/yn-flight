package com.zh.dao.mapper.my;

import com.zh.bean.flight.TFuelNo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TFuelNoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TFuelNo record);

    int insertList(@Param("list") List<TFuelNo> record);

    int insertSelective(TFuelNo record);

    TFuelNo selectByPrimaryKey(Integer id);

    TFuelNo selectByFlrcNo(String no);

    int updateByPrimaryKeySelective(TFuelNo record);

    int updateByPrimaryKey(TFuelNo record);

    int updateByFuelNo(TFuelNo record);

    TFuelNo selectMaxNO(@Param("fuelType") Integer fuelType, @Param("remark") String remark);

    List<TFuelNo> selectByAll(TFuelNo record);

    //查找最新没用过的油单编号
    TFuelNo selectMaxNOByStatus(@Param("fuelType") Integer type, @Param("remark") String aptareaCode);

    List<TFuelNo> selectMaxNOByStatusNew(@Param("remark") String aptareaCode);

    List<TFuelNo> selectNOByStatusNew(@Param("aptareaCode") String aptareaCode, @Param("fuelType") Integer fuelType);

    List<TFuelNo> getMaxFuelNo(@Param("remark") String staffAirportCode);

    int updateStatus(@Param("list") List<TFuelNo> tFuelNos, @Param("status") Integer status, @Param("oldStatus") Integer oldStatus);

    int automaticRecover(@Param("flrcNo") String flrcNo, @Param("status") Integer status);

    int updateBatch(@Param("list") List<TFuelNo> tFuelNos);

}