package com.zh.service;

import com.zh.bean.login.*;

import java.util.List;
import java.util.Map;

public interface SettingService {

    String findsettinglist(MySetting setting, MyStaff staff);

    void insertsetting(MySetting setting);

    void updatesetting(MySetting setting);

    /**
     * 新增配置信息
     *
     * @return
     */
    MySetting addSettingInfo(MySetting setting, MyStaff staff);

    /**
     * 查询配置列表
     */
    List<Row> getSettingList(MySetting setting, MyStaff staff);

    /**
     * 删除配置信息
     *
     * @param staff
     */
    void deleteSettingInfo(MySetting setting, MyStaff staff);

    /**
     * 查询配置详情
     */
    MySetting getSettingInfo(MySetting setting);

    /**
     * 修改配置信息
     *
     * @param staff
     */
    void updateSettingInfo(MySetting setting, MyStaff staff);

    /**
     * 是否执行配置信息
     *
     * @param staff
     */
    void updateSettingStutas(MySetting setting, MyStaff staff);

    String getColList(MySetting setting, MyStaff staff);

    /**
     * 修改配置信息行的优先级
     */
    void updateSettPriority(MyStaff staff, MySettPriority settPriority);


    String getSettingInfoOne(TStaff tStaff);

    InSttingEntity updateSettingInfoOne(InSttingEntity inSttingEntity);

    List<InSttingEntity> updateSettingInfoAll(String inSttingEntity);

    Map<String, Object> findNewSettingList(MyStaff staff);
}
