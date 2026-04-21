package com.zh.controller;

import com.zh.annotation.TaskLogs;
import com.zh.bean.OutMessage;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyTask;
import com.zh.bean.login.*;
import com.zh.constant.Constant;
import com.zh.service.StaffService;
import com.zh.util.JsonHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工表(童雪俊)
 */
@RestController
@RequestMapping(value = "/staffController")
public class StaffController extends BaseController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 根据加油员ID获取人员车辆表的信息（供远程调用）
     */
    @PostMapping(value = "/getStaffVehiInfo")
    public MyStaffVehi getStaffVehiInfo(@RequestBody MyTask task) {
        return staffService.getStaffVehiInfo(task.getTaskOpeStaffId());
    }

    /**
     * 根据加油车编号查出对应的车辆信息（供远程调用）
     */
    @PostMapping(value = "/getVehiInfo")
    public MyVehi getVehiInfo(@RequestBody MyStaffVehiTask staffTask) {
        if (StringUtils.isBlank(staffTask.getVehiNo()) || StringUtils.isBlank(staffTask.getSfvhStaffId())) {
            return null;
        }
        return staffService.getVehiInfo(staffTask);
    }

    /**
     * 通过人员id查询人员信息（供地图使用）
     */
    @PostMapping(value = "/getStaffById")
    public TStaff getStaffById(@RequestBody TStaff staff) {
        //通过人员id查询人员信息
        return staffService.getStaffById(staff.getStaffId());
    }

    /**
     * 获取所有调度员ID（供远程调用使用）
     */
    @PostMapping(value = "/getStaffList")
    public List<MyStaff> getStaffList(@RequestBody MyStaff staffInfo) {
        //获取所有调度员ID（供远程调用使用）
        return staffService.getStaffList(staffInfo.getStaffAirportCode(), staffInfo.getStaffAptareaCode());
    }

    /**
     * 获取所有调度员ID（供远程调用使用）
     */
    @PostMapping(value = "/getAllStaffList")
    public Map<String, String> getAllStaffList(@RequestBody MyStaff staffInfo) {
        Map<String, String> result = new HashMap<String, String>();
        List<MyStaff> res = new ArrayList<MyStaff>();
        //获取所有调度员ID（供远程调用使用）
        res = staffService.getAllStaffList(staffInfo.getStaffAirportCode(), staffInfo.getStaffAptareaCode());
        for (MyStaff map : res) {
            result.put(map.getStaffId(), map.getStaffName());
        }
        return result;
    }

    /**
     * 获取所有人员ID（供远程调用使用）
     */
    @PostMapping(value = "/getStaffLists")
    public List<MyStaff> getStaffLists(@RequestBody MyStaff staffInfo) {
        //获取所有人员ID（供远程调用使用）
        return staffService.getStaffLists(staffInfo.getStaffAirportCode(), staffInfo.getStaffAptareaCode());
    }

    /**
     * 平板端人员登录
     */
    @TaskLogs("pad登录")
    @PostMapping(value = "/staffLogin")
    public ReturnMsg<Map<String, Object>> staffLogin(@RequestBody MyStaff staff) {
        Map<String, Object> staffInfoMap = staffService.staffLogin(staff.getStaffId(), staff.getStaffPwd());
        //使用errer为key去map集合中取对应的value，判断如果为null说明登录成功，返回给前台正确信息
        if (null == staffInfoMap.get(Constant.LOGIN_ERROR_KEY)) {
            ReturnMsg<Map<String, Object>> msg = new ReturnMsg<Map<String, Object>>(Constant.CODE_OK, null, staffInfoMap);
            return msg;
        } else {
            //否则就会使用errer为key把map集合中的错误信息取出来返回给前台
            String errorinfo = (String) staffInfoMap.get(Constant.LOGIN_ERROR_KEY);
            ReturnMsg<Map<String, Object>> msg = new ReturnMsg<Map<String, Object>>(Constant.CODE_ERR, errorinfo, null);
            return msg;
        }
    }

    /**
     * PC端人员登录
     */
    @PostMapping(value = "/PCstaffLogin")
    public ReturnMsg<Map<String, Object>> PCstaffLogin(@RequestBody MyStaff staff, HttpServletResponse response) {
        Map<String, Object> staffInfoMap = staffService.PCstaffLogin(staff.getStaffId(), staff.getStaffPwd());
        //使用errer为key去map集合中取对应的value，判断如果为null说明登录成功，返回给前台正确信息
        if (null == staffInfoMap.get(Constant.LOGIN_ERROR_KEY)) {
            ReturnMsg<Map<String, Object>> msg = new ReturnMsg<Map<String, Object>>(Constant.CODE_OK, null, staffInfoMap);
            return msg;
        } else {
            //否则就会使用errer为key把map集合中的错误信息取出来返回给前台
            String errorinfo = (String) staffInfoMap.get(Constant.LOGIN_ERROR_KEY);
            ReturnMsg<Map<String, Object>> msg = new ReturnMsg<Map<String, Object>>(Constant.CODE_ERR, errorinfo, null);
            return msg;
        }
    }

    /**
     * PC端管理系统人员登录
     */
    @PostMapping(value = "/PCManagestaffLogin")
    public ReturnMsg<Map<String, Object>> PCManagestaffLogin(@RequestBody MyStaff staff) {
        Map<String, Object> staffInfoMap = staffService.PCManagestaffLogin(staff.getStaffId(), staff.getStaffPwd());
        //使用errer为key去map集合中取对应的value，判断如果为null说明登录成功，返回给前台正确信息
        if (null == staffInfoMap.get(Constant.LOGIN_ERROR_KEY)) {
            ReturnMsg<Map<String, Object>> msg = new ReturnMsg<Map<String, Object>>(Constant.CODE_OK, null, staffInfoMap);
            return msg;
        } else {
            //否则就会使用errer为key把map集合中的错误信息取出来返回给前台
            String errorinfo = (String) staffInfoMap.get(Constant.LOGIN_ERROR_KEY);
            ReturnMsg<Map<String, Object>> msg = new ReturnMsg<Map<String, Object>>(Constant.CODE_ERR, errorinfo, null);
            return msg;
        }
    }

    /**
     * 平板端人员登出(注销)
     *
     * @return
     */
    @PostMapping(value = "/removeStaff")
    public ReturnMsg<Object> removeStaff(@RequestBody MyStaff staff) {
        staffService.removeStaff(staff);
        logoutWS(staff);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 清空加油员的连续工作时间和连续任务数
     *
     * @return
     */
    @PostMapping(value = "/cleanStaff")
    public ReturnMsg<Object> cleanStaff(@RequestBody MyStaff staff) {
        staffService.cleanStaff(staff);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 通知前端下线
     *
     * @param staff
     */
    private void logoutWS(MyStaff staff) {
        OutMessage<String> outMessage = new OutMessage<String>();
        outMessage.setFlg(Constant.STAFF);
        outMessage.setTo(Constant.PAD_STAFF_OFFLINE);
        if (staff.getLoginUserIn() != null) {
            outMessage.setTo(staff.getLoginUserIn().getStaffId());
        } else {
            outMessage.setTo(staff.getStaffId());
        }
        stringRedisTemplate.convertAndSend("chatLogout", JsonHelper.object2str(outMessage).getData());
    }

    /**
     * PC端人员登出(注销)
     *
     * @return
     */
    @PostMapping(value = "/PCremoveStaff")
    public ReturnMsg<Object> PCremoveStaff(@RequestBody MyStaff staff) {
        staffService.PCremoveStaff(staff);
        logoutWS(staff);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * (pad绑车接口)人员车辆绑定 人车绑定
     */
    @PostMapping(value = "/staffAndVehi")
    @TaskLogs("pad绑车")
    public ReturnMsg<String> staffAndVehi(@RequestBody MyStaffVehi staffVehi) {
        Integer staffAndVehi = staffService.staffAndVehi(staffVehi);
        if (staffAndVehi == 1) {
            return new ReturnMsg<String>(Constant.CODE_OK, null, "绑定成功！");
        } else {
            return new ReturnMsg<String>(Constant.CODE_ERR, null, "此车已经被绑定！");
        }
    }

    /**
     * 根据加油员Id判断人车是否绑定 人车绑定
     */
    @PostMapping(value = "/judgeStaffAndVehi")
    public ReturnMsg<MyStaffVehi> judgeStaffAndVehi(@RequestBody MyVehi tVehiInfo, @RequestBody MyStaff staff) {
        MyStaffVehi myStaffVehi = staffService.judgeStaffAndVehi(tVehiInfo, staff);
        return new ReturnMsg<MyStaffVehi>(Constant.CODE_OK, null, myStaffVehi);
    }

    /**
     * 根据加油员Id判断人车是否绑定 人车绑定
     */
    @PostMapping(value = "/judgeVehiIsBind")
    public ReturnMsg<MyStaffVehi> judgeVehiIsBind(@RequestBody MyVehi tVehiInfo, @RequestBody MyStaff staff) {
        MyStaffVehi myStaffVehi = staffService.judgeVehiIsBind(tVehiInfo, staff);
        return new ReturnMsg<MyStaffVehi>(Constant.CODE_OK, null, myStaffVehi);
    }


    /**
     * 根据加油员Id获取人员详细信息如果已经绑定车辆显示车辆车牌号
     */
    @PostMapping(value = "/getStaffInfoAndVehiNo")
    public ReturnMsg<MyStaff> getStaffInfoAndVehiNo(@RequestBody MyStaff staff) {
        MyStaff staffInfo = staffService.getStaffInfoAndVehiNo(staff.getStaffId());
        ReturnMsg<MyStaff> msg = new ReturnMsg<MyStaff>(Constant.CODE_OK, null, staffInfo);
        return msg;
    }

    /**
     * (pad解绑接口)人车解绑
     */
    @PostMapping(value = "/deleteStaffVehi")
    public ReturnMsg<Object> deleteStaffVehi(@RequestBody MyStaff staff) {
        staffService.deleteStaffVehi(staff);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 根据机场代码获取人员的分组名
     */
    @PostMapping(value = "/getStaffGroupName")
    public ReturnMsg<List<Object>> getStaffGroupName(@RequestBody MyStaff staff) {
        List<Object> staffGroupNameList = staffService.getStaffGroupName(staff);
        ReturnMsg<List<Object>> msg = new ReturnMsg<List<Object>>(Constant.CODE_OK, null, staffGroupNameList);
        return msg;
    }

    /**
     * 修改分组
     *
     * @param staffvehi
     * @param staff
     * @return
     */
    @PutMapping(value = "/updateGruop")
    public ReturnMsg<Object> updateGruop(@RequestBody MyGroup staffvehi, @RequestBody MyStaff staff) {
        staffService.updateGruop(staffvehi.getUpdateStaffArr(), staff);
        return new ReturnMsg<Object>(Constant.CODE_OK, null, null);

    }

    /**
     * 根据角色类型查出该角色的常用消息
     */
    @PostMapping(value = "/getStaffMessageByType")
    public ReturnMsg<List<MyMessage>> getStaffMessageByType(@RequestBody MyStaff staff) {
        List<MyMessage> staffMessageList = staffService.getStaffMessageByType(staff);
        ReturnMsg<List<MyMessage>> msg = new ReturnMsg<List<MyMessage>>(Constant.CODE_OK, null, staffMessageList);
        return msg;
    }

    /**
     * 根据机场代码查出该角色的常用消息
     */
    @PostMapping(value = "/getStaffMessageByCode")
    public ReturnMsg<List<MyMessage>> getStaffMessageByCode(@RequestBody MyStaff staff) {
        List<MyMessage> staffMessageList = staffService.getStaffMessageByCode(staff);
        ReturnMsg<List<MyMessage>> msg = new ReturnMsg<List<MyMessage>>(Constant.CODE_OK, null, staffMessageList);
        return msg;
    }

    /**
     * 根据消息ID删除消息
     */
    @DeleteMapping(value = "/deleteStaffMessage")
    public ReturnMsg<Object> deleteStaffMessage(@RequestBody MyMessage message) {
        staffService.deleteStaffMessage(message);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 新增消息内容
     */
    @PostMapping(value = "/addStaffMessage")
    public ReturnMsg<Object> addStaffMessage(@RequestBody MyStaff staff, @RequestBody MyMessage message) {
        staffService.addStaffMessage(staff, message);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 查询销售信息
     */
    @PostMapping(value = "/getSalesInfo")
    public ReturnMsg<List<MySalesInfo>> getSalesInfo(@RequestBody MyStaff staff) {
        List<MySalesInfo> salesInfoList = staffService.getSalesInfo(staff);
        ReturnMsg<List<MySalesInfo>> msg = new ReturnMsg<List<MySalesInfo>>(Constant.CODE_OK, null, salesInfoList);
        return msg;
    }

    /**
     * 销售信息详情
     */
    @PostMapping(value = "/getSalesInfoById")
    public ReturnMsg<MySalesInfo> getSalesInfoById(@RequestBody MySalesInfo salesInfo) {
        MySalesInfo salesInfoById = staffService.getSalesInfoById(salesInfo);
        ReturnMsg<MySalesInfo> msg = new ReturnMsg<MySalesInfo>(Constant.CODE_OK, null, salesInfoById);
        return msg;
    }

    /**
     * 删除销售信息
     */
    @DeleteMapping(value = "/deleteSalesInfo")
    public ReturnMsg<Object> deleteSalesInfo(@RequestBody MySalesInfo salesInfo) {
        staffService.deleteSalesInfo(salesInfo);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 新增销售信息
     */
    @PostMapping(value = "/addSalesInfo")
    public ReturnMsg<Object> addSalesInfo(@RequestBody MyStaff staff, @RequestBody MySalesInfo salesInfo) {
        staffService.addSalesInfo(staff, salesInfo);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 修改销售信息
     */
    @PutMapping(value = "/updateSalesInfo")
    public ReturnMsg<Object> updateSalesInfo(@RequestBody MySalesInfo salesInfo) {
        staffService.updateSalesInfo(salesInfo);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 根据机场代码获取所有的加油员信息（供远程调用）
     */
    @PostMapping(value = "/getStaffInfoList")
    public List<MyStaff> getStaffInfoList(@RequestBody MyStaff staff) {
        return staffService.getStaffInfoList(staff);
    }


    @PostMapping("/deleteToken")
    public void deleteToken(@RequestBody MyStaff staff) {
        staffService.deleteToken(staff);
    }

}
