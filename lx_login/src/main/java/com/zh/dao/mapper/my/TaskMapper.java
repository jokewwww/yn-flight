package com.zh.dao.mapper.my;

import com.zh.bean.flight.MyFlightTask;
import com.zh.bean.flight.MyTask;
import com.zh.bean.flight.MyTaskOil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface TaskMapper {

    //以（所属机场，所属区域，记录创建时间＝系统日期）的条件，统计任务表（DB）中的所有记录数，作为画面中的保障量_总量
    Integer getTaskSum(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode, @Param("date") String date);

    //以（所属机场，所属区域，记录创建时间＝系统日期，加油完成时间≠空）的条件，统计任务表（DB）中的所有记录数，作为画面中的保障量_已加
    Integer getTaskYetSum(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //以（所属机场，所属区域，记录创建时间＝系统日期，任务派发时间＝空）的条件，统计任务表（DB）中的所有记录数，作为画面中的保障量_未到
    Integer getTaskNotSum(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //以（所属机场，所属区域，记录创建时间＝系统日期，任务派发时间≠空，任务接受时间＝空）的条件，统计任务表（DB）中的所有记录数，作为画面中的保障量_待加
    Integer getTaskAwaitSum(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //以（所属机场，所属区域，记录创建时间＝系统日期，任务接受时间≠空，加油完成时间＝空）的条件，统计任务表（DB）中的所有记录数，作为画面中的保障量_加油
    Integer getTaskRefuelSum(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //PC端根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出
    List<MyFlightTask> getTaskAndFlight(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode, @Param("date") String date);


    //PC端根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出
    List<MyFlightTask> getTaskAndFlightLock(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode, @Param("date") String date);


    //更新任务信息
    int updateTaskInfo(@Param("task") MyTask task);

    //根据任务ID查询任务单条信息
    MyTask getTaskInfo(@Param("taskId") String taskId);

    //任务取消,清空任务表的加油员ID和创建人员ID并且状态改为0
    int updateTaskCancel(@Param("taskId") String taskId, @Param("status") Integer status);

    //根据任务ID查询出加油员ID
    String getTaskOpeStaffId(@Param("taskId") String taskId);

    //获取已下发的任务列表
    List<MyTask> getTaskAlready(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //根据任务ID和任务状态更改任务的状态
    int updateTaskState(@Param("taskId") String taskId, @Param("taskStatus") Integer taskStatus, @Param("taskAccTime") String taskAccTime, @Param("vehiNo") String vehiNo);

    //根据任务ID查询任务单条记录和航班
    MyFlightTask getTaskAndFlightById(@Param("taskId") String taskId);

    //根据任务ID和任务标星更改任务的任务标星字段
    int updateTaskStarmark(@Param("taskId") String taskId, @Param("taskStarmark") Integer taskStarmark);

    //根据系统当前日期获取7天的任务列表
    List<MyTask> getTasktSevenDateList(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //通过油单编号获取油单的信息
    MyTaskOil getOilInfo(@Param("taskFuelRecptNo") String taskFuelRecptNo);

    //根据任务ID更改任务的信息
    int updateTasktInfo(@Param("task") MyTask task);

    //根据任务的检索条件进行检索任务表
    List<MyTask> getTasktInfoCondition(@Param("task") MyTask task, @Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //加油员任务检索
    List<MyTask> getStaffTasktInfoCondition(@Param("task") MyTask task, @Param("staffId") String staffId);

    //加油员任务检索By任务状态
    List<MyTask> getStaffTasktInfoByStatus(@Param("staffId") String staffId);

    //查询当前加油员的当天任务
    List<MyTask> selectTaskByStaffId(@Param("staffId") String staffId);

    //查询当前加油员的前三天任务
    List<MyTask> selectThreeDayTaskByStaffId(@Param("staffId") String staffId);

    //查询当前加油员的前七天任务
    List<MyTask> selectOneWeekTaskByStaffId(@Param("staffId") String staffId);

    int updateTaskStates(@Param("taskId") String taskId, @Param("taskStatus") Integer taskStatus, @Param("sfvhVehiNo") String sfvhVehiNo, @Param("taskChagStaTime") String taskChagStaTime);

    //根据机场代码查询所有当天已完成的任务信息（远程调用）
    List<MyTask> getTaskEndInfo(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode, @Param("date") String date);

    MyFlightTask getFlightById(@Param("flightId") String flightId);

    //根据航班ID查询航班任务记录
    List<MyFlightTask> getTaskAndFlightByFlightId(@Param("flightId") String flightId);

    //通过任务ID获取油单任务的信息
    MyTaskOil getTaskOilInfo(@Param("taskId") String taskId);

    //根据人员ID更改任务中的车辆编号（远程调用）
    int updateTaskVehiNo(@Param("sfvhStaffId") String sfvhStaffId, @Param("sfvhVehiNo") String sfvhVehiNo);

    MyFlightTask getTaskAndFlightByIds(@Param("taskId") String taskId);

    //任务总量 关联查询加油任务表和航显表,任务状态不是 2：申请待批（预留）  6：油单待审核（预留） 8：拒绝（预留）,9：取消加油 的数据
    Integer getTaskSumByStatus(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //任务完成 关联查询加油任务表和航显表,任务状态是 7：任务完成 的数据
    Integer getTaskYetSumByStatus(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //未到  关联查询加油任务表和航显表,任务状态是 0：未下发 的数据
    Integer getTaskNotSumByStatus(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //待加 关联查询加油任务表和航显表,任务状态是1：待接受 的数据
    Integer getTaskAwaitSumByStatus(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //加油中 关联查询加油任务表和航显表,任务状态是3：已接受，4：到位（预留），5：加油完成 的数据
    Integer getTaskRefuelSumByStatus(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    List<MyTask> getOldTaskAlready(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    // 根据航班好  所属机场代码 开始时间  结束时间 来查询任务  同可以用来判断 是否存在重复数据
    MyTask findOneTaskId(@Param("taskFlightNo") String taskFlightNo, @Param("taskAirportCode") String taskAirportCode, @Param("taskChagStaTime") Date taskChagStaTime, @Param("taskChagEndTime") Date taskChagEndTime);

    int getTaskDoing(MyFlightTask task);

    List<MyTask> getNotDoTaskByTaskOpeStaffId(MyFlightTask task);
}
