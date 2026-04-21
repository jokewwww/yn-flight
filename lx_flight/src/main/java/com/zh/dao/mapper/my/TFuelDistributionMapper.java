package com.zh.dao.mapper.my;

import com.zh.bean.flight.TFuelDistribution;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TFuelDistributionMapper {

    /**
     * 获取列表
     *
     * @param tFuelDistribution
     * @return
     */
    List<TFuelDistribution> selectList(TFuelDistribution tFuelDistribution);

    int insertList(@Param("list") List<TFuelDistribution> record);

    int updateList(@Param("list") List<TFuelDistribution> record);

    List<TFuelDistribution> selectByFuelNoList(@Param("list") List<String> fuelNoList);
}