package com.zh.dao.mapper.my;

import com.zh.bean.login.MySetting;
import com.zh.bean.login.MyStaff;
import com.zh.bean.login.Row;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MySettingMapper {


    int insertsetting(MySetting setting);

    MyStaff selectstaff(@Param("settStaffId") String settStaffId);


    String findsettinglist(@Param("staffAirportCode") String staffAirportCode, @Param("staffId") String staffId, @Param("settType") String settType);

    int updatesetting(@Param("setting") MySetting setting, @Param("settInfo") String settInfo);

    /**
     * 新增配置信息
     */
    int addSettingInfo(@Param("setting") MySetting setting);

    /**
     * 查询配置列表
     */
    List<Row> getSettingList(@Param("settType") String settType, @Param("staffId") String staffId);

    /**
     * 删除配置信息
     *
     * @param staffId
     */
    int deleteSettingInfo(@Param("settOptionId") String settOptionId, @Param("staffId") String staffId);

    /**
     * 删除配置信息
     *
     * @param staffId
     */
    int deleteSetting(@Param("staffId") String staffId);

    /**
     * 查询配置详情
     */
    MySetting getSettingInfo(@Param("settOptionId") String settOptionId);

    /**
     * 修改配置信息
     *
     * @param string
     */
    int updateSettingInfo(@Param("setting") MySetting setting, @Param("staffId") String staffId);

    /**
     * 是否执行配置信息
     */
    int updateSettingStutas(@Param("settOptionId") String settOptionId, @Param("settStatus") Integer settStatus);

    /**
     * 配置执行标识改为0
     */
    void updateSettingStutass(@Param("settOptionId") String settOptionId, @Param("staffId") String staffId);

    String getColList(@Param("settType") String settType, @Param("staffId") String staffId);

    /**
     * 根据当前人员的ID去查询行的最大优先级
     */
    Integer getRowPriority(@Param("staffId") String staffId);

    //把大的优先级改小一级
    int updateMaxSettPriority(@Param("staffId") String staffId, @Param("maxSettId") String maxSettId, @Param("minSettPriority") Integer minSettPriority);

    //把小的优先级改大一级
    int updateMinSettPriority(@Param("staffId") String staffId, @Param("maxSettPriority") Integer maxSettPriority, @Param("minSettId") String minSettId);

    int updateSettPriority(@Param("staffId") String staffId, @Param("settPriority") Integer settPriority);

    List<MySetting> findsettingLists(@Param("staffAirportCode") String staffAirportCode, @Param("staffId") String staffId);
}
