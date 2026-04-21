package com.zh.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.zh.annotation.OperationLogs;
import com.zh.annotation.TaskLogs;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyFlightCodeCust;
import com.zh.bean.flight.MyFlightTask;
import com.zh.bean.flight.MyTask;
import com.zh.bean.flight.MyTaskOil;
import com.zh.bean.login.MyStaff;
import com.zh.bean.login.MyStaffVehi;
import com.zh.bean.login.TStaff;
import com.zh.constant.Constant;
import com.zh.exception.CustomException;
import com.zh.service.FlgtTypeService;
import com.zh.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 任务表
 */
@RestController
@RequestMapping(value = "/taskController")
@OperationLogs("任务管理")
@Api(value = "任务管理", tags = {"任务管理"})
public class TaskController extends BaseController {

    @Autowired
    private TaskService taskService;
    @Value("${task_and_flight_lock}")
    private Boolean lock;
    @Autowired
    private FlgtTypeService flgtTypeService;


    /**
     * 根据人员ID更改任务中的车辆编号（远程调用）
     */
    @PostMapping(value = "/updateTaskVehiNo")
    public void updateTaskVehiNo(@RequestBody MyStaffVehi staffVehi) {
        taskService.updateTaskVehiNo(staffVehi);
    }

    /**
     * 获取任务保障量（总量，已加，未到，待加，加油中）
     */
    @PostMapping(value = "/getTaskAmount")
    public ReturnMsg<Map<String, Object>> getTaskAmount(@RequestBody TStaff staff) {
        //使用map集合接受查出来的各个任务数量
//		Map<String,Object> taskAmountMap = taskService.getTaskAmount(staff);
        Map<String, Object> taskAmountMap = taskService.getTaskAmountByStatus(staff);
        ReturnMsg<Map<String, Object>> msg = new ReturnMsg<Map<String, Object>>(Constant.CODE_OK, null, taskAmountMap);
        return msg;
    }

    /**
     * PC端根据当前系统时间获取getFlightInfoById当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出
     */
    @PostMapping(value = "/getTaskAndFlight")
    public ReturnMsg<List<MyFlightTask>> getTaskAndFlight(@RequestBody MyStaff staff) {
        List<MyFlightTask> flightTaskList = taskService.getTaskAndFlight(new MyFlightTask(), staff);
        ReturnMsg<List<MyFlightTask>> msg = new ReturnMsg<List<MyFlightTask>>(Constant.CODE_OK, null, flightTaskList);
        return msg;
    }

    /**
     * pad端刷新航班详情
     */
    @PostMapping(value = "/flushTaskAndFlight")
    public ReturnMsg<MyFlightTask> flushTaskAndFlight(@RequestBody MyFlightTask myFlightTask, @RequestBody MyStaff staff) {
        List<MyFlightTask> flightTaskList = taskService.getTaskAndFlight(myFlightTask, staff);
        MyFlightTask flightTask = flightTaskList.stream().filter(dto -> ObjectUtil.equal(dto.getFlgtId(), myFlightTask.getFlgtId()))
                .findFirst().orElse(new MyFlightTask());
        ReturnMsg<MyFlightTask> msg = new ReturnMsg<MyFlightTask>(Constant.CODE_OK, null, flightTask);
        return msg;
    }

    /**
     * 任务下发,根据前台传的值更新任务信息
     *
     * @return
     */
    @OperationLogs("PC任务下发")
    @TaskLogs("调度台任务下发")
    @PutMapping(value = "/updateTaskInfo")
    public ReturnMsg<Object> updateTaskInfo(@RequestBody MyFlightTask task, @RequestBody MyStaff staff) {
        System.out.println("PC任务下发--updateTaskInfo---" + JSON.toJSONString(task) + "---" + JSON.toJSONString(staff));
        //判断如果加油员没有使用手持pad接收完成任务则自动把之前的任务都改成已完成
        //if(lock)
        //taskService.updateTaskInfoOver(task,staff);
        extracted(task.getTaskOpeStaffId(), task.getFlgtAcname());
        taskService.updateTaskInfo(task, staff, true);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 任务下发,根据前台传的值更新任务信息--pad
     *
     * @return
     */
    @OperationLogs("pad任务下发")
    @TaskLogs("pad任务下发")
    @PutMapping(value = "/updateTaskInfoPad")
    public ReturnMsg<Object> updateTaskInfoPad(@RequestBody MyFlightTask task, @RequestBody MyStaff staff) {
        System.out.println("PAD任务下发--updateTaskInfoPad---" + JSON.toJSONString(task) + "---" + JSON.toJSONString(staff));
        // pad申请任务时不可保障机型验证
        extracted(staff.getLoginUserIn().getStaffId(), task.getFlgtAcname());
        MyFlightTask integer = taskService.updateTaskInfo(task, staff, false);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, integer);
        return msg;
    }

