package com.zh.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zh.annotation.OperationLogs;
import com.zh.annotation.TaskLogs;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.*;
import com.zh.bean.login.MyStaff;
import com.zh.bean.login.MyStaffVehiTask;
import com.zh.bean.login.TStaff;
import com.zh.constant.Constant;
import com.zh.dao.mapper.my.MyFuelRecptMapper;
import com.zh.dao.mapper.my.StaffMapper;
import com.zh.prop.Prop;
import com.zh.service.FuelRecptService;
import com.zh.util.ExcelUtils;
import com.zh.util.ExceportTextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * duquanhong
 */
@RestController
@RequestMapping(value = "/recpt")
@OperationLogs("油单管理")
public class FuelRecptController extends BaseController {

    @Autowired
    Prop prop;
    @Autowired
    private FuelRecptService fuelRecptService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private MyFuelRecptMapper myFuelRecptMapper;

    /**
     * 根据加油员id查询任务信息（跨库使用）
     */
    @PostMapping(value = "/gettaskById")
    public MyTask getflightById(@RequestBody MyStaffVehiTask staff) {
        // 通过加油员id查询任务信息
        return fuelRecptService.gettaskById(staff.getSfvhStaffId());
    }

    /**
     * 根据加油员id查询任务信息（跨库使用）
     */
    @PostMapping(value = "/gettaskListById")
    public List<MyTask> gettaskListById(@RequestBody List<MyStaffVehiTask> list) {
        // 通过加油员id查询任务信息`
        return fuelRecptService.gettaskListById(list);
    }

    /**
     * @ 根据员工id 查询油单总数，国内 国际 离境 数量
     * @duquanhong
     */
    @PostMapping(value = "/total")
    public ReturnMsg<List<TfuelTotal>> recptcont(@RequestBody TStaff tStaff) {
        List<TfuelTotal> fuelrecpt = fuelRecptService.findRecptCont(tStaff);
        return new ReturnMsg<List<TfuelTotal>>(Constant.CODE_OK, null, fuelrecpt);

    }

    /**
     * 根据员工id查询 加油量多少升 多少吨
     */
    @PostMapping(value = "/fightOil")
    public ReturnMsg<List<TfuelFight>> findfightoil(@RequestBody TStaff tStaff) {
        List<TfuelFight> fuelrecpt = fuelRecptService.findfightoil(tStaff);
        return new ReturnMsg<List<TfuelFight>>(Constant.CODE_OK, null, fuelrecpt);
    }

    /**
     * 根据输入机位号 机场ID 获取机位信息
     */
    @PostMapping(value = "/flightlist")
    public ReturnMsg<List<TFlightPlace>> findFlight(@RequestBody TFlight tFlight) {
        List<TFlightPlace> findflight = fuelRecptService.findflight(tFlight);
        return new ReturnMsg<List<TFlightPlace>>(Constant.CODE_OK, null, findflight);

    }

    /**
     * 根据飞机号查询飞机详细信息
     */
    @PostMapping(value = "/FlightDetails")
    public ReturnMsg<TFlightInfo> flightdetails(@RequestBody TFlightInfo tFlightInfoList) {
        TFlightInfo flightdetail = fuelRecptService.finddetails(tFlightInfoList);
        return new ReturnMsg<TFlightInfo>(Constant.CODE_OK, null, flightdetail);

    }

    /**
     * 根据加油员ID获取任务列表(平板端)
     */
    @PostMapping(value = "/staffTask")
    public ReturnMsg<List<Map<String, Object>>> findStaffTask(@RequestBody MyStaff staff) {
        List<Map<String, Object>> findStaffTask = fuelRecptService.findStaffTask(staff);
        return new ReturnMsg<List<Map<String, Object>>>(Constant.CODE_OK, null, findStaffTask);

    }

    /**
     * 查询是否有进行的任务是2,3,4,5,6
     */
    @PostMapping(value = "/findDoingTask")
    public ReturnMsg<String> findDoingTask(@RequestBody MyStaff staff) {
        int countDoing = fuelRecptService.findDoingTask(staff.getLoginUserIn().getStaffId());
        if (countDoing > 0) {
            ReturnMsg<String> msg = new ReturnMsg<String>(Constant.CODE_ERR, "有正在进行的任务", null);
            return msg;
        } else {
            ReturnMsg<String> msg = new ReturnMsg<String>(Constant.CODE_OK, null, null);
            return msg;
        }
    }

    /**
     * 查询最近七天的油单列表
     */
    @PostMapping(value = "/findfuel")
    public ReturnMsg<List<MyFuelRecpt>> findfuelrecpt(@RequestBody MyStaff staff, @RequestBody MyFuelRecpt myFuelRecpt) {
        List<MyFuelRecpt> findfuel = fuelRecptService.findfuelrecpt(staff, myFuelRecpt);
        return new ReturnMsg<List<MyFuelRecpt>>(Constant.CODE_OK, null, findfuel);

    }

