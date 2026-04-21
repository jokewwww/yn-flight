package com.zh.service;

import com.zh.bean.flight.MyTask;
import com.zh.bean.login.*;

import java.util.List;

public interface FuelService {

    TFuel findfuel(TStaff tStaff);

    List<MyFuel> findfuelist(MyStaff staff);

    String setFuel(MyFuel fuel, MyStaff staff);

    TFuel findfuels(MyTask task);

    void updatestaff(MyStaff staff);

    void updateAiprortSer(MyStaff staff, MyTask task);

    MyStaffVehi selectstaffvehi(MyStaffVehi staffvehi);

    void updatefuelLogicDelFlg(MyFuel fuel);

    String setNewFuel(List<MyFuel> listfuel, MyStaff staff);
}
