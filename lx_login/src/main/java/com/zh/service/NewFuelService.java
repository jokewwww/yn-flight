package com.zh.service;

import com.zh.bean.login.MyFuel;
import com.zh.bean.login.MyStaff;

import java.util.List;

public interface NewFuelService {

    List<MyFuel> findNewfuels(MyStaff staff);

    int saveNewFuels(List<MyFuel> fuelList, MyStaff staff);

    List<MyFuel> findNewFuelParam(MyStaff staff);

    List<MyFuel> findNewFuelFuture(MyStaff staff);
}
