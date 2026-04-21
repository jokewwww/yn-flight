package com.zh.controller;

import com.zh.annotation.TaskLogs;
import com.zh.bean.ReturnMsg;
import com.zh.bean.login.MyStaff;
import com.zh.bean.login.MyStaffVehiTask;
import com.zh.bean.login.MyVehi;
import com.zh.constant.Constant;
import com.zh.service.StaffVehiTaskService;
import com.zh.util.PinyinUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 人员车辆表
 */
@RestController
@RequestMapping(value = "/staffvehi")
public class StaffVehiController extends BaseController {

    @Autowired
    private StaffVehiTaskService staffService;

    /**
     * 查询人员车辆信息
     *
     * @return
     */
    @PostMapping(value = "/findstaffvehi")
    public ReturnMsg<List<MyStaffVehiTask>> findstaffvehi(@RequestBody MyStaff stask) {
        List<MyStaffVehiTask> staffvehi = staffService.findStaffvehi(stask);
        staffvehi.forEach(myStaffVehiTask -> {
            String pinyinToUpperCase = PinyinUtils.getPinyinToUpperCase(myStaffVehiTask.getStaffName());
            if (!StringUtils.isEmpty(pinyinToUpperCase) && !"0".equals(pinyinToUpperCase)) {
                myStaffVehiTask.setAlpha(pinyinToUpperCase);
            }
        });

        //胡老大在2019年4月12日23:02:49让LRC注掉
       /* // 按登录未登录排序
        Collections.sort(staffvehi, new Comparator<MyStaffVehiTask>() {
            public int compare(MyStaffVehiTask arg0, MyStaffVehiTask arg1) {
                int hits0 = arg0.getSfvhStaffStatus();
                int hits1 = arg1.getSfvhStaffStatus();
                if (hits1 > hits0) {
                    return 1;
                } else if (hits1 == hits0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });*/


        return new ReturnMsg<List<MyStaffVehiTask>>(Constant.CODE_OK, null, staffvehi);

    }

    /**
     * 查询车辆信息(所属机场所有车辆)
     */
    @PostMapping(value = "/findVehi")
    public ReturnMsg<List<MyVehi>> findListVehi(@RequestBody MyStaff staff) {
        List<MyVehi> staffvehi = staffService.findListVehi(staff);
        return new ReturnMsg<List<MyVehi>>(Constant.CODE_OK, null, staffvehi);
    }

    /**
     * 显示登录人员所属机场的人员列表
     */
    @PostMapping(value = "/findStafflist")
    public ReturnMsg<List<MyStaff>> findstafflist(@RequestBody MyStaff staff) {
        List<MyStaff> staffs = staffService.findstafflist(staff);
        return new ReturnMsg<List<MyStaff>>(Constant.CODE_OK, null, staffs);

    }

    /**
     * 新增人员信息
     *
     * @param staff
     * @return
     */
    @PostMapping(value = "/insertstaff")
    public ReturnMsg<MyStaff> insertstaff(@RequestBody MyStaff staff) {
        Integer insertstaff = staffService.insertstaff(staff);
        if (insertstaff == 1) {
            return new ReturnMsg<MyStaff>(Constant.CODE_ERR, "账号已经存在！", null);
        } else {
            return new ReturnMsg<MyStaff>(Constant.CODE_OK, null, null);
        }
    }

    /**
     * 修改人员信息
     */
    @PutMapping(value = "/updatestaff")
    @TaskLogs("修改人员信息")
    public ReturnMsg<MyStaff> updatestaff(@RequestBody MyStaff staff) {
        staffService.updatestaff(staff);
        return new ReturnMsg<MyStaff>(Constant.CODE_OK, null, null);
    }

    /**
     * 删除人员信息
     */
    @DeleteMapping(value = "/deletestaff")
    public ReturnMsg<MyStaff> deletestaff(@RequestBody MyStaff staff) {
        staffService.deletestaff(staff);
        return new ReturnMsg<MyStaff>(Constant.CODE_OK, null, null);
    }

    /**
     * 修改车辆信息
     *
     * @param vehi
     * @return
     */
    @PutMapping(value = "/updatevehi")
    public ReturnMsg<MyVehi> updatevehi(@RequestBody MyVehi vehi) {
        Integer updatevehi = staffService.updatevehi(vehi);
        if (updatevehi == 0) {
            return new ReturnMsg<MyVehi>(Constant.CODE_ERR, "编号已存在！", null);
        } else if (updatevehi == 2) {
            return new ReturnMsg<MyVehi>(Constant.CODE_ERR, "车牌号已存在！", null);
        } else {
            return new ReturnMsg<MyVehi>(Constant.CODE_OK, null, null);
        }
    }

    /**
     * 删除车辆信息
     *
     * @param vehi
     * @return
     */
    @DeleteMapping(value = "/deletevehi")
    public ReturnMsg<MyVehi> deletevehi(@RequestBody MyVehi vehi) {
        staffService.deletevehi(vehi);
        return new ReturnMsg<MyVehi>(Constant.CODE_OK, null, null);
    }

    /**
     * 人员详情
     */
    @PostMapping(value = "/staffonefind")
    public ReturnMsg<MyStaff> staffonefind(@RequestBody MyStaff staff) {
        MyStaff staffs = staffService.staffonefind(staff);
        return new ReturnMsg<MyStaff>(Constant.CODE_OK, null, staffs);
    }

    /**
     * 车辆详情
     */
    @PostMapping(value = "/vehionefind")
    public ReturnMsg<MyVehi> vehionefind(@RequestBody MyVehi vehi) {
        MyVehi vehis = staffService.vehionefind(vehi);
        return new ReturnMsg<MyVehi>(Constant.CODE_OK, null, vehis);
    }

    /**
     * 查询人员姓名是否存在（垮库）
     *
     * @param staff
     * @return
     */
    @PostMapping(value = "/selectstaffname")
    public int selectstaffname(@RequestBody MyStaff staff) {
        int i = staffService.selectstaffname(staff);
        return i;
    }
}
