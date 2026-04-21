package com.zh.service;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.*;
import com.zh.bean.login.MyStaff;
import com.zh.bean.login.MyStaffVehiTask;
import com.zh.bean.login.TStaff;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface FuelRecptService {

    List<TfuelTotal> findRecptCont(TStaff tStaff);

    List<TfuelFight> findfightoil(TStaff tStaff);

    List<TFlightPlace> findflight(TFlight tFlight);

    TFlightInfo finddetails(TFlightInfo tFlightInfoList);

    List<Map<String, Object>> findStaffTask(MyStaff staff);

    List<MyFuelRecpt> findfuelrecpt(MyStaff staff, MyFuelRecpt myFuelRecpt);

    List<MyFuelRecpt> findtodayfuelrecpt(MyStaff staff);

    List<Map<String, Object>> selectTaskAndRecpt(MyStaff staff, Integer dayType);

    void updatefuel(MyFuelRecpt myFuelRecpt, MyStaff staff);

    void uprecyclefuel(MyFuelRecpt myFuelRecpt);

    ReturnMsg<List<String>> auditfuel(MyFuelId flrcId);

    ReturnMsg<Object> mark(MyFuelMark myFuelMark);

    MyFuelRecpt findFuellist(MyFuelRecpt myFuelRecpt);

    List<MyFuelRecpt> findfuelretirevelist(MyFuelRecpt myFuelRecpt);

    void findFuelrecptlist(MyFuelRecpt myFuelRecpt, MyTask task, MyStaff staff);

    Integer UpFuelRecpt(MyFuelRecpt fuelRecpt, MyStaff staff);

    void upFuelIn(MyFUel fuel, MyFuelRecpt fuelRecpt);

    List<MyFuelRecpt> fuelretirevelist(MyFuelRecpt fuelRecpt);

    MyTask gettaskById(String sfvhStaffId);

    List<MyTask> gettaskListById(List<MyStaffVehiTask> list);

    Object finsbzstaff(String staffId);

    void ForiceUpdateTaskStatus(MyTask task);

    Map<String, Object> upstatus(MyFlightTask task);

    List<MyFuelRecpt> findlistfuel();

    void readflight(MyFsubscription fsubscription, MyStaff staff);

    void DissFlight(MyFsubscription fsubscription, MyStaff staff);

    ArrayList<Object> flightcode(MyStaff staff);

    void insertFlightTemp(MyFlightTemp myFlightTemp);

    void updateFlightTemp(MyFlightTemp myFlightTemp);

    void deleteFlightTemp(MyFlightTemp myFlightTemp);

    List<MyFlightTemp> selectFlightTemp();

    MyFlightTemp selectFlightTempFind(MyFlightTemp myFlightTemp);

    ArrayList<Map<String, Object>> flightcodeNew(MyStaff staff);

    Map<String, Object> updateFuelPad(MyFuelRecpt fuelRecpt, MyStaff staff);

    MyFuelRecpt UpFuelRecptPc(MyFuelRecpt fuelRecpt, MyStaff staff);

    ReturnMsg<TaskAndRecpt> handleTaskAndRecpt(List<TaskAndRecpt> list);

    List<MyFlightTask> getTodayFligtTasklist(String flgtAirportCode);

    Integer findDoingTask(String staffId);

    void cancelFuel(String flrcId);

    void recoveryFuel(String flrcId, int flrcTakebackFlg);

    Map<String, Object> findNewFuelParam(TFuelNo tFuelNo, MyStaff staff);

    Map<String, Object> getFuelPad(TFuelDistribution tFuelDistribution);

    Integer recycleFuelPad(List<TFuelNo> tFuelNo, String padId);

    Integer recycleFuelPc(List<TFuelNo> tFuelNo);

    List<Map<String, Object>> selectByDateTaskAndRecpt(MyFuelRecpt myFuelRecpt, MyStaff staff);

    Map<String, Object> pcUpdateFuel(MyFuelRecpt fuelRecpt, MyStaff staff);

    MyFuelRecpt resend(MyFuelRecpt fuelRecpt, MyStaff staff);

    String findRecptBase(String flrcId);


    List<MyStaff> findStaffByName(MyStaff staff);


    MyFuelRecpt getFuelData(MyFuelVo fuelVo);

    MyFuelRecpt deleteFuel(MyFuelRecpt fuelRecpt, MyStaff staff);

    void test();

    void printFuel(MyFuelRecpt myFuelRecpt, String staffId);
}