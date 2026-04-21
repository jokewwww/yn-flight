package com.zh.dao.mapper.my;

import com.zh.bean.flight.TOrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TOrderInfoMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(TOrderInfo record);

    int insertSelective(TOrderInfo record);

    TOrderInfo selectByPrimaryKey(Long orderId);

    TOrderInfo selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(TOrderInfo record);

    int updateByPrimaryKey(TOrderInfo record);

    List<TOrderInfo> selectOrderInfo();

    List<TOrderInfo> selectOrderInfoByAir(@Param("apc3") String apc3);

    List<TOrderInfo> selectOrderInfoByRegnAndNo(TOrderInfo record);
}

