package com.zh.service;

import com.zh.bean.flight.TFuelDistribution;
import com.zh.bean.flight.TFuelNo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/9/13 11:25
 * @Description:
 */
public interface TFuelDistributionService {

    /**
     * 获取列表
     *
     * @param tFuelDistribution
     * @return
     */
    List<TFuelDistribution> selectList(TFuelDistribution tFuelDistribution);

    int updateList(@Param("list") List<TFuelNo> record);
}
