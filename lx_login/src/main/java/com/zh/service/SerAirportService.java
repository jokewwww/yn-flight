package com.zh.service;

import com.zh.bean.login.MyServAirport;
import com.zh.bean.login.MyStaff;

import java.util.List;

public interface SerAirportService {

    MyServAirport findfwflight(String svapAptplacNo);

    String getAirportAreaCode(String svapAptplacNo, String getAirportAreaCode);

    List<MyServAirport> selectairport(MyStaff staff);

    void insertairport(MyServAirport servAirport);

    void deleteairport(MyServAirport servAirport, MyStaff staff);

    void synchronizationAirport(MyServAirport servAirport, MyStaff staff);

    List<MyServAirport> selectServAirport(MyServAirport servAirport);
}
