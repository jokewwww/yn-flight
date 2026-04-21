package com.zh.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.zh.bean.ReturnMsg;
import com.zh.bean.login.*;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.MySettingMapper;
import com.zh.exception.CustomException;
import com.zh.service.SettingService;
import com.zh.service.TParamService;
import com.zh.util.JsonHelper;
import com.zh.util.ModelAssistant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private MySettingMapper mysettingmapper;

    @Autowired
    private TParamService tParamService;

    /**
     * 显示配置信息
     */
    @Override
    public String findsettinglist(MySetting setting, MyStaff staff) {
        String staffid = "default";
        if (StringUtils.isBlank(staff.getStaffId())) {
            staffid = staff.getLoginUserIn().getStaffId();
        } else {
            staffid = staff.getStaffId();
        }
        String mysting = mysettingmapper.findsettinglist(staff.getLoginUserIn().getStaffAirportCode(), staffid, setting.getSettType());

        return mysting;

    }

    /**
     * 新增用户配置信息
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void insertsetting(MySetting setting) {
        String uuid = UUID.randomUUID().toString();
        setting.setSettOptionId(uuid);
        if (mysettingmapper.insertsetting(setting) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
        }

    }

    /**
     * 修改用户配置信息
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void updatesetting(MySetting setting) {
        //查询人员信息
        MyStaff staff = mysettingmapper.selectstaff(setting.getSettStaffId());
        //如果员工岗位为调度员去修改
        if ("2".equals(staff.getStaffType())) {
            String settInfo = JsonHelper.object2str(setting.getSettInfomap()).getData();
            if (mysettingmapper.updatesetting(setting, settInfo) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            }

        }

    }

    /**
     * 新增配置信息
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public MySetting addSettingInfo(MySetting setting, MyStaff staff) {
        //先根据当前人员的ID去查询行的最大优先级
        Integer rowPriority = mysettingmapper.getRowPriority(staff.getLoginUserIn().getStaffId());
        if (rowPriority != null) {
            if ("2".equals(setting.getSettType()) && 1 == setting.getSettRowOrCol()) {
                setting.setSettPriority(rowPriority + 1);
            } else {
                setting.setSettPriority(0);
            }
        } else {
            setting.setSettPriority(1);
        }
        String uuid = UUID.randomUUID().toString();
        setting.setSettOptionId(uuid);
        setting.setSettAirportCode(staff.getLoginUserIn().getStaffAirportCode());
        setting.setSettStaffId(staff.getLoginUserIn().getStaffId());
        MySetting settingInfo = new MySetting();
        if (mysettingmapper.addSettingInfo(setting) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
        } else {
            settingInfo.setSettOptionId(uuid);
            settingInfo.setSettInfo(setting.getSettInfo());
            settingInfo.setSettPriority(setting.getSettPriority());
        }
        return settingInfo;
    }

    /**
     * 查询配置列表
     */
    @Override
    public List<Row> getSettingList(MySetting setting, MyStaff staff) {
        return mysettingmapper.getSettingList(setting.getSettType(), staff.getLoginUserIn().getStaffId());
    }

    /**
     * 查询配置详情
     */
    @Override
    public MySetting getSettingInfo(MySetting setting) {
        return mysettingmapper.getSettingInfo(setting.getSettOptionId());
    }

    /**
     * 删除配置信息
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void deleteSettingInfo(MySetting setting, MyStaff staff) {
        if (setting != null) {
            MySetting settingInfo = mysettingmapper.getSettingInfo(setting.getSettOptionId());
            if (settingInfo != null) {
                if (mysettingmapper.deleteSettingInfo(settingInfo.getSettOptionId(), settingInfo.getSettStaffId()) != 1) {
                    throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
                } else {
                    if (0 < settingInfo.getSettPriority()) {
                        if (mysettingmapper.updateSettPriority(settingInfo.getSettStaffId(), settingInfo.getSettPriority()) < 0) {
                            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
                        }
                    }
                }
            }
        }
    }

    /**
     * 修改配置信息
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void updateSettingInfo(MySetting setting, MyStaff staff) {
        if (mysettingmapper.updateSettingInfo(setting, staff.getLoginUserIn().getStaffId()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 是否执行配置信息
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void updateSettingStutas(MySetting setting, MyStaff staff) {
        //判断如果前台传执行标识等于1就去把此用户的所有配置执行标识改为0（除了当前传过来这一条）
        if (setting != null && setting.getSettStatus() == 1) {
            mysettingmapper.updateSettingStutass(setting.getSettOptionId(), staff.getLoginUserIn().getStaffId());
        }
        //修改是否执行标识
        if (mysettingmapper.updateSettingStutas(setting.getSettOptionId(), setting.getSettStatus()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    @Override
    public String getColList(MySetting setting, MyStaff staff) {
        return mysettingmapper.getColList(setting.getSettType(), staff.getLoginUserIn().getStaffId());
    }

    /**
     * 修改配置信息行的优先级
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void updateSettPriority(MyStaff staff, MySettPriority settPriority) {
        if (null != settPriority.getMaxSettId() && !"".equals(settPriority.getMaxSettId()) && null != settPriority.getMinSettId() && !"".equals(settPriority.getMinSettId())) {
            MySetting maxSettingInfo = mysettingmapper.getSettingInfo(settPriority.getMaxSettId());
            MySetting minSettingInfo = mysettingmapper.getSettingInfo(settPriority.getMinSettId());
            if (maxSettingInfo != null && minSettingInfo != null) {
                //把大的优先级改小一级
                if (mysettingmapper.updateMaxSettPriority(staff.getLoginUserIn().getStaffId(), maxSettingInfo.getSettOptionId(), minSettingInfo.getSettPriority()) != 1) {
                    throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
                } else {
                    //把小的优先级改大一级
                    if (mysettingmapper.updateMinSettPriority(staff.getLoginUserIn().getStaffId(), maxSettingInfo.getSettPriority(), minSettingInfo.getSettOptionId()) != 1) {
                        throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
                    }
                }
            }
        }
    }

    @Override
    public String getSettingInfoOne(TStaff tStaff) {
        MySetting settingInfo = mysettingmapper.getSettingInfo("9999999999");
        if (null != settingInfo) {
            return settingInfo.getSettInfo();
        }
        return null;
    }

    @Override
    public InSttingEntity updateSettingInfoOne(InSttingEntity inSttingEntity) {
        Objects.requireNonNull(inSttingEntity, "数据不能为空");
        Assert.hasText(inSttingEntity.getId(), "ID不能为空");
        MySetting settingInfo = mysettingmapper.getSettingInfo("9999999999");
        if (null != settingInfo) {
            Assert.hasText(settingInfo.getSettInfo(), "DB值不能为空");
            List<InSttingEntity> inSttingEntities = JSON.parseObject(settingInfo.getSettInfo(), new TypeReference<ArrayList<InSttingEntity>>() {
            });
            List<InSttingEntity> collect = inSttingEntities.stream()
                    .filter(o -> o.getId().equals(inSttingEntity.getId()))
                    .peek(o ->
                            ModelAssistant.copyProperties(inSttingEntity, o)
                    )
                    .collect(Collectors.toList());
            if (!inSttingEntities.isEmpty()) {
                String settInfo = JSON.toJSONString(inSttingEntities, SerializerFeature.WriteMapNullValue);
                int updatesetting = mysettingmapper.updatesetting(settingInfo, settInfo);
                if (updatesetting != 1)
                    throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
                return inSttingEntity;
            }
        }
        return null;
    }

    @Override
    public List<InSttingEntity> updateSettingInfoAll(String inSttingEntity) {
        Assert.hasText(inSttingEntity, "数据不能为空");
        JSONObject jsonObject = JSONObject.parseObject(inSttingEntity);
        String settingDataJson = jsonObject.getString("settingDataJson");
        if (StringUtils.isNotEmpty(settingDataJson)) {
            MySetting settingInfo = mysettingmapper.getSettingInfo("9999999999");
            int updatesetting = mysettingmapper.updatesetting(settingInfo, settingDataJson);
            if (updatesetting != 1)
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
            List<InSttingEntity> inSttingEntities = JSON.parseObject(settingDataJson, new TypeReference<ArrayList<InSttingEntity>>() {
            });
            return inSttingEntities;
        }
        return null;
    }

    @Override
    public Map<String, Object> findNewSettingList(MyStaff staff) {
        String staffid = "default";
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map.put("head", null);
            map.put("color", null);
            List<MySetting> mysting = mysettingmapper.findsettingLists(staff.getLoginUserIn().getStaffAirportCode(), staffid);
            mysting.forEach(setting -> {
                if (StringUtils.isNotEmpty(setting.getSettType())) {
                    if (setting.getSettType().equals("1")) {
                        map.put("head", setting);
                    }
                    if (setting.getSettType().equals("2")) {
                        map.put("color", setting);
                    }
                }
            });
            List<TParam> allByPcTparam = tParamService.getAllByPcTparam(staff.getLoginUserIn().getStaffAirportCode());
            map.put("tParams", allByPcTparam.size() == 0 ? allByPcTparam : Lists.newArrayList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}