    /**
     * 查询当天的油单列表
     */
    @PostMapping(value = "/todayfuel")
    public ReturnMsg<List<MyFuelRecpt>> findtodayfuelrecpt(@RequestBody MyStaff staff) {
        List<MyFuelRecpt> findfuel = fuelRecptService.findtodayfuelrecpt(staff);
        return new ReturnMsg<List<MyFuelRecpt>>(Constant.CODE_OK, null, findfuel);

    }

    /**
     * 修改油单信息手动修改标记，人员，时间
     */
    @PutMapping(value = "/upfuel")
    public ReturnMsg<MyFuelRecpt> updatefuel(@RequestBody MyFuelRecpt myFuelRecpt, @RequestBody MyStaff staff) {
        fuelRecptService.updatefuel(myFuelRecpt, staff);
        return new ReturnMsg<MyFuelRecpt>(Constant.CODE_OK, null, null);

    }

    /**
     * 根据油单id进行回收
     */
    @PutMapping(value = "/recyclefuel")
    public ReturnMsg<MyFuelRecpt> uprecyclefuel(@RequestBody MyFuelRecpt myFuelRecpt) {
        fuelRecptService.uprecyclefuel(myFuelRecpt);
        return new ReturnMsg<MyFuelRecpt>(Constant.CODE_OK, null, null);
    }

    /**
     * 根据油单id进行审核油单
     */
    @PutMapping(value = "/auditfuel")
    public ReturnMsg<List<String>> auditfuel(@RequestBody MyFuelId flrcId) {
        return fuelRecptService.auditfuel(flrcId);
    }

    /**
     * 根据油单id进行标记油单
     */
    @PutMapping(value = "/mark")
    public ReturnMsg<Object> mark(@RequestBody MyFuelMark myFuelMark) {
        return fuelRecptService.mark(myFuelMark);
    }


    /**
     * 根据油单id进行撤销审核油单
     */
    @PutMapping(value = "/cancelFuel")
    public ReturnMsg<MyFuelRecpt> cancelFuel(@RequestBody MyFuelRecpt myFuelRecpt) {
        fuelRecptService.cancelFuel(myFuelRecpt.getFlrcId());
        return new ReturnMsg<MyFuelRecpt>(Constant.CODE_OK, null, null);
    }

    /**
     * 根据油单id进行回收油单
     */
    @PutMapping(value = "/recoveryFuel")
    public ReturnMsg<MyFuelRecpt> recoveryFuel(@RequestBody MyFuelRecpt myFuelRecpt) {
        fuelRecptService.recoveryFuel(myFuelRecpt.getFlrcId(), myFuelRecpt.getFlrcTakebackFlg());
        MyFuelRecpt fuelRecpt = fuelRecptService.findFuellist(myFuelRecpt);
        return new ReturnMsg<MyFuelRecpt>(Constant.CODE_OK, null, fuelRecpt);
    }

    /**
     * 根据油单id获取油单详情
     *
     * @return
     * @ myFuelRecpt
     */
    @PostMapping(value = "/findFuellist")
    public ReturnMsg<MyFuelRecpt> findFuellist(@RequestBody MyFuelRecpt myFuelRecpt) {
        MyFuelRecpt findfuel = fuelRecptService.findFuellist(myFuelRecpt);
        return new ReturnMsg<MyFuelRecpt>(Constant.CODE_OK, null, findfuel);
    }

    /**
     * 生成油单  5
     */
    @TaskLogs("加油完成生成油单")
    @OperationLogs("pad5 生成油单")
    @PostMapping(value = "/findFuelrecptlist")
    public ReturnMsg<Object> findFuelrecptlist(@RequestBody MyFuelRecpt myFuelRecpt, @RequestBody MyTask task,
                                               @RequestBody MyStaff staff) {
        fuelRecptService.findFuelrecptlist(myFuelRecpt, task, staff);
        return new ReturnMsg<Object>(Constant.CODE_OK, null, null);

    }

    /**
     * PAD上传油单
     */
    @TaskLogs("pad上传油单")
    @OperationLogs("pad油单上传")
    @PostMapping(value = "/insertFuelPad")
    public ReturnMsg<MyFuelRecpt> shangChuangYouDan(@RequestBody MyFuelRecpt fuelRecpt, @RequestBody MyStaff staff) {
        System.out.println("PAD上传油单--upFuelPad---" + JSON.toJSONString(fuelRecpt) + "--------" + JSON.toJSONString(staff));
        // 油单进行上传
        Integer integer = fuelRecptService.UpFuelRecpt(fuelRecpt, staff);
        if (integer == 1) {
            return new ReturnMsg<MyFuelRecpt>(Constant.CODE_OK, "该油单已经上传", null);
        }
        return new ReturnMsg<MyFuelRecpt>(Constant.CODE_OK, null, null);
    }

