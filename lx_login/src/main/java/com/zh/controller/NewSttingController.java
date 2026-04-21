package com.zh.controller;

import com.zh.bean.ReturnMsg;
import com.zh.bean.login.MyStaff;
import com.zh.constant.Constant;
import com.zh.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/9/30 15:52
 * @Description: 页面配置表
 */
@RestController
@RequestMapping(value = "/newSetting")
public class NewSttingController {

    @Autowired
    private SettingService settingService;

    /**
     * 显示配置信息
     */
    @PostMapping(value = "/settingList")
    public ReturnMsg<Map<String, Object>> findSettlist(
            @RequestBody MyStaff staff
    ) {
        Map<String, Object> settings = settingService.findNewSettingList(staff);
        //List<InSttingEntity> sett = JSON.parseObject(settings, new TypeReference<ArrayList<InSttingEntity>>() {});
        return new ReturnMsg<Map<String, Object>>(Constant.CODE_OK, null, settings);
    }
}
