package com.zh.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zh.annotation.TaskLogs;
import com.zh.bean.ResponseObject;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.*;
import com.zh.bean.login.LoginUser;
import com.zh.bean.login.MyStaff;
import com.zh.bean.login.TStaff;
import com.zh.constant.Constant;
import com.zh.service.FlightService;
import com.zh.util.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 航班表
 */
@RestController
@RequestMapping(value = "/flightController")
public class FlightController extends BaseController {

    @Autowired
    private FlightService flightService;

    /**
     * 根据航班公司二字码获取航班公司名
     */
    @PostMapping(value = "/getFlightName")
    public ReturnMsg<Map<String, Object>> getFlightName(@RequestBody MyAirlinesCode myAirlinesCode) {
        Map<String, Object> flightName = flightService.getFlightName(myAirlinesCode);
        ReturnMsg<Map<String, Object>> msg = new ReturnMsg<Map<String, Object>>(Constant.CODE_OK, null, flightName);
        return msg;
    }

    /**
     * 根据机场三字码获取机场名
     */
    @PostMapping(value = "/getFlightNames")
    public ReturnMsg<Map<String, Object>> getFlightNames(@RequestBody MyAirportCode airportCode) {
        Map<String, Object> flightName = flightService.getFlightNames(airportCode);
        ReturnMsg<Map<String, Object>> msg = new ReturnMsg<Map<String, Object>>(Constant.CODE_OK, null, flightName);
        return msg;
    }

    /**
     * 根据机场码获取机场名（远程调用）
     */
    @PostMapping(value = "/getAirportName")
    public MyAirportCode getAirportName(@RequestBody MyAirportCode airportCode) {
        MyAirportCode airportCodeInfo = flightService.getAirportName(airportCode);
        return airportCodeInfo;
    }

    /**
     * 获取航班量（总量，已离，未到，停场）
     */
    @PostMapping(value = "/getFlightAmount")
    public ReturnMsg<Map<String, Object>> getFlightAmount(@RequestBody TStaff staff) {
        //使用map集合接受查出来的各个航班数量
        Map<String, Object> flightAmountMap = flightService.getFlightAmount(staff);
        ReturnMsg<Map<String, Object>> msg = new ReturnMsg<Map<String, Object>>(Constant.CODE_OK, null, flightAmountMap);
        return msg;
    }

    /**
     * 飞机信息接口
     * 取得当前在场航班信息，并将一进一出航班配对。
     */
    @PostMapping(value = "/getFlightList")
    public ReturnMsg<List<TFlightInfoList>> getFlightList(@RequestBody TFlight flight) {
        List<TFlightInfoList> flightList = flightService.getFlightList(flight);
        ReturnMsg<List<TFlightInfoList>> msg = new ReturnMsg<List<TFlightInfoList>>(Constant.CODE_OK, null, flightList);
        return msg;
    }

