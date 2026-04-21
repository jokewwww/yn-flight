package com.zh.service;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyFlight;
import com.zh.bean.flight.MyFlightTask;

public interface IExcelService {

    ReturnMsg<String> importTFlight(byte[] file, String airportCode);

    public MyFlightTask addFlightAndTask(MyFlight aFlight, MyFlight dFlight);
}
