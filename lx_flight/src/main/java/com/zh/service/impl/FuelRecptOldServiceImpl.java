package com.zh.service.impl;

import com.zh.bean.flight.MyFuelRecptOld;
import com.zh.dao.mapper.my.MyFuelRecptOldMapper;
import com.zh.service.FuelRecptOldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuelRecptOldServiceImpl implements FuelRecptOldService {

    @Autowired
    private MyFuelRecptOldMapper fuelRecptOldMapper;

    @Override
    public List<MyFuelRecptOld> getFuelHistory(MyFuelRecptOld fuelRecpt) {
        return fuelRecptOldMapper.getFuelHistory(fuelRecpt);
    }
}