    @TaskLogs("测试一下")
    @PostMapping(value = "/tt")
    public ReturnMsg<MyFuelRecpt> test(@RequestBody MyFuelRecpt fuelRecpt, @RequestBody MyStaff staff) {


//        List<String> list = Lists.newArrayList();
//        list.add("!");
//        System.out.println(list.get(2));
        return new ReturnMsg<MyFuelRecpt>(Constant.CODE_OK, null, null);
    }

    /**
     * 新PC修改上传油单 -现在pad用
     */
    @OperationLogs("pad油单修改")
    @TaskLogs("pad修改油单")
    @PostMapping(value = "/updateFuelPad")
    public ReturnMsg<Object> updateFuelPad(
            @RequestBody MyFuelRecpt fuelRecpt,
            @RequestBody MyStaff staff) {
        String flrcId = fuelRecpt.getFlrcId();
        String flrcNo = fuelRecpt.getFlrcNo();
        if (StrUtil.isBlank(flrcId) && StrUtil.isBlank(flrcNo)) {
            return new ReturnMsg<Object>(Constant.CODE_ERR, "油单编号或者油单ID不能为空", null);
        }
        System.out.println("PAD修改油单--upFuelPad---" + JSON.toJSONString(fuelRecpt) + "--------" + JSON.toJSONString(staff));
        // 修改油单进行上传
        Map<String, Object> map = fuelRecptService.updateFuelPad(fuelRecpt, staff);
        return new ReturnMsg<Object>(Constant.CODE_OK, null, map);
    }

    /**
     * 新版本  PC上传油单
     */
    @PostMapping(value = "/pcInsertFuel")
    @OperationLogs("PC上传油单")
    @TaskLogs("智慧航油后台油单添加")
    public ReturnMsg<MyFuelRecpt> shangChuangYouDanPc(@RequestBody MyFuelRecpt fuelRecpt, @RequestBody MyStaff staff) {
        System.out.println("PCinsert油单" + fuelRecpt.getFlrcNo());

        MyFuelRecpt flno = new MyFuelRecpt();
        flno.setFlrcNo(fuelRecpt.getFlrcNo());
        List<MyFuelRecpt> findfuelretirevelist = fuelRecptService.findfuelretirevelist(flno);
        if (findfuelretirevelist.size() > 0) {
            System.out.println("油单编号重复请查询后在新增!" + fuelRecpt.getFlrcNo());
            return new ReturnMsg<MyFuelRecpt>(Constant.CODE_ERR, "油单编号重复请查询后在新增" + fuelRecpt.getFlrcNo(), null);
        }
        // 油单进行上传
        MyFuelRecpt myFuelRecpt = fuelRecptService.UpFuelRecptPc(fuelRecpt, staff);
        if (null != myFuelRecpt) {
            if (!Integer.valueOf(1).equals(myFuelRecpt.getFlrcStatus())) {
                return new ReturnMsg<MyFuelRecpt>(Constant.CODE_OK, null, myFuelRecpt);
            } else {
                return new ReturnMsg<MyFuelRecpt>(Constant.CODE_ERR, Optional.ofNullable(myFuelRecpt).map(MyFuelRecpt::getFlrcUpdateErrMsg).orElse("PC上传油单异常"), myFuelRecpt);
            }
        }
        return new ReturnMsg<MyFuelRecpt>(Constant.CODE_ERR, "匹配不到加油客户单位,请进行修改后重试", null);
    }


    /**
     * 新PC修改上传油单 -现在PC用
     */
    @OperationLogs("PC油单修改")
    @TaskLogs("智慧航油后台油单修改")
    @PostMapping(value = "/pcUpdateFuel")
    public ReturnMsg<Object> pcUpdateFuel(
            @RequestBody MyFuelRecpt fuelRecpt,
            @RequestBody MyStaff staff) {
        String flrcId = fuelRecpt.getFlrcId();
        String flrcNo = fuelRecpt.getFlrcNo();
        if (StrUtil.isBlank(flrcId) && StrUtil.isBlank(flrcNo)) {
            return new ReturnMsg<Object>(Constant.CODE_ERR, "油单编号或者油单ID不能为空", null);
        }
        // 修改油单进行上传
        System.out.println("PC修改油单油单编号" + fuelRecpt.getFlrcNo() + "PC修改油单油单人员" + staff.getLoginUserIn().getStaffId());
        Map<String, Object> map = fuelRecptService.pcUpdateFuel(fuelRecpt, staff);
        MyFuelRecpt myFuelRecpt = map == null || map.get("newfuel") == null ? null : (MyFuelRecpt) map.get("newfuel");
        if (myFuelRecpt == null ||
                (myFuelRecpt != null && !Integer.valueOf(1).equals(myFuelRecpt.getFlrcStatus()))
        ) {
            return new ReturnMsg<Object>(Constant.CODE_OK, null, map);
        } else {
            String errInfo = Optional.ofNullable(myFuelRecpt).map(MyFuelRecpt::getFlrcUpdateErrMsg).orElse("PC修改异常");
            return new ReturnMsg<Object>(Constant.CODE_ERR, errInfo, map);
        }
    }

