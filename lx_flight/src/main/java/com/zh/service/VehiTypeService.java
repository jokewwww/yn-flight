package com.zh.service;

import com.zh.bean.login.StaffBind;
import com.zh.bean.login.VehiType;
import com.zh.bean.login.VehiTypeStaff;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther:
 * @Date: 2021/9/13 11:25
 * @Description:
 */
public interface VehiTypeService {

    /**
     * 获取人员和不准驾车型
     *
     * @return
     */
    List<VehiTypeStaff> getVehiNoByStaffId(String staffId);

    /**
     * 绑定人员车辆关系
     *
     * @param staffBind
     * @return
     */
    int bindVehiNoByStaffId(StaffBind staffBind);

    /**
     * 车型列表
     *
     * @return
     */
    List<VehiType> getVehiType(VehiType vehiType);

    /**
     * 新增车型
     *
     * @param vehiType
     * @return
     */
    VehiType addVehiType(VehiType vehiType);

    /**
     * 修改车型
     *
     * @param vehiType
     * @return
     */
    int updateVehiType(VehiType vehiType);

    /**
     * 删除车型
     *
     * @param vehiType
     * @return
     */
    int deleteVehiType(VehiType vehiType);

    /**
     * 根据人员id获取不准驾车型
     *
     * @return
     */
    List<VehiTypeStaff> getNoVehiNoByStaffId(@Param("staffId") String staffId);
}