    /**
     * 手动创建航班的同时创建任务
     */
    @PostMapping(value = "/addFlightAndTask")
    public ReturnMsg<Object> addFlightAndTask(@RequestBody MyFlight flight, @RequestBody MyTask task,
                                              @RequestBody MyStaff staff) {
        // 手动创建航班的同时创建任务
        MyFlightTask myFlightTask = flightService.addFlightAndTask(flight, staff, task.getTaskContent(), task.getTaskStatus());
	/*	ReturnMsg<Object> msg = null;
		if(null == myFlightTask){
			msg = new ReturnMsg<Object>(Constant.CODE_OK, null, myFlightTask);
		}else{
			msg = new ReturnMsg<Object>(Constant.CODE_ERR, "同一天内不可重复新增同一航班", null);
		}*/
        flightService.cacheFlightAndTaskForPad(Optional.ofNullable(staff.getLoginUserIn()).map(LoginUser::getStaffAirportCode).orElse(StrUtil.EMPTY));
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, myFlightTask);
        return msg;
    }

    /**
     * 取得系统日期和机场代码和机场区域代码检索航班表（DB）的航班日期字段,取得前1天，当天，后1天的航班所有信息
     */
    @PostMapping(value = "/getFlightThreeDateList")
    public ReturnMsg<List<MyFlight>> getFlightThreeDateList(@RequestBody MyStaff staff) {
        List<MyFlight> flightList = flightService.getFlightThreeDateList(staff);
        ReturnMsg<List<MyFlight>> msg = new ReturnMsg<List<MyFlight>>(Constant.CODE_OK, null, flightList);
        return msg;
    }

    /**
     * 取得系统日期和机场代码和机场区域代码检索航班表（DB）的航班日期字段,取得前7天的航班所有信息
     */
    @PostMapping(value = "/getFlightSevenDateList")
    public ReturnMsg<List<MyFlight>> getFlightSevenDateList(@RequestBody MyStaff staff) {
        List<MyFlight> flightList = flightService.getFlightSevenDateList(staff);
        ReturnMsg<List<MyFlight>> msg = new ReturnMsg<List<MyFlight>>(Constant.CODE_OK, null, flightList);
        return msg;
    }

    /**
     * 修改航班信息
     */
    @PutMapping(value = "/updateFlightInfo")
    public ReturnMsg<Object> updateFlightInfo(@RequestBody MyFlight flight, @RequestBody MyTask task) {
        flightService.updateFlightInfo(flight, task);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 修改航显列表里的航班信息
     */
    @PutMapping(value = "/updateOnlyFlightInfo")
    public ReturnMsg<Object> updateOnlyFlightInfo(@RequestBody MyFlight flight, @RequestBody MyStaff staff) {
        flightService.updateOnlyFlightInfo(flight, staff);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 根据航班ID查询航班详细信息
     */
    @PostMapping(value = "/getFlightInfoById")
    public ReturnMsg<MyFlightTask> getFlightInfoById(@RequestBody MyFlight flight) {
        MyFlightTask flightById = flightService.getFlightInfoById(flight);
        ReturnMsg<MyFlightTask> msg = new ReturnMsg<MyFlightTask>(Constant.CODE_OK, null, flightById);
        return msg;
    }

    /**
     * 根据航班号查询航班详细信息
     */
    @PostMapping(value = "/getFlightInfoByFlightNo")
    public ReturnMsg<List<MyFlight>> getFlightInfoByFlightNo(@RequestBody MyFlight flight) {
        List<MyFlight> MyFlightList = flightService.getFlightInfoByFlightNo(flight);
        ReturnMsg<List<MyFlight>> msg = new ReturnMsg<List<MyFlight>>(Constant.CODE_OK, null, MyFlightList);
        return msg;
    }

    /**
     * 根据航班字段进行检索
     */
    @PostMapping(value = "/getFlightListCondition")
    public ReturnMsg<List<MyFlight>> getFlightListCondition(@RequestBody MyFlightCondition flight) {
        List<MyFlight> flightListCondition = flightService.getFlightListCondition(flight);
        ReturnMsg<List<MyFlight>> msg = new ReturnMsg<List<MyFlight>>(Constant.CODE_OK, null, flightListCondition);
        return msg;
    }

    /**
     * pad航班新建
     */
    @PostMapping(value = "/padAddFlightAndTask")
    public ReturnMsg<Object> padAddFlightAndTask(@RequestBody MyFlightTask flight, @RequestBody MyStaff staff) {
        flightService.padAddFlightAndTask(flight, staff);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * pad任务申领
     */
    @PutMapping(value = "/padUpdateTaskState")
    public ReturnMsg<Object> padUpdateTaskState(@RequestBody MyFlight flight, @RequestBody MyTask task) {
        //根据航班号，航班时间查出航班ID，再根据航班ID更新任务表里的加油员ID和任务状态
        Map<String, Object> padUpdateTaskState = flightService.padUpdateTaskState(flight, task);
        if (padUpdateTaskState.get("false") != null) {
            String object = (String) padUpdateTaskState.get("false");
            ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_ERR, object, null);
            return msg;
        } else {
            ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, padUpdateTaskState);
            return msg;
        }
    }

    /**
     * pad任务同步上传
     */
    @PutMapping(value = "/padSyncTask")
    public ReturnMsg<Object> padSyncTask(@RequestBody MyFlight flight, @RequestBody MyTask task) {
        //根据航班号，航班时间查出航班ID，再根据航班ID更新任务表里的加油员ID和任务状态
        Map<String, Object> padUpdateTaskState = flightService.padUpdateTaskState(flight, task);
        if (padUpdateTaskState.get("false") != null) {
            String object = (String) padUpdateTaskState.get("false");
            ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_ERR, object, null);
            return msg;
        } else {
            ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, padUpdateTaskState);
            return msg;
        }
    }

    /**
     * pad航班新建和油单
     */
    @PostMapping(value = "/padAddFlightAndTaskAndOil")
    public ReturnMsg<String> padAddFlightAndTaskAndOil(@RequestBody MyFlightTask flight, @RequestBody MyFuelRecpt fuelRecpt, @RequestBody MyStaff staff) {
        String padAddFlightAndTaskAndOil = flightService.padAddFlightAndTaskAndOil(flight, fuelRecpt, staff);
        ReturnMsg<String> msg = new ReturnMsg<String>(Constant.CODE_OK, null, padAddFlightAndTaskAndOil);
        return msg;
    }

    /**
     * PC端根据当前系统时间获取当日的任务列表并且同时根据任务列表中的航班ID查询航班列表一起输出,按照本场和前方起飞分开的
     */
    @PostMapping(value = "/getFlightDateList")
    public ReturnMsg<List<MyFlightTask>> getFlightDateList(@RequestBody MyStaff staff) {
        List<MyFlightTask> flightTaskList = flightService.getFlightDateList(staff);
        ReturnMsg<List<MyFlightTask>> msg = new ReturnMsg<List<MyFlightTask>>(Constant.CODE_OK, null, flightTaskList);
        return msg;
    }

    /**
     * 获取当天的本场航班的航班信息,按照预计起飞时间排序
     */
    @PostMapping(value = "/getFlightTaskDateThis")
    public ReturnMsg<List<MyFlightTask>> getFlightTaskDateThis(@RequestBody MyStaff staff) {
        List<MyFlightTask> flightTaskList = flightService.getFlightTaskDateThis(staff);
        ReturnMsg<List<MyFlightTask>> msg = new ReturnMsg<List<MyFlightTask>>(Constant.CODE_OK, null, flightTaskList);
        return msg;
    }

    /**
     * 根据航班号查询预创建航班表
     */
    @PostMapping(value = "/getFlightTemp")
    public ReturnMsg<List<MyFlightTemp>> getFlightTemp(@RequestBody MyStaff staff, @RequestBody MyFlightTemp myFlightTemp) {
        List<MyFlightTemp> flightTempList = flightService.getFlightTemp(staff, myFlightTemp);
        ReturnMsg<List<MyFlightTemp>> msg = new ReturnMsg<List<MyFlightTemp>>(Constant.CODE_OK, null, flightTempList);
        return msg;
    }

    /**
     * PAD 获取当天的航班的航班信息,按照预计起飞时间排序
     */
    @PostMapping(value = "/getCurrentFlight")
    public ReturnMsg<List<MyFlightTask>> getCurrentFlight(@RequestBody MyStaff staff) {
        List<MyFlightTask> flightList = flightService.getCurrentFlight(staff);
        ReturnMsg<List<MyFlightTask>> msg = new ReturnMsg<List<MyFlightTask>>(Constant.CODE_OK, null, flightList);
        return msg;
    }

    /**
     * Excele导出
     */
    @PostMapping(value = "/report")
    public void export(@RequestBody MyStaff staff, HttpServletRequest request, HttpServletResponse response) {
//	 public ReturnMsg<MyFlightTask> export(@RequestBody MyStaff staff,HttpServletRequest request,HttpServletResponse response) {
        List<MyFlightTask> list = flightService.selectFlightDateList(staff);
        String[] columnNames = {"序号", "进港航班", "离港航班", "航线", "机号", "机型", "机位",
                "计飞", "到达", "结束", "班组", "油单", "备注"};
        String[] columns = {"flgtNum", "flgtLinkFlno", "flgtFlno", "flightValic", "flgtRegn", "flgtAcname",
                "flgtPlacecode", "flgtDStot", null, null, null, null, "flgtbezu"
        };
        String sheetName = "航空信息表";
        String fileName = "航空信息表" + System.currentTimeMillis();
        ExcelUtils.exportExcel(response, list, columnNames, columns, sheetName, fileName);
//		return new ReturnMsg<MyFlightTask>(Constant.CODE_OK, null, null);
    }


    /**
     * 查询飞机号码表
     */
    @PostMapping(value = "/getFLIGHTCODE")
    public ReturnMsg<List<MyFlightCode>> getFLIGHTCODE() {
        List<MyFlightCode> flightList = flightService.getFLIGHTCODE();
        ReturnMsg<List<MyFlightCode>> msg = new ReturnMsg<List<MyFlightCode>>(Constant.CODE_OK, null, flightList);
        return msg;
    }

    /**
     * 查询飞机号码表详情
     */
    @PostMapping(value = "/getFLIGHTCODEFind")
    public ReturnMsg<MyFlightCode> getFLIGHTCODEFind(@RequestBody MyFlightCode flightCode) {
        MyFlightCode flightList = flightService.getFLIGHTCODEFind(flightCode);
        ReturnMsg<MyFlightCode> msg = new ReturnMsg<MyFlightCode>(Constant.CODE_OK, null, flightList);
        return msg;
    }

    /**
     * 查询航空加油客户表
     */
    @PostMapping(value = "/getCUSTOM")
    public ReturnMsg<List<MyCustom>> getCUSTOM() {
        List<MyCustom> flightList = flightService.getCUSTOM();
        ReturnMsg<List<MyCustom>> msg = new ReturnMsg<List<MyCustom>>(Constant.CODE_OK, null, flightList);
        return msg;
    }

    /**
     * 查询航空加油客户表详情
     */
    @PostMapping(value = "/getCUSTOMFind")
    public ReturnMsg<MyCustom> getCUSTOMFind(@RequestBody MyCustom custom) {
        MyCustom flightList = flightService.getCUSTOMFind(custom);
        ReturnMsg<MyCustom> msg = new ReturnMsg<MyCustom>(Constant.CODE_OK, null, flightList);
        return msg;
    }

    /**
     * 删除飞机号码表信息
     */
    @DeleteMapping(value = "/deleteFLIGHTCODE")
    public ReturnMsg<Object> deleteFLIGHTCODE(@RequestBody MyFlightCode flightCode) {
        flightService.deleteFLIGHTCODE(flightCode);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 删除航空加油客户表信息
     */
    @DeleteMapping(value = "/deleteCUSTOM")
    public ReturnMsg<Object> deleteCUSTOM(@RequestBody MyCustom custom) {
        flightService.deleteCUSTOM(custom);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 新增飞机号码表信息
     */
    @PostMapping(value = "/addFLIGHTCODE")
    public ReturnMsg<Object> addFLIGHTCODE(@RequestBody MyFlightCode flightCode) {
        Integer addFLIGHTCODE = flightService.addFLIGHTCODE(flightCode);
        if (addFLIGHTCODE == 1) {
            return new ReturnMsg<Object>(Constant.CODE_ERR, "已存在!", null);
        } else {
            return new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        }
    }

    /**
     * 新增航空加油客户表信息
     */
    @PostMapping(value = "/addCUSTOM")
    public ReturnMsg<Object> addCUSTOM(@RequestBody MyCustom custom) {
        Integer addCUSTOM = flightService.addCUSTOM(custom);
        if (addCUSTOM == 1) {
            return new ReturnMsg<Object>(Constant.CODE_ERR, "已存在!", null);
        } else {
            return new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        }
    }

    /**
     * 修改飞机号码表信息
     */
    @PutMapping(value = "/updateFLIGHTCODE")
    public ReturnMsg<Object> updateFLIGHTCODE(@RequestBody MyFlightCode flightCode) {
        flightService.updateFLIGHTCODE(flightCode);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 修改航空加油客户表信息
     */
    @PutMapping(value = "/updateCUSTOM")
    @TaskLogs("修改航空加油客户表信息")
    public ReturnMsg<Object> updateCUSTOM(@RequestBody MyCustom custom, @RequestBody MyStaff staff) {
        flightService.updateCUSTOM(custom);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 获取规划数据（地图）
     */
    @PostMapping(value = "/getProjectData")
    public ReturnMsg<TProjectData> getProjectData(@RequestBody MyFlight flight) {
        TProjectData projectData = flightService.getProjectData(flight);
        ReturnMsg<TProjectData> msg = new ReturnMsg<TProjectData>(Constant.CODE_OK, null, projectData);
        return msg;
    }

    /**
     * 新建或者修改航班闹钟提示
     */
    @PostMapping(value = "/addOrUpdateFlightAlarm")
    public ReturnMsg<MyFlightAlarm> addOrUpdateFlightAlarm(@RequestBody MyFlightAlarm flightAlarm, @RequestBody MyStaff staff) {
        MyFlightAlarm alarm = flightService.addOrUpdateFlightAlarm(flightAlarm, staff);
        return new ReturnMsg<>(Constant.CODE_OK, null, alarm);
    }

    /**
     * 删除航班闹钟提示
     */
    @DeleteMapping(value = "/deleteFlightAlarm")
    public ReturnMsg<MyFlightAlarm> deleteFlightAlarm(@RequestBody MyFlightAlarm flightAlarm, @RequestBody MyStaff staff) {
        flightService.deleteFlightAlarm(flightAlarm, staff);
        return new ReturnMsg<>(Constant.CODE_OK, null, null);
    }

    /**
     * 查询航班闹钟提示
     */
    @PostMapping(value = "/getFlightAlarm")
    public ReturnMsg<List<MyFlightAlarm>> getFlightAlarm(@RequestBody MyStaff staff, @RequestBody MyFlightAlarm flightAlarm) {
        List<MyFlightAlarm> flightAlarmList = flightService.getFlightAlarm(staff, flightAlarm);
        ReturnMsg<List<MyFlightAlarm>> msg = new ReturnMsg<List<MyFlightAlarm>>(Constant.CODE_OK, null, flightAlarmList);
        return msg;
    }

    /**
     * pad航班列表
     * 飞机信息接口
     * 取得当前在场航班信息，并将一进一出航班配对。
     */
    @PostMapping(value = "/getPageFlightList")
    public ResponseObject<Object> getPageFlightList(@RequestBody MyNewFlight flight, @RequestBody MyStaff staff) {
        return flightService.getFlightListByNum(flight);
    }

    /**
     * pad航班列表-无分页
     * 飞机信息接口
     * 取得当前在场航班信息，并将一进一出航班配对。
     */
    @PostMapping(value = "/getFlightTaskListNoPage")
    public ResponseObject<Object> getPageFlightListNoPage(@RequestBody MyNewFlight flight, @RequestBody MyStaff staff) {
        ResponseObject<Object> flightListByNum = flightService.getFlightAndTaskForPad(flight);
        return flightListByNum;
    }

    /**
     * 根据ffid 创建ffid去创建当天日期的航班
     * 1 如果根据ffid 查到的数据就是当天的 抛异常
     * 2 如果该条航班为关联航班 则创建的时候对应创建进出港航班
     *
     * @param flight
     * @param staff
     * @return
     */
    @PostMapping(value = "/copyNewFlight")
    public ReturnMsg<MyFlight> copyNewFlight(@RequestBody MyFlight flight, @RequestBody MyStaff staff) {
        return ReturnMsg.getInstanceOKz(flightService.copyNewFlight(flight));
    }

    @PostMapping(value = "/insertFlightRemark")
    public ReturnMsg<MyFlight> insertFlightRemark(@RequestBody MyFlight flight) {
        return ReturnMsg.getInstanceOKz(flightService.insertFlightRemark(flight));
    }

    /**
     * 查询机位号表数据
     */
    @PostMapping(value = "/getPlaceCode")
    public ReturnMsg<List<TPlacecode>> getPlaceCode(@RequestBody TPlacecode placeCode) {
        return ReturnMsg.getInstanceOKz(flightService.getPlaceCode(placeCode));
    }

    /**
     * 新建或者修改机位号数据
     */
    @PostMapping(value = "addOrUpdatePlaceCode")
    public ReturnMsg<Object> addOrUpdatePlaceCode(@RequestBody TPlacecode placeCode1) {

        Integer res = flightService.addOrUpdatePlaceCode(placeCode1);
        if (res == 1) {
            return new ReturnMsg<Object>(Constant.CODE_ERR, "保存成功!", placeCode1);
        } else {
            return new ReturnMsg<Object>(Constant.CODE_OK, "保存失败!", null);
        }
    }

    @PostMapping(value = "/getOnePlaceCode")
    public ReturnMsg<TPlacecode> getOnePlaceCode(@RequestBody TPlacecodeKey key) {
        return ReturnMsg.getInstanceOKz(flightService.getOnePlaceCode(key));
    }

    /**
     * 删除机位号
     */
    @PostMapping(value = "deletePlaceCode")
    public ReturnMsg<Object> deletePlaceCode(@RequestBody TPlacecodeKey key) {
        Integer res = flightService.deletePlaceCode(key);
        if (res == 1) {
            return new ReturnMsg<Object>(Constant.CODE_ERR, "删除成功!", null);
        } else {
            return new ReturnMsg<Object>(Constant.CODE_OK, "删除失败!", null);
        }
    }

    /**
     * 查询机位号类型数据
     */
    @PostMapping(value = "/getPlaceCodeType")
    public ReturnMsg<List<TPlacecodeType>> getPlaceCodeType(@RequestBody TPlacecodeType placeCodeType) {
        return ReturnMsg.getInstanceOKz(flightService.getPlaceCodeType(placeCodeType));
    }

    /**
     * 新建或者修改机位号类型数据
     */
    @PostMapping(value = "addOrUpdatePlaceCodeType")
    public ReturnMsg<Object> addOrUpdatePlaceCodeType(@RequestBody TPlacecodeType placeCode) {
        Integer res = flightService.addOrUpdatePlaceCodeType(placeCode);
        if (res == 1) {
            return new ReturnMsg<Object>(Constant.CODE_ERR, "保存成功!", null);
        } else {
            return new ReturnMsg<Object>(Constant.CODE_OK, "保存失败!", null);
        }
    }

    /**
     * 删除机位号类型
     */
    @PostMapping(value = "deletePlaceCodeType")
    public ReturnMsg<Object> deletePlaceCodeType(@RequestBody TPlacecodeType tPlacecodeType) {
        Integer res = flightService.deletePlaceCodeType(tPlacecodeType.getId());
        if (res == 1) {
            return new ReturnMsg<Object>(Constant.CODE_ERR, "删除成功!", null);
        } else {
            return new ReturnMsg<Object>(Constant.CODE_OK, "删除失败!", null);
        }
    }


/*
	@Scheduled(fixedRate = 120000)
	public void getAFlight(){
		flightService.getAFlight();
	}
*/


    @GetMapping(value = "test")
    public void test() {
        flightService.test();
    }


    @GetMapping("test1")
    public void test1() {
        flightService.test1();
    }


    /**
     * 订单转化航显
     */
    @PostMapping(value = "orderToFlight")
    public ReturnMsg<Object> orderToFlight(@RequestBody String orders, @RequestBody MyStaff staff) {
        System.out.println("订单转化航显");
        JSONObject jsonObject = JSONObject.parseObject(orders);
        String fuels = jsonObject.getString("orders");
        List<Map> list = JSON.parseArray(fuels, Map.class);
        List<TOrderInfo> tFuelNoList = list.stream()
                .map(o -> {
                            return JSON.parseObject(JSON.toJSONString(o), TOrderInfo.class);
                        }
                ).collect(Collectors.toList());
        List<TOrderInfo> collect0 = tFuelNoList.stream().filter(o -> o.getStatus() == 0).collect(Collectors.toList());
        List<TOrderInfo> collect1 = tFuelNoList.stream().filter(o -> o.getStatus() == 1).collect(Collectors.toList());
        List<MyFlightTask> myFlightTasks = flightService.orderToFlight(collect0, staff);
        if (collect1.size() > 0) {
            return new ReturnMsg<Object>(Constant.CODE_ERR, "转化失败!", collect1);
        }
        return new ReturnMsg<Object>(Constant.CODE_OK, "转化成功!", null);
    }


    @PostMapping(value = "/getFlightCodeNew")
    public ReturnMsg<MyCustom> getFlightCodeNew(@RequestBody MyFlightCode flightCode) {
        MyCustom flightList = flightService.getFlightCodeNew(flightCode);
        ReturnMsg<MyCustom> msg = new ReturnMsg<MyCustom>(Constant.CODE_OK, null, flightList);
        return msg;
    }

    @PostMapping(value = "/validCustomNum")
    public ReturnMsg validCustomNum(@RequestBody MyFlightTask task) {
        return flightService.validCustomNum(task);
    }
}