    /**
     * 油单重发
     */
    @PostMapping(value = "/resend")
    public ReturnMsg<Object> resend(
            @RequestBody MyFuelRecpt fuelRecpt,
            @RequestBody MyStaff staff) {
        // 油单重发
        System.out.println("油单重发,油单号：" + fuelRecpt.getFlrcNo());
        String flrcId = fuelRecpt.getFlrcId();
        String flrcNo = fuelRecpt.getFlrcNo();
        if (StrUtil.isBlank(flrcId) && StrUtil.isBlank(flrcNo)) {
            return new ReturnMsg<Object>(Constant.CODE_ERR, "油单编号或者油单ID不能为空", null);
        }
        MyFuelRecpt resend = fuelRecptService.resend(fuelRecpt, staff);
        if (resend != null && !Integer.valueOf(1).equals(resend.getFlrcStatus())) {
            return new ReturnMsg<Object>(Constant.CODE_OK, null, "已重发");
        } else {
            return new ReturnMsg<Object>(Constant.CODE_ERR, null, Optional.ofNullable(resend).map(MyFuelRecpt::getFlrcUpdateErrMsg).orElse("重发异常"));
        }
    }

    /**
     * 油单检索
     */
    @PostMapping(value = "/fuelretireve")
    public ReturnMsg<List<MyFuelRecpt>> fuelretireve(@RequestBody MyFuelRecpt fuelRecpt) {
        List<MyFuelRecpt> findfuelretireve = fuelRecptService.fuelretirevelist(fuelRecpt);
        return new ReturnMsg<List<MyFuelRecpt>>(Constant.CODE_OK, null, findfuelretireve);
    }


    /**
     * 加油员保障任务
     */
    @PostMapping(value = "/bzstaff")
    public ReturnMsg<Object> findbzstaff(@RequestBody MyStaff staff) {
        Object flight = fuelRecptService.finsbzstaff(staff.getLoginUserIn().getStaffId());
        return new ReturnMsg<Object>(Constant.CODE_OK, null, flight);

    }

    /**
     * PC端强制更改任务为接受(离线)
     */
    @PutMapping(value = "/ForiceUpdateTaskStatus")
    public ReturnMsg<MyTask> ForiceUpdateTaskStatus(@RequestBody MyTask task) {
        fuelRecptService.ForiceUpdateTaskStatus(task);
        return new ReturnMsg<MyTask>(Constant.CODE_OK, null, null);
    }

    /**
     * 修改任务状态
     *
     * @param task
     * @return
     */
    @PutMapping(value = "/upstatus")
    public ReturnMsg<Object> upstatus(@RequestBody MyFlightTask task) {
        Map<String, Object> tsk = fuelRecptService.upstatus(task);
        return new ReturnMsg<Object>(Constant.CODE_OK, null, tsk);
    }

    /**
     * 导出文件文件
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/exportText")
    public void exportText(HttpServletResponse response) {
        //获取有效的数据
        List<MyFuelRecpt> list = fuelRecptService.findlistfuel();
        //将集合转换成字符串
//        String jsonString = JSON.toJSONString(list);
        StringBuilder sBuff = new StringBuilder();
        for (MyFuelRecpt dto : list) {
            //油单类型
            sBuff.append(dto.getFlrcType());
            sBuff.append("\t");
            //加油单号
            sBuff.append(dto.getFlrcNo());
            sBuff.append("\t");
            //加油日期
            sBuff.append(dto.getFlrcDate());
            sBuff.append("\t");
            //飞机号码
            sBuff.append(dto.getFlrcAircrftNo());
            sBuff.append("\t");
            //飞机类型
            sBuff.append(dto.getFlrcAircrftType());
            sBuff.append("\t");
            //飞机单位
            sBuff.append(dto.getFlightUnit());
            sBuff.append("\t");
            //航班号
            sBuff.append(dto.getFlrcFlightNo());
            sBuff.append("\t");
            //密度
            sBuff.append(dto.getFlrcFuelDnst());
            sBuff.append("\t");
            //温度
            sBuff.append(dto.getFlrcFuelTemp());
            sBuff.append("\t");
            //体积
            sBuff.append(dto.getFlrcFuelVol());
            sBuff.append("\t");
            //加油质量
            sBuff.append(dto.getFlrcQuantity());
            sBuff.append("\t");
            //加油车编号
            sBuff.append(dto.getFlrcVehiNo());
            sBuff.append("\t");
            //地井编号
            sBuff.append(dto.getFlrcHydrtPitNo());
            sBuff.append("\t");
            //加油员姓名
            sBuff.append(dto.getFlrcDeliverName());
            sBuff.append("\t");
            //加油开始时间
            sBuff.append(dto.getFlrcStatTime());
            sBuff.append("\t");
            //加油结束时间
            sBuff.append(dto.getFlrcFnshTime());
            sBuff.append("\t");
            //签名（收油人）
            sBuff.append(dto.getFlrcSign());
            sBuff.append("\t");
            //化验单编号
            sBuff.append(dto.getFlrcTestBillNo());
            sBuff.append("\r\n");
        }
        ExceportTextUtil.writeToTxt(response, sBuff.toString(), "开关控制-JSON_FOR_UCC");
    }

    /**
     * 航班订阅
     */
    @PostMapping(value = "/ReadFlight")
    public ReturnMsg<MyFsubscription> readflight(@RequestBody MyFsubscription fsubscription, @RequestBody MyStaff
            staff) {
        fuelRecptService.readflight(fsubscription, staff);
        return new ReturnMsg<MyFsubscription>(Constant.CODE_OK, null, null);

    }

