package com.zh.dao.mapper.my;

import com.zh.bean.flight.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface FlightMapper {

    //以航班日期=当前系统时间从DB查询出航班量_总数
    Integer getFlightSum(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //以航班日期=当前系统，并且进离港=出港，实际起飞时间!=空为条件从DB查询出航班量_已离
    Integer getleaveFlightSum(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //以航班日期=当前系统，并且进离港=进港，实际到达时间!=空为条件从DB查询出航班量_未到
    Integer getnonArrivalFlightSum(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //以航班日期=当前系统，并且进离港=出港，实际起飞时间==空为条件从DB查询出航班量_停场
    Integer getstopFlightSum(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //新本场航班数量查询 方法见mapper
    Integer getstopFlightSumNew(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);


    //单出在场：以（所属机场代码＝输入_机场代码，航班日期＝系统日期，进离港＝出港，计划到达时间＝空，实际起飞时间＝空）的条件，
    //取得航班表（DB）中的对象记录，写入输出接口。
    List<TFlight> getFlightList(@Param("flgtAirportCode") String flgtAirportCode);

    //单进在场：以（所属机场代码＝输入_机场代码，航班日期＝系统日期，进离港＝进港，实际到达时间≠空）的条件，取得航班表（DB）中的对象记录
    List<TFlight> getFlightListTwo(@Param("flgtAirportCode") String flgtAirportCode);

    //查询（飞机号码＝①_飞机号码，进离港＝出港，计划到达时间＝①_计划到达时间）的记录
    Integer getFlightCount(@Param("tFlightTwo") TFlight tFlightTwo);

    //以（所属机场代码＝输入_机场ID，航班日期＝系统日期，进离港＝出港，计划到达时间≠空，实际起飞时间＝空）的条件，取得航班表（DB）中的对象记录
    List<TFlight> getFlightListFour(@Param("flgtAirportCode") String flgtAirportCode);

    //查询（飞机号码＝flightListFour集合中的飞机号码，进离港＝进港，计划到达时间＝flightListFour集合中的计划到达时间，实际到达时间≠空）的记录
    Integer getFlightCountTwo(@Param("tFlightFour") TFlight tFlightFour);

    //添加航班信息,判断如果返回等于1说明添加成功，否则添加失败
    int addFlight(@Param("flight") MyFlight flight);

    //根据不同的机场三字码去DB中查出对应的机场名称
    String getAirportName(@Param("airport3c") String airport3c);

    //根据航空公司二字码去DB中查出对应的航空公司名称
    String getCompanyName(@Param("flgtAl2c") String flgtAl2c);

    //根据航空公司二字码去DB中查出对应的航空公司简名称
    String getCompanyNames(@Param("flgtAl2c") String flgtAl2c);


    //添加任务信息,判断如果返回等于1说明添加成功，否则添加失败
    int addTask(@Param("task") MyTask task);

    //添加任务信息,判断如果返回等于1说明添加成功，否则添加失败
    int addTaskNew(@Param("task") MyTask task);


    //取得系统日期和机场代码和机场区域代码检索航班表（DB）的航班日期字段,取得前1天，当天，后1天的航班所有信息
    List<MyFlight> getFlightThreeDateList(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //取得系统日期和机场代码和机场区域代码检索航班表（DB）的航班日期字段,取得前7天的航班所有信息
    List<MyFlight> getFlightSevenDateList(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //更新航班信息
    int updateFlightInfo(@Param("flight") MyFlight flight);

    //更新航班备注字段 , 不影响之前的逻辑
    int updateFlightRemark(@Param("flight") MyFlight flight);

    //根据航班ID查出航班单条记录
    MyFlightTask getFlightInfoById(@Param("flgtId") String flgtId);

    //根据航班号查询

    List<MyFlight> getFlightInfoByFlightNo(@Param("flight") MyFlight flight);

    //修改任务中的航班号
    int updateTaskInfo(@Param("flgtId") String flgtId, @Param("flgtFlno") String flgtFlno, @Param("content") Integer content, @Param("status") Integer status);

    //根据航班ID查询单条任务信息
    MyTask getTaskInfoById(@Param("flgtId") String flgtId);

    //根据航班字段进行检索
    List<MyFlight> getFlightListCondition(@Param("flight") MyFlightCondition flight);

    //根据航班号和航班时间查询是否存在此航班
    MyFlight getFlightExist(@Param("flgtFlno") String flgtFlno, @Param("forMatTime") String forMatTime, @Param("flgtRegn") String flgtRegn);

    //更新航班信息
    int updateFlight(@Param("newFlight") MyFlight newFlight);

    //更新任务信息
    int updateTask(@Param("newTask") MyTask newTask);

    //再根据任务ID更新任务表里的加油员ID和任务状态
    //更新任务信息
    int updateTaskState(@Param("taskId") String taskId, @Param("taskOpeStaffId") String taskOpeStaffId, @Param("taskStatus") Integer taskStatus, @Param("forMatTime") String forMatTime);

    //根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出,按照本场和前方起飞分开的
    List<MyFlightTask> getFlightDateList(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出,按照本场查询,按照预计起飞时间排序
    List<MyFlightTask> getFlightTaskDateThis(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    // 根据当前系统时间获取当日的航班列表,按照本场查询,按照预计起飞时间排序 PAD端
    List<MyFlightTask> getCurrentFlight(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode, @Param("staffId") String staffId);

    //根据航班号，航班日期，进离港检索航班表（DB）
    MyFlight getFlightInfo(@Param("flight") MyFlight flight, @Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //根据航班号，航班日期，进离港检索航班表（DB）-老数据存在重复航班，所以返回list
    List<MyFlight> getFlightInfos(@Param("flight") MyFlight flight, @Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //更新航班的任务下发标识
    int updateFlightInfoOne(@Param("taskFlightId") String taskFlightId, @Param("i") int i, @Param("customName") String customName);

    //根据航班号查询预创建航班表
    List<MyFlightTemp> getFlightTemp(@Param("flgtFlno") String flgtFlno, @Param("staffAirportCode") String staffAirportCode);

    //根据机场码获取机场名（远程调用）
    String getAirportNames(@Param("apcdIcaoCode") String apcdIcaoCode);

    TFlight getFlightListThree(@Param("flgtLinkFlno") String flgtLinkFlno, @Param("flgtLinkFlop") String flgtLinkFlop, @Param("flgtLinkRepeat") Integer flgtLinkRepeat);

    List<MyFlightTask> selectFlightDateList(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode, @Param("date") String date);

    List<MyFlightTask> selectFlightDateListLock(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode, @Param("date") String date);

    //查询飞机号码表
    List<MyFlightCode> getFLIGHTCODE();

    MyFlightCode getFlightCodeByflgtRegn(@Param("flgtRegn") String flgtRegn);

    MyFlightCode getFlightCodeByflgtRegnAndFlno(@Param("flgtRegn") String flgtRegn, @Param("flno") String flno);

    //查询航空加油客户表
    List<MyCustom> getCUSTOM();

    //删除飞机号码表信息
    int deleteFLIGHTCODE(@Param("arcrRegn") String arcrRegn, @Param("startDate") String startDate);

    //删除航空加油客户表信息
    int deleteCUSTOM(@Param("cstmNum") String cstmNum);

    //新增飞机号码表信息
    int addFLIGHTCODE(@Param("flightCode") MyFlightCode flightCode);

    //新增航空加油客户表信息
    int addCUSTOM(@Param("custom") MyCustom custom);

    //修改飞机号码表信息
    int updateFLIGHTCODE(@Param("flightCode") MyFlightCode flightCode, @Param("startDate") String startDate);

    //修改航空加油客户表信息
    int updateCUSTOM(@Param("custom") MyCustom custom);

    MyFlightCode getFLIGHTCODEFind(@Param("arcrRegn") String arcrRegn, @Param("startDate") String startDate, @Param("flno") String flno);

    MyCustom getCUSTOMFind(@Param("cstmNum") String cstmNum);

    //获取规划数据
    TProjectData getProjectData(@Param("flgtFfid") String flgtFfid, @Param("flgtAirportCode") String flgtAirportCode, @Param("flgtAptareaCode") String flgtAptareaCode);

    //根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出,按照本场查询,按照预计起飞时间排序
    List<MyFlightTask> selectFlightOnlyIn(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode, @Param("date") String date);

    // 更具出港航班ID获取关联的进港航班ID
    String selectInFlightID(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode, @Param("flgtId") String flgtId);

    // 更具进港航班ID获取关联的出港航班ID
    String selectInFlightIds(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode, @Param("flgtId") String flgtId);

    //更新进港航班信息
    int updateInFlightInfo(@Param("flight") MyFlight flight);

    //新建航班闹钟提示
    int addFlightAlarm(@Param("flightAlarm") MyFlightAlarm flightAlarm);

    //删除航班闹钟提示
    int deleteFlightAlarm(@Param("flalId") String flalId, @Param("staffId") String staffId);

    //查询航班闹钟提示
    List<MyFlightAlarm> getFlightAlarm(@Param("staffId") String staffId, @Param("flalId") String flalId);

    //修改航班闹钟提示
    int updateFlightAlarm(@Param("flightAlarm") MyFlightAlarm flightAlarm);

    //机场三字码查询每个机场简称
    String getAirportNamess(@Param("airport3c") String airport3c);

    //根据飞机号码查询飞机类型
    String getFlgtAcname(@Param("flgtRegn") String flgtRegn);

    //查询当天最大自身连接数
    Integer findMaxFlgtRepeat();

    //更新航班的序号
    Integer getMaxflgtNum(@Param("date") String date, @Param("airportCode") String airportCode, @Param("airportAreaCode") String airportAreaCode);

    // 根据 queryString 查询 飞机号号 进港航班离港航班
    List<MyFlightTask> getPageTaskAndFlight(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode, @Param("queryString") String queryString, @Param("date") String nowStr);

    // 根据 queryString 查询 飞机号号 进港航班离港航班
    Integer getPageTaskAndFlightCount(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode, @Param("queryString") String queryString, @Param("date") String nowStr);

    MyFlight findMyFlightByFfid(@Param("ffid") String ffid);

    MyFlight findMyFlightByFlgtId(@Param("flgtId") String flgtId);

    MyFlight findLinkFlight(String flgtFlno, Date flgtLinkFlop, Integer flgtLinkRepeat, Integer flgtRepeat);

    // 根据 queryString 查询 飞机号号 进港航班离港航班
    List<MyFlightTask> getFlightListByNum(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode, @Param("queryString") String queryString, @Param("date") String nowStr, @Param("size") Integer pageSize);

    List<MyFlightTask> getTestFlight();

    //更新航班信息 预计加油量 ， 轮挡油量 起飞油量等
    int updateFlightFuel(@Param("newFlight") MyFlight newFlight);

    //根据航班号,机场所属代码，航班日期，进离港检索航班表（DB）
    MyFlight getFlightInfoDetail(@Param("flgtRegn") String flgtRegn, @Param("flgtFlno") String flgtFlno, @Param("flgtAirportCode") String flgtAirportCode, @Param("flgtFlop") String flgtFlop, @Param("flgtAdid") String flgtAdid);

    //根据飞机号查询客户编号信息
    MyFlightCode getCustomInfoByRegn(@Param("regn") String regn);

    MyCustom getFlightCodeNew(@Param("custom") String custom);
}
