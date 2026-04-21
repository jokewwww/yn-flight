package com.zh.service;

import com.zh.bean.login.*;

import java.util.List;

/**
 * @Auther:
 * @Date: 2021/9/13 11:25
 * @Description:
 */
public interface FlgtTypeService {

    /**
     * 获取人员和不保障机型
     *
     * @return
     */
    List<FlgtTypeStaff> getFlgtTypeByStaffId(String staffId);

    /**
     * 绑定保障机型与人员关系
     *
     * @param staffBind
     * @return
     */
    int bindFlgtTypeByStaffId(StaffBind staffBind);

    /**
     * 保障机型列表
     *
     * @return
     */
    List<FlgtType> getFlgtType(FlgtType flgtType);

    /**
     * 新增机型
     *
     * @param flgtType
     * @return
     */
    FlgtType addFlgtType(FlgtType flgtType);

    /**
     * 修改机型
     *
     * @param flgtType
     * @return
     */
    int updateFlgtType(FlgtType flgtType);

    /**
     * 删除机型
     *
     * @param flgtType
     * @return
     */
    int deleteFlgtType(FlgtType flgtType);

    /**
     * 根据人员id获取不保障机型
     *
     * @return
     */
    List<FlgtTypeStaff> getNoFlgtTypeByStaffId(String staffId);

    /**
     * 获取车型和不保障机型
     *
     * @return
     */
    List<FlgtTypeVehi> getFlgtTypeByVehi(String flgtTypeId);

    /**
     * 绑定保障机型车型关系
     *
     * @param vehiBind
     * @return
     */
    int bindFlgtTypeBySVehi(VehiBind vehiBind);

    /**
     * 根据人员id获取是否已设置不可保障该机型
     *
     * @return
     */
    int getNoFlgtNoByStaffId(String staffId, String type);
}