    /**
     * 取消订阅
     */
    @DeleteMapping(value = "/DissFlight")
    public ReturnMsg<MyFsubscription> DissFlight(@RequestBody MyFsubscription fsubscription, @RequestBody MyStaff
            staff) {
        fuelRecptService.DissFlight(fsubscription, staff);
        return new ReturnMsg<MyFsubscription>(Constant.CODE_OK, null, null);

    }

    /**
     * 机场三字码 对应名称
     */
    @PostMapping(value = "/flightcode")
    public ReturnMsg<ArrayList<Object>> flightcode(@RequestBody MyStaff staff) {
        ArrayList<Object> sett = fuelRecptService.flightcode(staff);
        return new ReturnMsg<ArrayList<Object>>(Constant.CODE_OK, null, sett);

    }

    /**
     * 机场三字码 对应名称    修改返回值类型
     */
    @PostMapping(value = "flightcodeNew")
    public ReturnMsg<ArrayList<Map<String, Object>>> flightcodeNew(@RequestBody MyStaff staff) {
        ArrayList<Map<String, Object>> sett = fuelRecptService.flightcodeNew(staff);
        return new ReturnMsg<ArrayList<Map<String, Object>>>(Constant.CODE_OK, null, sett);

    }

    /**
     * 航班预建表增加
     */
    @PostMapping(value = "/insertFlightTemp")
    public ReturnMsg<MyFlightTemp> insertFlightTemp(@RequestBody MyFlightTemp myFlightTemp) {
        fuelRecptService.insertFlightTemp(myFlightTemp);
        return new ReturnMsg<MyFlightTemp>(Constant.CODE_OK, null, null);
    }

    /**
     * 航班预建修改
     */
    @PutMapping(value = "/updateFlightTemp")
    public ReturnMsg<MyFlightTemp> updateFlightTemp(@RequestBody MyFlightTemp myFlightTemp) {
        fuelRecptService.updateFlightTemp(myFlightTemp);
        return new ReturnMsg<MyFlightTemp>(Constant.CODE_OK, null, null);
    }

    /**
     * 航班预建删除
     */
    @DeleteMapping(value = "/deleteFlightTemp")
    public ReturnMsg<MyFlightTemp> deleteFlightTemp(@RequestBody MyFlightTemp myFlightTemp) {
        fuelRecptService.deleteFlightTemp(myFlightTemp);
        return new ReturnMsg<MyFlightTemp>(Constant.CODE_OK, null, null);
    }

    /**
     * 航班预建查询
     */
    @PostMapping(value = "/selectFlightTemp")
    public ReturnMsg<List<MyFlightTemp>> selectFlightTemp() {
        List<MyFlightTemp> mftemp = fuelRecptService.selectFlightTemp();
        return new ReturnMsg<List<MyFlightTemp>>(Constant.CODE_OK, null, mftemp);
    }

    /**
     * 航班预建详情
     */
    @PostMapping(value = "/selectFlightTempFind")
    public ReturnMsg<MyFlightTemp> selectFlightTempFind(@RequestBody MyFlightTemp myFlightTemp) {
        MyFlightTemp mftemp = fuelRecptService.selectFlightTempFind(myFlightTemp);
        return new ReturnMsg<MyFlightTemp>(Constant.CODE_OK, null, mftemp);
    }

    /**
     * @Param: [staff, recptList]
     * @return: com.zh.bean.ReturnMsg<com.zh.bean.flight.TaskAndRecpt>
     * @Author: XiuHongXin
     * @Date: 2019/8/23
     */
    @PostMapping("/padOfflineTasks")
    public ReturnMsg<TaskAndRecpt> padOfflineTasks(@RequestBody MyStaff staff, @RequestBody String recptList) {
        JSONObject jsonObject = JSONObject.parseObject(recptList);
        String fuels = jsonObject.getString("offlineTasks");
        if (StringUtils.isNotEmpty(fuels)) {
            List<TaskAndRecpt> list = JSON.parseArray(fuels, TaskAndRecpt.class);
            return fuelRecptService.handleTaskAndRecpt(list);
        }
        return ReturnMsg.getInstanceNGz("请求参数错误：offlineTasks is required", null);
    }

