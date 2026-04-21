package com.zh.dao.mapper.my;

import com.zh.bean.login.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FlgtTypeMapper {

    /**
     * 获取人员和不保障机型
     *
     * @return
     */
    List<FlgtTypeStaff> getFlgtTypeByStaffId(@Param("staffId") String staffId);

    /**
     * 删除绑定保障机型与人员关系
     *
     * @param staffId
     * @return
     */
    int deleteBindByStaffId(@Param("staffId") String staffId);

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

    FlgtType getOne(FlgtType flgtType);

    /**
     * 新增机型
     *
     * @param flgtType
     * @return
     */
    Integer addFlgtType(FlgtType flgtType);

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
     * @param id
     * @return
     */
    int deleteFlgtType(@Param("id") Integer id);

    /**
     * 根据人员id获取不保障机型
     *
     * @return
     */
    List<FlgtTypeStaff> getNoFlgtTypeByStaffId(@Param("staffId") String staffId);


    /**
     * 获取车型和不保障机型
     *
     * @return
     */
    List<FlgtTypeVehi> getFlgtTypeByVehi(@Param("flgtTypeId") String flgtTypeId);

    /**
     * 删除绑定关系
     *
     * @param flgtTypeId
     * @return
     */
    int deleteBindByVehi(@Param("flgtTypeId") Integer flgtTypeId);

    /**
     * 绑定保障机型车型关系
     *
     * @param vehiBind
     * @return
     */
    int bindFlgtTypeBySVehi(VehiBind vehiBind);

    /**
     * 根据人员id获取不保障机型
     *
     * @return
     */
    List<FlgtTypeStaff> getNoFlgtByStaffId(@Param("staffId") String staffId, @Param("type") String type);

    /**
     * 根据人员id查询绑定车号
     *
     * @param staffId
     * @return
     */
    MyStaffVehi getSfvhVehiNo(@Param("staffId") String staffId);

    /**
     * 根据加油车编号获取不保障机型
     *
     * @return
     */
    List<FlgtTypeVehi> getNoFlgtByVehiNo(@Param("sfvhVehiNo") String sfvhVehiNo, @Param("type") String type);

}
