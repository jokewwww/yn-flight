package com.zh.service;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.*;
import com.zh.bean.login.MyStaff;
import com.zh.bean.login.MyStaffVehi;
import com.zh.bean.login.TStaff;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TaskService {

    /**
     * 获取任务保障量（总量，已加，未到，待加，加油中）
     *
     * @param staff
     */
    Map<String, Object> getTaskAmount(TStaff staff);

    /**
     * 获取任务保障量（总量，已加，未到，待加，加油中）
     *
     * @param staff
     */
    Map<String, Object> getTaskAmountByStatus(TStaff staff);

    /**
     * PC端根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出
     *
     * @param staff
     */
    List<MyFlightTask> getTaskAndFlight(MyFlightTask myFlightTask, MyStaff staff);

    /**
     * 设置中文航线-中文航线中是字母的替换为中文
     *
     * @param flightTask
     */
    void setAirLines(MyFlightTask flightTask);

    /**
     * 功能描述：根据飞机号和航班号获取当前飞机的航班和任务信息
     *
     * @param airportCode 所属机场代码
     * @param aptareaCode 所属机场区域代码
     * @param date        日期
     * @param flgtRegn    飞机号
     * @param flgtFlno    航班号
     * @return com.zh.bean.flight.MyFlightTask
     * @author zhaojiacan
     * @date 2024/4/12
     */
    MyFlightTask getFlightByRegnAndFlno(@Param("airportCode") String airportCode, @Param("aptareaCode") String aptareaCode, @Param("date") String date, @Param("flgtRegn") String flgtRegn, @Param("flgtFlno") String flgtFlno);


    /**
     * 任务下发,根据前台传的值更新任务信息
     *
     * @param staff
     * @param b
     * @return
     */
    MyFlightTask updateTaskInfo(MyFlightTask task, MyStaff staff, boolean b);

    /**
     * 任务取消,清空任务表的加油员ID和创建人员ID并且状态改为0
     *
     * @return
     */
    Map<String, Object> updateTaskCancel(MyTask task);

    /**
     * 获取已下发的任务列表
     *
     * @param staff
     */
    List<MyTask> getTaskAlready(MyStaff staff);

    /**
     * 更改任务的状态,任务接受1->3
     *
     * @return
     */
    String updateTaskState(MyTask task, MyStaff staff);

    /**
     * 更改任务的状态,加油到位3->4
     *
     * @return
     */
    void taskStatusInPlace(MyTask task, MyStaff staff);

    /**
     * 根据任务ID查询任务单条记录和航班
     */
    MyFlightTask getTaskAndFlightById(MyTask task);

    /**
     * 任务标星
     */
    void updateTaskStarmark(MyTask task);

    /**
     * 根据系统当前日期获取7天的任务列表
     */
    List<MyTask> getTasktSevenDateList(MyStaff staff, String taskDate);

    /**
     * 根据任务ID获取任务信息，如果是任务完成的任务则获取油单的详细内容
     */
    Object getTasktAndOil(MyTask task);

    /**
     * 任务信息修改
     */
    void updateTasktInfo(MyTask task);

    /**
     * 根据任务的检索条件进行检索任务表
     *
     * @param staff
     */
    List<MyTask> getTasktInfoCondition(MyTask task, MyStaff staff);

    /**
     * 加油员任务检索
     */
    List<MyTask> getStaffTasktInfoCondition(MyTask task, MyStaff staff);

    /**
     * 加油员任务检索by任务状态
     */
    List<MyTask> getStaffTasktInfoByStatus(MyStaff staff);

    /**
     * 任务结束把任务状态改为7同时把任务中的任务完成时间更新
     */
    void updateTaskEnd(MyTask task, String useType);

    /**
     * 根据机场代码查询所有当天已完成的任务信息（远程调用）
     */
    List<MyTask> getTaskEndInfo(MyStaff staff);


    /**
     * 根据航班ID查询航班任务记录
     */
    List<MyFlightTask> getTaskAndFlightByFlightId(MyTask task);

    /**
     * 根据任务ID获取任务信息，如果是任务完成的任务则获取油单的详细内容
     */
    MyTaskOil getTaskOilInfo(MyTask task);

    /**
     * 根据人员ID更改任务中的车辆编号（远程调用）
     */
    void updateTaskVehiNo(MyStaffVehi staffVehi);

    List<MyTask> getOldTaskAlready(MyStaff staff);

    int updateTaskInfoOver(MyFlightTask task, MyStaff staff);

    /**
     * 根据任务ID查询任务
     */
    MyTask getTaskById(MyTask task);

    String saveTaskChagStaTime(MyTask task);

    Pair<String, String> pingCountries(String flgtRegn, String flgtFlno, Boolean lock);

    MyFlightCode getFlightCodeInfoByRegnAndFlno(String flgtRegn, String flgtFlno);

    List<MyTask> exportTasktSevenDateList(MyStaff staff);

    ReturnMsg<Object> updateHangFlight(MyFlightTask task, MyStaff staff);

    ReturnMsg<Object> updateRestoreFlight(MyFlightTask task, MyStaff staff);

    /**
     * 功能描述：获取加油客户编号
     *
     * @param flgtRegn            飞机号
     * @param flgtFlno            航班号
     * @param apcdCnafAirportCode 机场编码
     * @return com.zh.bean.flight.MyFlightCodeCust
     * @author zhaojiacan
     * @date 2024/4/17
     */
    MyFlightCodeCust getCustomerCode(String flgtRegn, String flgtFlno, String apcdCnafAirportCode);
}
