package com.zh.service;

import com.zh.bean.ResponseObject;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.*;
import com.zh.bean.login.MyStaff;
import com.zh.bean.login.TStaff;

import java.util.List;
import java.util.Map;

public interface FlightService {

    /**
     * 获取航班量（总量，已离，未到，停场）
     */
    Map<String, Object> getFlightAmount(TStaff staff);

    /**
     * 飞机信息接口
     * 取得当前在场航班信息，并将一进一出航班配对。
     */
    List<TFlightInfoList> getFlightList(TFlight flight);

    /**
     * 手动创建航班的同时创建任务
     *
     * @param staff
     * @param taskContent
     */
    MyFlightTask addFlightAndTask(MyFlight flight, MyStaff staff, Integer taskContent, Integer taskStatus);

    /**
     * 取得系统日期和机场代码和机场区域代码检索航班表（DB）的航班日期字段,取得前1天，当天，后1天的航班所有信息
     */
    List<MyFlight> getFlightThreeDateList(MyStaff staff);

    /**
     * 取得系统日期和机场代码和机场区域代码检索航班表（DB）的航班日期字段,取得前7天的航班所有信息
     */
    List<MyFlight> getFlightSevenDateList(MyStaff staff);

    /**
     * 修改航班信息
     *
     * @param task
     */
    void updateFlightInfo(MyFlight flight, MyTask task);

    /**
     * 根据航班ID查询航班详细信息
     */
    MyFlightTask getFlightInfoById(MyFlight flight);

    /**
     * 根据航班号查询航班详细信息
     */
    List<MyFlight> getFlightInfoByFlightNo(MyFlight flight);

    /**
     * 根据航班字段进行检索
     */
    List<MyFlight> getFlightListCondition(MyFlightCondition flight);

    /**
     * pad航班新建
     *
     * @param staff
     */
    void padAddFlightAndTask(MyFlightTask flight, MyStaff staff);

    /**
     * 根据航班号，航班时间查出航班ID，再根据航班ID更新任务表里的加油员ID和任务状态
     *
     * @return
     */
    Map<String, Object> padUpdateTaskState(MyFlight flight, MyTask task);

    /**
     * pad航班新建和油单
     *
     * @param staff
     * @return
     */
    String padAddFlightAndTaskAndOil(MyFlightTask flight, MyFuelRecpt fuelRecpt, MyStaff staff);

    /**
     * PC端根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出,按照本场和前方起飞分开的
     */
    List<MyFlightTask> getFlightDateList(MyStaff staff);

    /**
     * 获取当天的本场航班的航班信息,按照预计起飞时间排序
     */
    List<MyFlightTask> getFlightTaskDateThis(MyStaff staff);

    /**
     * 根据航班公司二字码获取航班公司名
     */
    Map<String, Object> getFlightName(MyAirlinesCode myAirlinesCode);

    /**
     * 根据机场三字码获取机场名
     */
    Map<String, Object> getFlightNames(MyAirportCode airportCode);

    /**
     * 根据航班号查询预创建航班表
     */
    List<MyFlightTemp> getFlightTemp(MyStaff staff, MyFlightTemp myFlightTemp);

    /**
     * PAD 获取当天航班的航班信息,按照预计起飞时间排序
     */
    List<MyFlightTask> getCurrentFlight(MyStaff staff);

    /**
     * 根据机场码获取机场名（远程调用）
     */
    MyAirportCode getAirportName(MyAirportCode airportCode);

    List<MyFlightTask> selectFlightDateList(MyStaff staff);

    /**
     * 查询飞机号码表
     */
    List<MyFlightCode> getFLIGHTCODE();

    /**
     * 查询航空加油客户表
     */
    List<MyCustom> getCUSTOM();

    /**
     * 删除飞机号码表信息
     */
    void deleteFLIGHTCODE(MyFlightCode flightCode);

    /**
     * 删除航空加油客户表信息
     */
    void deleteCUSTOM(MyCustom custom);

    /**
     * 新增飞机号码表信息
     *
     * @return
     */
    Integer addFLIGHTCODE(MyFlightCode flightCode);

    /**
     * 新增航空加油客户表信息
     *
     * @return
     */
    Integer addCUSTOM(MyCustom custom);

    /**
     * 修改飞机号码表信息
     */
    void updateFLIGHTCODE(MyFlightCode flightCode);

    /**
     * 修改航空加油客户表信息
     */
    void updateCUSTOM(MyCustom custom);


    MyFlightCode getFLIGHTCODEFind(MyFlightCode flightCode);

    MyCustom getCUSTOMFind(MyCustom custom);

    /**
     * 获取规划数据
     */
    TProjectData getProjectData(MyFlight flight);

    /**
     * 航显页面修改航班信息
     *
     * @param flight
     */
    void updateOnlyFlightInfo(MyFlight flight, MyStaff staff);

    /**
     * 新建航班闹钟提示
     *
     * @param staff
     */
    MyFlightAlarm addOrUpdateFlightAlarm(MyFlightAlarm flightAlarm, MyStaff staff);

    /**
     * 删除航班闹钟提示
     */
    void deleteFlightAlarm(MyFlightAlarm flightAlarm, MyStaff staff);

    /**
     * 查询航班闹钟提示
     *
     * @param flightAlarm
     */
    List<MyFlightAlarm> getFlightAlarm(MyStaff staff, MyFlightAlarm flightAlarm);

    ResponseObject<Object> getPageFlightList(MyNewFlight flight);

    ResponseObject<Object> getFlightListByNum(MyNewFlight flight);

    ResponseObject<Object> getFlightAndTaskForPad(MyNewFlight flight);

    ResponseObject<Object> queryFlightAndTaskForPad(MyNewFlight flight);

    void cacheFlightAndTaskForPad(MyNewFlight flight);

    void cacheFlightAndTaskForPad(String airportCode);

    Integer pingSingleType(MyFlightTask myFlightTask);

    MyFlight copyNewFlight(MyFlight flight);

    MyFlight insertFlightRemark(MyFlight flight);

    List<TPlacecode> getPlaceCode(TPlacecode placeCode);

    Integer addOrUpdatePlaceCode(TPlacecode placeCode);

    Integer deletePlaceCode(TPlacecodeKey key);

    List<TPlacecodeType> getPlaceCodeType(TPlacecodeType placeCodeType);

    Integer addOrUpdatePlaceCodeType(TPlacecodeType placeCodeType);

    Integer deletePlaceCodeType(String id);

    TPlacecode getOnePlaceCode(TPlacecodeKey key);

    List<MyFlight> getAFlight(String flgtAirportCode);

    void test();

    void test1();

    List<MyFlightTask> orderToFlight(List<TOrderInfo> tOrderInfos, MyStaff staff);

    MyCustom getFlightCodeNew(MyFlightCode flightCode);

    ReturnMsg validCustomNum(MyFlightTask task);

    public int getFlightMaxNum();
}