    /**
     * 当前员工任务油单查询
     */
    @OperationLogs("当前员工任务油单查询")
    @PostMapping(value = "/selectTaskAndRecpt/{dayType}")
    public ReturnMsg<List<Map<String, Object>>> selectTaskAndRecpt(@RequestBody MyStaff
                                                                           staff, @PathVariable(name = "dayType") Integer dayType) {
        List<Map<String, Object>> tartmap = fuelRecptService.selectTaskAndRecpt(staff, dayType);
        return new ReturnMsg<>(Constant.CODE_OK, null, tartmap);
    }

    /**
     * 当前员工任务油单查询    pad 的日期 放在 ->staffServStation
     */
    @OperationLogs("当前员工任务油单查询")
    @PostMapping(value = "/selectByDateTaskAndRecpt")
    public ReturnMsg<List<Map<String, Object>>> selectByDateTaskAndRecpt(
            @RequestBody MyFuelRecpt myFuelRecpt,
            @RequestBody MyStaff staff) {
        List<Map<String, Object>> tartmap = fuelRecptService.selectByDateTaskAndRecpt(myFuelRecpt, staff);
        return new ReturnMsg<>(Constant.CODE_OK, null, tartmap);
    }

    /**
     * 手持pad的打印消息
     */
    @OperationLogs("手持pad的打印消息")
    @TaskLogs("手持pad的打印消息")
    @PostMapping(value = "/padPrint")
    public ReturnMsg<String> padPrint(@RequestBody MyStaff staff, @RequestBody String printInfo) {
        if (printInfo != null && !"".equals(printInfo)) {
            System.out.println("手持pad:" + staff.getLoginUserIn().getStaffId() + "," + printInfo);
            ReturnMsg<String> msg = new ReturnMsg<String>(Constant.CODE_OK, null, null);
            return msg;
        } else {
            ReturnMsg<String> msg = new ReturnMsg<String>(Constant.CODE_ERR, "打印信息为空", null);
            return msg;
        }
    }

