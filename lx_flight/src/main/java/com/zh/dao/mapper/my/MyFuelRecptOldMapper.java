package com.zh.dao.mapper.my;

import com.zh.bean.flight.MyFuelRecptOld;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyFuelRecptOldMapper {

    int upFuelrecpt(MyFuelRecptOld fuelRecpt);

    List<MyFuelRecptOld> getFuelHistory(MyFuelRecptOld fuelRecpt);
}
