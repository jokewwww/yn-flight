package com.zh.dao.mapper.my;

import com.zh.bean.flight.*;
import com.zh.bean.login.MyStaffVehiTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyFuelRecptMapper {


    List<TfuelTotal> findRecptCont(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    List<TfuelFight> findfightoil(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    List<TFlightPlace> findflight(TFlight tFlight);

    TFlightInfo flightdeta(TFlightInfo tFlightInfoList);

    MyFlightTask flightask(TFlightInfo tFlightInfoList);

    TFlightInfo flightdetails(TFlightInfo tFlightInfoList);

    MyFlightTask flightaskl(TFlightInfo tFlightInfoList);

    List<MyFlightTask> findStaffTask(@Param("staffId") String staffId);

    List<MyFlightCode> getCstmNameAndArcrAcnameByArcrRegn(@Param("flgtRegn") String flgtRegn);

    List<MyFuelRecpt> findfuelrecpt(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    List<MyFuelRecpt> findtodayfuelrecpt(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    int updatefuel(MyFuelRecpt myFuelRecpt);

    int uprecyclefuel(MyFuelRecpt myFuelRecpt);


    MyFuelRecpt findFuellist(MyFuelRecpt myFuelRecpt);


    MyTask gettaskById(@Param("sfvhStaffId") String sfvhStaffId);

    List<MyTask> gettaskListById(@Param("list") List<MyStaffVehiTask> list, @Param("date") String date);

    MyFlightTask findtasklist(@Param("taskId") String taskId);

    //加油单最大编号
    String getMaxflrcNo();

    int upFuelrecpt(MyFuelRecpt fuelRecpt);


    int upfuelIn(MyFuelRecpt fuelRecpt);

    List<MyFuelRecpt> findfuelretirevelist(MyFuelRecpt fuelRecpt);


    int updateFueltask(@Param("flrcNo") String flrcNo, @Param("taskChagEndTime") String taskChagEndTime, @Param("taskId") String taskId, @Param("taskStatus") Integer taskStatus);

    int updatefueltaskvehi(@Param("taskDoneTime") String taskDoneTime, @Param("taskId") String taskId);

    String findflightno(@Param("staffId") String staffId);

    Integer findstaffcount(@Param("staffId") String staffId);

    Integer findstafffinsh(@Param("staffId") String staffId);

    //根据加油单编号获取单条任务信息
    MyTask getTaskInfo(@Param("flrcNo") String flrcNo);

    int updateFuelstatus(@Param("taskId") String taskId);

    //修改区域代码 (任务表)
    int updateAirprtcodeTask(@Param("staffAptareaCode") String staffAptareaCode, @Param("taskAptareaCode") String taskAptareaCode);

    //修改任务状态为 4
    int updateTaskStatus(@Param("taskId") String taskId);

    //修改区域代码 (航班表)
    int updateAirprtcodeFlight(@Param("staffAptareaCode") String staffAptareaCode, @Param("taskAptareaCode") String taskAptareaCode, @Param("forMatTime") String forMatTime);

    //强制修改任务状态为已接受
    int ForiceUpdateTaskStatus(@Param("taskId") String taskId);

    int upstatus(@Param("flgtId") String flgtId, @Param("taskStatus") Integer taskStatus);


    List<MyFuelRecpt> findlistfuel();

    Integer selectflrcno(@Param("flrcNo") String flrcNo);

    Integer selectflrcregn(@Param("flrcAircrftNo") String flrcAircrftNo);

    Integer selectflrcflno(@Param("flrcFlightNo") String flrcFlightNo);

    Integer selectstaffname(@Param("flrcDeliverName") String flrcDeliverName);


    void DissFlight(@Param("staffId") String staffId, @Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode, @Param("flgtRflno") String flgtRflno, @Param("flgtRflop") String flgtRflop);

    void readflight(@Param("staffId") String staffId, @Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode, @Param("flgtRflno") String flgtRflno, @Param("flgtRflop") String flgtRflop);


    List<MyAirportCode> flightcode(@Param("staffAirportCode") String staffAirportCode);

    MyFlightCode selectcustom(@Param("flrcAircrftNo") String flrcAircrftNo);

    int insertFlightTemp(MyFlightTemp myFlightTemp);

    int updateFlightTemp(MyFlightTemp myFlightTemp);

    int deleteFlightTemp(@Param("flgtFlno") String flgtFlno);

    List<MyFlightTemp> selectFlightTemp();

    MyFlightTemp selectFlightTempFind(@Param("flgtFlno") String flgtFlno);

    int auditfuel(@Param("flrcId") String flrcId);

    //更改对应的任务信息中的油单上传时间
    int updateTask(@Param("taskId") String taskId, @Param("format2") String format2);

    int updateByPrimaryKeySelective(MyFuelRecpt fuelRecpt);

    MyFuelRecpt findFuelByNo(@Param("flrcNo") String flrcNo);

    List<MyFlightTask> getTodayFligtTasklist(@Param("flgtAirportCode") String flgtAirportCode);
}