    /**
     * 加油员加油架次安排表
     */
    @OperationLogs("加油架次安排表")
    @TaskLogs("加油架次安排表")
    @PostMapping(value = "/exportStaffFuel")
    public void getTodayTasklist(HttpServletResponse response, @RequestBody MyStaff mystaff) {
        String flgtAirportCode = mystaff.getLoginUserIn().getStaffAirportCode();
        List<MyFlightTask> myFlightTasks = fuelRecptService.getTodayFligtTasklist(flgtAirportCode);
        if (myFlightTasks != null && myFlightTasks.size() > 0) {
            Map<String, List<MyFlightTask>> groupLisk = new HashMap<String, List<MyFlightTask>>();
            try {
                for (MyFlightTask myFlightTask : myFlightTasks) {
                    if (groupLisk.containsKey(myFlightTask.getTaskOpeStaffId())) {//map中该加油员已存在，将该数据存放到同一个key（key存放的是加油员id）的map中
                        groupLisk.get(myFlightTask.getTaskOpeStaffId()).add(myFlightTask);
                    } else {//map中不存在，新建key，用来存放数据
                        List<MyFlightTask> tmpList = new ArrayList<MyFlightTask>();
                        tmpList.add(myFlightTask);
                        groupLisk.put(myFlightTask.getTaskOpeStaffId(), tmpList);
                    }
                }
                int lie = myFlightTasks.get(myFlightTasks.size() - 1).getTaskCentTank();
                List<String> r = new ArrayList<>();
                r.add("组名");
                r.add("姓名");
                for (int l = 0; l < lie; l++) {
                    r.add("航班号/飞机号/机位号");
                }
                r.add("合计架次");
//				r.add("合计油量");
                String[] columnNames = r.toArray(new String[r.size()]);
                List<String> s = new ArrayList<>();
                s.add("groupid");
                s.add("staffname");
                for (int l = 0; l < lie; l++) {
                    s.add("flight" + l);
                }
                s.add("num");
//				s.add("totalfuel");
                String[] columns = s.toArray(new String[s.size()]);
                StringBuilder sBuff = new StringBuilder();
                List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                for (String key : groupLisk.keySet()) {
                    List<MyFlightTask> myFlightTaskList = groupLisk.get(key);
                    MyFlightTask temp = myFlightTaskList.get(0);
                    //垮库查询获取调度员ID
//					HttpHeaders headers = new HttpHeaders();
//					MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
//					headers.setContentType(type);
//					headers.add("Accept", MediaType.APPLICATION_JSON.toString());
//					MyStaff staffInfo = new MyStaff();
//					staffInfo.setStaffId(temp.getTaskOpeStaffId());
//					HttpEntity<MyStaff> formEntity = new HttpEntity<MyStaff>(staffInfo, headers);
                    //跨库查询后使用人员信息(暂时与地图共用)
//					ResponseEntity<MyStaff> rateResponse = restTemplate.exchange(Constant.HTTP + prop.getLoginIp() + ":" + prop.getLoginPort() + "/base/staffController/getStaffById", HttpMethod.POST, formEntity, new ParameterizedTypeReference<MyStaff>() {

//					});
                    MyStaff staff = staffMapper.getStaffById(temp.getTaskOpeStaffId());
//					MyStaff staff = rateResponse.getBody();
                    HashMap<String, String> listinfo = new HashMap<String, String>();
                    if (staff != null) {
                        //组名
                        if (staff.getStaffGroupId() != null) {
                            listinfo.put("groupid", staff.getStaffGroupId());
                        } else {
                            listinfo.put("groupid", ".");
                        }
                        //姓名
                        if (staff.getStaffName() != null) {
                            listinfo.put("staffname", staff.getStaffName());
                        } else {
                            listinfo.put("staffname", ".");
                        }
                    } else {
                        listinfo.put("groupid", ".");
                        listinfo.put("staffname", ".");
                    }
                    int num = 0;
                    int totalfuel = 0;
                    for (int n = 0; n < myFlightTaskList.size(); n++) {
                        MyFlightTask myFlightTask = myFlightTaskList.get(n);
                        //航班号/飞机号/机位号/（油量）
                        String info = "";
                        if (myFlightTask.getFlgtFlno() != null) {//航班号
                            info = info + myFlightTask.getFlgtFlno();
                        } else {
                            info = info + " ";
                        }
                        if (myFlightTask.getFlgtRegn() != null) {//飞机号
                            info = info + "/" + myFlightTask.getFlgtRegn();
                        } else {
                            info = info + "/ ";
                        }
                        if (myFlightTask.getFlgtPlacecode() != null) {//机位号
                            info = info + "/" + myFlightTask.getFlgtPlacecode();
                        } else {
                            info = info + "/ ";
                        }
//						if(myFlightTask.getTaskTotalFuel()!=null){//油量
//							info=info+"/"+myFlightTask.getTaskTotalFuel();
//						}else{
//							info=info+"/ ";
//						}
                        listinfo.put("flight" + n, info);
                        num = myFlightTaskList.size();
                        if (myFlightTask.getTaskTotalFuel() != null) {
                            totalfuel = totalfuel + myFlightTask.getTaskTotalFuel();
                        }
                    }
                    if (num < lie) {
                        for (int i = 0; i < lie - num; i++) {
                            int cha = num + i;
                            listinfo.put("flight" + cha, " ");
                        }
                    }
                    //合计架次
                    listinfo.put("num", num + "");
                    //合计油量
//					listinfo.put("totalfuel",totalfuel+"");
                    list.add(listinfo);
                }
                Date date = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
                String dateTime = df.format(date);
                if (list.size() > 2) {
                    Collections.sort(list, new Comparator<Map<String, String>>() {
                        public int compare(Map<String, String> o1, Map<String, String> o2) {
                            return o1.get("groupid").compareTo(o2.get("groupid"));
                        }
                    });
                }
                String sheetName = "加油架次安排表";
                String fileName = dateTime + "加油员加油架次安排表";
                ExcelUtils.exportStaffFuel(response, list, columnNames, columns, sheetName, fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询最近有效的油料参数 pad 用
     *
     * @param staff
     * @return
     */
    @OperationLogs("pad获取单号")
    @TaskLogs("pad获取单号")
    @PostMapping("/param")
    public ReturnMsg<Map<String, Object>> findNewFuelParam(@RequestBody TFuelNo tFuelNo, @RequestBody MyStaff staff) {
        return ReturnMsg.getInstanceOKz(fuelRecptService.findNewFuelParam(tFuelNo, staff));
    }

    /**
     * pad获取单号
     *
     * @param staff
     * @return
     */
    @TaskLogs("pad获取油单号")
    @OperationLogs("pad获取油单号")
    @PostMapping("/getFuelPad")
    public ReturnMsg<Map<String, Object>> getFuelPad(@RequestBody TFuelDistribution
                                                             tFuelDistribution, @RequestBody MyStaff staff) {
        Map<String, Object> fuelPad = fuelRecptService.getFuelPad(tFuelDistribution);
        return ReturnMsg.getInstanceOKz(fuelPad);
//        if(ObjectUtil.isNull(fuelPad) && ObjectUtil.equal(1,tFuelDistribution.getLogo())){
//            return ReturnMsg.getInstanceNGz("该设备已申请请勿重复申请油单！",null);
//        }else{
//            return ReturnMsg.getInstanceOKz(fuelPad);
//        }
    }

    /**
     * pad回收单号
     *
     * @param staff
     * @return
     */
    @TaskLogs("pad回收油单号")
    @OperationLogs("pad回收油单号")
    @PostMapping("/recycleFuelPad")
    public ReturnMsg<String> recycleFuelPad(@RequestBody RecycleFuelRequest request, @RequestBody MyStaff staff) {
        Integer integer = 0;
        if (CollectionUtil.isNotEmpty(request.gettFuelNo())) {
            integer = fuelRecptService.recycleFuelPad(request.gettFuelNo(), request.getPadId());
        }
        ReturnMsg<String> msg = new ReturnMsg<String>(Constant.CODE_OK, null, "本次回收油单" + integer + "条");
        return msg;
    }

    @PostMapping("/test")
    public ReturnMsg<String> test(@RequestBody List<String> ids) {
        ReturnMsg<String> msg = new ReturnMsg<String>(Constant.CODE_OK, null, "本次回收油单" + ids + "条");
        return msg;
    }


    @PostMapping(value = "/recptBase")
    public ReturnMsg<String> recptBase(@RequestBody MyFuelRecpt myFuelRecpt) {
        if (myFuelRecpt != null && StringUtils.isNotEmpty(myFuelRecpt.getFlrcId())) {
            System.out.println("油单id:" + myFuelRecpt.getFlrcId());
            String base = fuelRecptService.findRecptBase(myFuelRecpt.getFlrcId());
            if (StringUtils.isNotEmpty(base)) {

//				byte[] imgbyte = ImageUtils.pdf2image(Base64Utils.decodeFromString(base));
//				base = Base64Utils.encodeToString(imgbyte);
                ReturnMsg<String> msg = new ReturnMsg<String>(Constant.CODE_OK, null, base);
                return msg;
            } else {
                ReturnMsg<String> msg = new ReturnMsg<String>(Constant.CODE_ERR, "没有对应的信息", null);
                return msg;
            }
        } else {
            ReturnMsg<String> msg = new ReturnMsg<String>(Constant.CODE_ERR, "油单id为空", null);
            return msg;
        }
    }


    /**
     * 根据加油员姓名模糊查询加油员信息
     */
    @OperationLogs("根据加油员姓名模糊查询加油员信息")
    @PostMapping(value = "/findStaffByName")
    public ReturnMsg<List<MyStaff>> findStaffByName(@RequestBody MyStaff staff) {
        List<MyStaff> myStaffList = fuelRecptService.findStaffByName(staff);
        return new ReturnMsg<List<MyStaff>>(Constant.CODE_OK, null, myStaffList);

    }


    /**
     * 根据客户编号、飞机号查询信息
     */
    @OperationLogs("根据客户编号、飞机号查询信息")
    @PostMapping(value = "/getFuelData")
    public ReturnMsg<MyFuelRecpt> getFuelData(@RequestBody MyFuelVo fuelVo) {
        MyFuelRecpt fuelData = fuelRecptService.getFuelData(fuelVo);
        return new ReturnMsg<MyFuelRecpt>(Constant.CODE_OK, null, fuelData);
    }

    @OperationLogs("deleteFuel删除油单")
    @TaskLogs("deleteFuel删除油单")
    @DeleteMapping(value = "/deleteFuel")
    public ReturnMsg<MyFuelRecpt> deleteFuel(@RequestBody MyFuelRecpt fuelRecpt, @RequestBody MyStaff staff) {
        System.out.println("--删除油单---油单号{}" + fuelRecpt.getFlrcNo() + "----操作人{}----" + staff.getStaffName());
        MyFuelRecpt fuelData = fuelRecptService.deleteFuel(fuelRecpt, staff);
        return new ReturnMsg<MyFuelRecpt>(Constant.CODE_OK, null, fuelData);
    }

    /**
     * 功能描述： 打印油单
     *
     * @param
     * @return void
     * @author zhaojiacan
     * @date 2024/10/18
     */
    @PostMapping("/print")
    public ReturnMsg<String> printFuel(@RequestBody MyFuelRecpt fuelRecpt, @RequestBody MyStaff staff) {
        String staffId = staff.getLoginUserIn().getStaffId();
        String flrcId = fuelRecpt.getFlrcId();
        if (StrUtil.isBlank(flrcId)) {
            return new ReturnMsg<>(Constant.CODE_ERR, "油单ID不能为空", "");
        }
        MyFuelRecpt myFuelRecpt = myFuelRecptMapper.findFuelById(flrcId);
        if (myFuelRecpt == null) {
            return new ReturnMsg<>(Constant.CODE_ERR, "油单不存在", "");
        }
        fuelRecptService.printFuel(myFuelRecpt, staffId);
        return new ReturnMsg<>(Constant.CODE_OK, null, "");
    }


    @GetMapping("test")
    private String test() {
        fuelRecptService.test();
        return "success";
    }
}
