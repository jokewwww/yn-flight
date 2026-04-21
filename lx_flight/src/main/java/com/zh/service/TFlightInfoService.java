package com.zh.service;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyTFlightInfo;
import com.zh.bean.login.MyStaff;

public interface TFlightInfoService {
    ReturnMsg<Object> insert(MyTFlightInfo myTFlightInfo, MyStaff staff);

    ReturnMsg<Object> update(MyTFlightInfo myTFlightInfo, MyStaff staff);

    ReturnMsg<Object> sendOne(MyTFlightInfo myTFlightInfo, MyStaff staff);

    ReturnMsg<Object> getAll(MyTFlightInfo myTFlightInfo, MyStaff staff);

    ReturnMsg<Object> delete(MyTFlightInfo myTFlightInfo, MyStaff staff);
}