    /**
     * 是否为非保障机型
     *
     * @param staffId
     * @param flgtAcname
     */
    private void extracted(String staffId, String flgtAcname) {
        Integer noFlgtNoByStaffId = flgtTypeService.getNoFlgtNoByStaffId(staffId, flgtAcname);
        // 返回值：0可以下发，1人员原因不能下发2是车型原因不能下发
        if (noFlgtNoByStaffId == 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz("该人员不支持该机型", null));
        } else if (noFlgtNoByStaffId == 2) {
            throw new CustomException(ReturnMsg.getInstanceNGz("该车辆不支持该机型", null));
        }
    }

    /**
     * 任务取消,清空任务表的加油员ID和创建人员ID并且状态改为0
     */
    @OperationLogs("取消任务")
    @TaskLogs("任务取消")
    @PutMapping(value = "/updateTaskCancel")
    public ReturnMsg<Object> updateTaskCancel(@RequestBody MyTask task, @RequestBody MyStaff staff) {
        System.out.println("任务取消开始--updateTaskCancel---" + JSON.toJSONString(task));
        Map<String, Object> updateTaskCancel = taskService.updateTaskCancel(task);
        System.out.println("任务取消结束--updateTaskCancel---" + JSON.toJSONString(updateTaskCancel));
        ReturnMsg<Object> msg = null;
        if ((Boolean) updateTaskCancel.get("key")) {
            msg = new ReturnMsg<Object>(Constant.CODE_OK, null, updateTaskCancel);
        } else {
            msg = new ReturnMsg<Object>(Constant.CODE_ERR, String.valueOf(updateTaskCancel.get("val")), updateTaskCancel);
        }
        return msg;
    }

    /**
     * 获取已下发的任务列表
     */
    @PostMapping(value = "/getTaskAlready")
    public ReturnMsg<List<MyTask>> getTaskAlready(@RequestBody MyStaff staff) {
        List<MyTask> taskList = taskService.getTaskAlready(staff);
        ReturnMsg<List<MyTask>> msg = new ReturnMsg<List<MyTask>>(Constant.CODE_OK, null, taskList);
        return msg;
    }

    /**
     * 查询历史任务 获取已完成的任务列表 (2019-05-14 11:20) 状态 > 6 的所有数据
     */
    @PostMapping(value = "/getOldTaskAlready")
    public ReturnMsg<List<MyTask>> getOldTaskAlready(@RequestBody MyStaff staff) {
        List<MyTask> taskList = taskService.getOldTaskAlready(staff);
        ReturnMsg<List<MyTask>> msg = new ReturnMsg<List<MyTask>>(Constant.CODE_OK, null, taskList);
        return msg;
    }

    /**
     * 更改任务的状态,任务接受1->3
     */
    @TaskLogs("任务接受")
    @PutMapping(value = "/updateTaskState")
    public ReturnMsg<String> updateTaskState(@RequestBody MyTask task, @RequestBody MyStaff staff) {
        System.out.println("任务接受开始--updateTaskState---" + JSON.toJSONString(task) + "--------" + JSON.toJSONString(staff));
        String updateTaskState = taskService.updateTaskState(task, staff);
        System.out.println("任务接受开始--updateTaskState---" + JSON.toJSONString(task) + "--------" + JSON.toJSONString(staff));
        if (updateTaskState == null) {
            ReturnMsg<String> msg = new ReturnMsg<String>(Constant.CODE_ERR, "更新失败", updateTaskState);
            return msg;
        } else {
            ReturnMsg<String> msg = new ReturnMsg<String>(Constant.CODE_OK, null, updateTaskState);
            return msg;
        }
    }

    /**
     * 更改任务的状态,加油到位3->4
     */
    @OperationLogs("到位确认")
    @TaskLogs("到位确认")
    @PutMapping(value = "/taskStatusInPlace")
    public ReturnMsg<String> taskStatusInPlace(@RequestBody MyTask task, @RequestBody MyStaff staff) {
        System.out.println("加油到位--taskStatusInPlace---" + JSON.toJSONString(task) + "--------" + JSON.toJSONString(staff));
        taskService.taskStatusInPlace(task, staff);
        System.out.println("加油到位--taskStatusInPlace---" + JSON.toJSONString(task) + "--------" + JSON.toJSONString(staff));
        ReturnMsg<String> msg = new ReturnMsg<String>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 根据任务ID查询任务单条记录和航班
     */
    @PostMapping(value = "/getTaskAndFlightById")
    @ApiOperation(value = "根据任务ID查询任务单条记录和航班")
    public ReturnMsg<MyFlightTask> getTaskAndFlightById(@RequestBody MyTask task) {
        MyFlightTask flightTaskList = taskService.getTaskAndFlightById(task);
        ReturnMsg<MyFlightTask> msg = new ReturnMsg<MyFlightTask>(Constant.CODE_OK, null, flightTaskList);
        return msg;
    }

    /**
     * 任务标星
     */
    @OperationLogs("任务星标")
    @PutMapping(value = "/updateTaskStarmark")
    public ReturnMsg<List<Object>> updateTaskStarmark(@RequestBody MyTask task) {
        taskService.updateTaskStarmark(task);
        ReturnMsg<List<Object>> msg = new ReturnMsg<List<Object>>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 根据系统当前日期获取7天的任务列表
     */
    @PostMapping(value = "/getTasktSevenDateList/{taskDate}")
    public ReturnMsg<List<MyTask>> getTasktSevenDateList(
            @PathVariable(value = "taskDate") String taskDate,
            @RequestBody MyStaff staff) {
        List<MyTask> taskList = taskService.getTasktSevenDateList(staff, taskDate);
        ReturnMsg<List<MyTask>> msg = new ReturnMsg<List<MyTask>>(Constant.CODE_OK, null, taskList);
        return msg;
    }

    /**
     * 根据系统当前日期获取7天的任务列表
     */
    @PostMapping(value = "/exportTasktSevenDateList")
    public ReturnMsg<List<MyTask>> exportTasktSevenDateList(@RequestBody MyStaff staff) {
        List<MyTask> taskList = taskService.exportTasktSevenDateList(staff);
        String[] columnNames = {"序号", "进港航班", "离港航班", "航线", "机号", "机型", "机位",
                "计飞", "到达", "结束", "班组", "油单", "备注"};
        String[] columns = {"flgtNum", "flgtLinkFlno", "flgtFlno", "flightValic", "flgtRegn", "flgtAcname",
                "flgtPlacecode", "flgtDStot", null, null, null, null, "flgtbezu"
        };
        String sheetName = "航空信息表";
        String fileName = "航空信息表" + System.currentTimeMillis();
        //ExcelUtils.exportExcel(response, list, columnNames, columns, sheetName,fileName);


        ReturnMsg<List<MyTask>> msg = new ReturnMsg<List<MyTask>>(Constant.CODE_OK, null, taskList);
        return msg;
    }

    /**
     * 根据任务ID获取任务信息，如果是任务完成的任务则获取油单的详细内容
     */
    @PostMapping(value = "/getTasktAndOil")
    public ReturnMsg<Object> getTasktAndOil(@RequestBody MyTask task) {
        Object taskInfo = taskService.getTasktAndOil(task);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, taskInfo);
        return msg;
    }

    /**
     * 任务信息修改
     */
    @PutMapping(value = "/updateTasktInfo")
    public ReturnMsg<Object> updateTasktInfo(@RequestBody MyTask task) {
        taskService.updateTasktInfo(task);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 根据任务的检索条件进行检索任务表
     */
    @PostMapping(value = "/getTasktInfoCondition")
    public ReturnMsg<List<MyTask>> getTasktInfoCondition(@RequestBody MyTask task, @RequestBody MyStaff staff) {
        List<MyTask> taskList = taskService.getTasktInfoCondition(task, staff);
        ReturnMsg<List<MyTask>> msg = new ReturnMsg<List<MyTask>>(Constant.CODE_OK, null, taskList);
        return msg;
    }

    /**
     * 加油员任务检索
     */
    @PostMapping(value = "/getStaffTasktInfoCondition")
    public ReturnMsg<List<MyTask>> getStaffTasktInfoCondition(@RequestBody MyTask task, @RequestBody MyStaff staff) {
        List<MyTask> taskList = taskService.getStaffTasktInfoCondition(task, staff);
        ReturnMsg<List<MyTask>> msg = new ReturnMsg<List<MyTask>>(Constant.CODE_OK, null, taskList);
        return msg;
    }

    /**
     * 获取加油客户编码
     */
    @GetMapping(value = "/getCustomerCode")
    public ReturnMsg<MyFlightCodeCust> getCustomerCode(HttpServletRequest request, @RequestParam String flgtRegn, @RequestParam String flgtFlno) {
        String staffAirportCode = request.getParameter("staffAirportCode");
        MyFlightCodeCust myFlightCode = taskService.getCustomerCode(flgtRegn, flgtFlno, staffAirportCode);
        ReturnMsg<MyFlightCodeCust> msg = new ReturnMsg<MyFlightCodeCust>(Constant.CODE_OK, null, myFlightCode);
        return msg;
    }


    /**
     * 加油员任务检索
     */
    @GetMapping(value = "/getStaffTasktInfoByStatus")
    public ReturnMsg<List<MyTask>> getStaffTasktInfoByStatus(@RequestBody MyStaff staff) {
        List<MyTask> taskList = taskService.getStaffTasktInfoByStatus(staff);
        ReturnMsg<List<MyTask>> msg = new ReturnMsg<List<MyTask>>(Constant.CODE_OK, null, taskList);
        return msg;
    }

    /**
     * 接收实时开始加油时间
     */
    @PutMapping(value = "/updateTaskChagStaTime")
    public ReturnMsg<Object> saveTaskChagStaTime(@RequestBody MyTask task) {
        System.out.println("PAD接收实时开始加油时间--saveTaskChagStaTime---" + JSON.toJSONString(task));
        String returnMsg = taskService.saveTaskChagStaTime(task);
        ReturnMsg<Object> msg = null;
        if ("success".equals(returnMsg)) {
            msg = new ReturnMsg<Object>(Constant.CODE_OK, null, returnMsg);
        } else {
            msg = new ReturnMsg<Object>(Constant.CODE_ERR, null, returnMsg);
        }
        return msg;
    }

    /**
     * 任务结束把任务状态改为7同时把任务中的任务完成时间更新
     */
    @OperationLogs("PC任务完成")
    @TaskLogs("PC任务结束")
    @PutMapping(value = "/updateTaskEnd")
    public ReturnMsg<Object> updateTaskEnd(@RequestBody MyTask task, @RequestBody MyStaff staff) {
        System.out.println("PC任务完成时传值--updateTaskEnd---" + JSON.toJSONString(task) + "staff--->" + JSON.toJSONString(staff));
        taskService.updateTaskEnd(task, "PC");
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 任务结束把任务状态改为7同时把任务中的任务完成时间更新Pad
     */
    @OperationLogs("pad任务完成")
    @TaskLogs("PAD任务结束")
    @PutMapping(value = "/updateTaskEndPad")
    public ReturnMsg<Object> updateTaskEndPad(@RequestBody MyTask task, @RequestBody MyStaff staff) {
        System.out.println("PAD任务完成时传值--updateTaskEndPad---" + JSON.toJSONString(task) + "staff--->" + JSON.toJSONString(staff));
        //MyTask myTask=taskService.getTaskById(task);
        taskService.updateTaskEnd(task, "PAD");
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;

    }

    /**
     * 根据机场代码查询所有当天已完成的任务信息（远程调用）
     */
    @PostMapping(value = "/getTaskEndInfo")
    public List<MyTask> getTaskEndInfo(@RequestBody MyStaff staff) {
        List<MyTask> taskList = taskService.getTaskEndInfo(staff);
        return taskList;
    }

    /**
     * 根据航班ID查询航班任务记录
     */
    @PostMapping(value = "/getTaskAndFlightByFlightId")
    public ReturnMsg<List<MyFlightTask>> ByFlightId(@RequestBody MyTask task) {
        List<MyFlightTask> flightTaskList = taskService.getTaskAndFlightByFlightId(task);
        ReturnMsg<List<MyFlightTask>> msg = new ReturnMsg<List<MyFlightTask>>(Constant.CODE_OK, null, flightTaskList);
        return msg;
    }

    /**
     * 根据任务ID获取任务信息，如果是任务完成的任务则获取油单的详细内容
     */
    @PostMapping(value = "/getTaskOilInfo")
    public ReturnMsg<MyTaskOil> getTaskOilInfo(@RequestBody MyTask task) {
        MyTaskOil taskInfo = taskService.getTaskOilInfo(task);
        ReturnMsg<MyTaskOil> msg = new ReturnMsg<MyTaskOil>(Constant.CODE_OK, null, taskInfo);
        return msg;
    }


    //hang 挂起
    @PostMapping(value = "/updateHangFlight")
    public ReturnMsg<Object> updateHangFlight(@RequestBody MyFlightTask task, @RequestBody MyStaff staff) {
        return taskService.updateHangFlight(task, staff);
    }

    //restore 恢复
    @PostMapping(value = "/restore")
    public ReturnMsg<Object> updateRestoreFlight(@RequestBody MyFlightTask task, @RequestBody MyStaff staff) {
        return taskService.updateRestoreFlight(task, staff);
    }


}
