package com.zh.controller;

import com.alibaba.fastjson.JSON;
import com.zh.bean.ReturnMsg;
import com.zh.bean.login.*;
import com.zh.constant.Constant;
import com.zh.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 页面配置表
 */
@RestController
@RequestMapping(value = "/setting")
public class SettingController extends BaseController {

    @Autowired
    private SettingService settingService;

    /**
     * 显示配置信息
     */
    @PostMapping(value = "/settingList")
    public ReturnMsg<HashMap<String, Object>> findSettlist(@RequestBody MySetting setting, @RequestBody MyStaff staff) {
        HashMap<String, Object> sett = null;
        try {
            String settings = settingService.findsettinglist(setting, staff);
            sett = JSON.parseObject(settings, HashMap.class);
            //sett = (HashMap<String, Object>) JsonHelper.str2Object(settings,HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ReturnMsg<HashMap<String, Object>>(Constant.CODE_OK, null, sett);
    }


    /**
     * 新建用户的配置信息
     */
    @PostMapping(value = "/insetting")
    public ReturnMsg<MySetting> insertsetting(@RequestBody MySetting setting) {
        settingService.insertsetting(setting);
        return new ReturnMsg<MySetting>(Constant.CODE_OK, null, null);

    }

    /**
     * 修改用户配置信息
     */
    @PutMapping(value = "/updatesetting")
    public ReturnMsg<MySetting> updatesetting(@RequestBody MySetting setting) {
        settingService.updatesetting(setting);
        return new ReturnMsg<MySetting>(Constant.CODE_OK, null, null);
    }

    /**
     * 新增配置信息
     */
    @PostMapping(value = "/addSettingInfo")
    public ReturnMsg<MySetting> addSettingInfo(@RequestBody MySetting setting, @RequestBody MyStaff staff) {
        MySetting settingInfo = settingService.addSettingInfo(setting, staff);
        ReturnMsg<MySetting> msg = new ReturnMsg<MySetting>(Constant.CODE_OK, null, settingInfo);
        return msg;
    }

    /**
     * 查询配置列表
     */
    @PostMapping(value = "/getSettingList")
    public ReturnMsg<Map<String, Object>> getSettingList(@RequestBody MySetting setting, @RequestBody MyStaff staff) {
        List<Row> settingList = settingService.getSettingList(setting, staff);
        String colList = settingService.getColList(setting, staff);
        Map<String, Object> row = new HashMap<String, Object>();
        row.put("row", settingList);
        row.put("col", colList);
        ReturnMsg<Map<String, Object>> msg = new ReturnMsg<Map<String, Object>>(Constant.CODE_OK, null, row);
        return msg;
    }

    /**
     * 查询配置详情
     */
    @PostMapping(value = "/getSettingInfo")
    public ReturnMsg<MySetting> getSettingInfo(@RequestBody MySetting setting) {
        MySetting settingInfo = settingService.getSettingInfo(setting);
        ReturnMsg<MySetting> msg = new ReturnMsg<MySetting>(Constant.CODE_OK, null, settingInfo);
        return msg;
    }

    /**
     * 新加的 查询配置详情 one default type = 1
     *
     */
    @PostMapping(value = "/getSettingInfoOne")
    public ReturnMsg<String> getSettingInfoOne(@RequestBody TStaff tStaff) {
        String settingInfo = settingService.getSettingInfoOne(tStaff);
        ReturnMsg<String> msg = new ReturnMsg<String>(Constant.CODE_OK, null, settingInfo);
        return msg;
    }

    /**
     * 新加的 修改配置详情 one default type = 1
     *
     */
    @PostMapping(value = "/updateSettingInfoOne")
    public ReturnMsg<InSttingEntity> updateSettingInfoOne(@RequestBody MyStaff staff, @RequestBody InSttingEntity inSttingEntity) {
        InSttingEntity settingInfo = settingService.updateSettingInfoOne(inSttingEntity);
        ReturnMsg<InSttingEntity> msg = new ReturnMsg<InSttingEntity>(Constant.CODE_OK, null, settingInfo);
        return msg;
    }

    /**
     * 新加的 修改排序 one default type = 1
     *
     */
    @PostMapping(value = "/updateSettingInfoAll")
    public ReturnMsg<List<InSttingEntity>> updateSettingInfoAll(@RequestBody String inSttingEntity) {
        List<InSttingEntity> settingInfo = settingService.updateSettingInfoAll(inSttingEntity);
        ReturnMsg<List<InSttingEntity>> msg = new ReturnMsg<List<InSttingEntity>>(Constant.CODE_OK, null, settingInfo);
        return msg;
    }


    /**
     * 删除配置信息
     */
    @DeleteMapping(value = "/deleteSettingInfo")
    public ReturnMsg<Object> deleteSettingInfo(@RequestBody MySetting setting, @RequestBody MyStaff staff) {
        settingService.deleteSettingInfo(setting, staff);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 修改配置信息
     */
    @PutMapping(value = "/updateSettingInfo")
    public ReturnMsg<Object> updateSettingInfo(@RequestBody MySetting setting, @RequestBody MyStaff staff) {
        settingService.updateSettingInfo(setting, staff);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 修改配置信息行的优先级
     */
    @PutMapping(value = "/updateSettPriority")
    public ReturnMsg<Object> updateSettPriority(@RequestBody MyStaff staff, @RequestBody MySettPriority settPriority) {
        settingService.updateSettPriority(staff, settPriority);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }

    /**
     * 是否执行配置信息
     */
    @PutMapping(value = "/updateSettingStutas")
    public ReturnMsg<Object> updateSettingStutas(@RequestBody MySetting setting, @RequestBody MyStaff staff) {
        settingService.updateSettingStutas(setting, staff);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, null);
        return msg;
    }
}
