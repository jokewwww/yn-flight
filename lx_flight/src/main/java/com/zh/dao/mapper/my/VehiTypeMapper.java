package com.zh.dao.mapper.my;

import com.zh.bean.login.StaffBind;
import com.zh.bean.login.VehiType;
import com.zh.bean.login.VehiTypeStaff;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VehiTypeMapper {

    /**
     * 获取人员和不准驾车型
     *
     * @return
     */
    List<VehiTypeStaff> getVehiNoByStaffId(@Param("staffId") String staffId, @Param("vehiAirportCode") String vehiAirportCode);

    /**
     * 删除绑定车辆与人员关系
     *
     * @param staffId
     * @return
     */
    int deleteBindByStaffId(@Param("staffId") String staffId);

    /**
     * 绑定车辆与人员关系
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

    VehiType getOneVehiType(VehiType vehiType);

    /**
     * 新增车型
     *
     * @param vehiType
     * @return
     */
    int addVehiType(VehiType vehiType);

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
     * @param id
     * @return
     */
    int deleteVehiType(@Param("id") Integer id);

    /**
     * 根据人员id获取不准驾车型
     *
     * @return
     */
    List<VehiTypeStaff> getNoVehiNoByStaffId(@Param("staffId") String staffId);

}
