package com.zh.service;

import com.zh.bean.flight.MyFuelRecptOld;

import java.util.List;

public interface FuelRecptOldService {

    /**
     * 获取列表
     *
     * @param fuelRecpt
     * @return
     */
    List<MyFuelRecptOld> getFuelHistory(MyFuelRecptOld fuelRecpt);
}