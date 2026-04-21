package com.zh.dao.mapper;

import com.zh.bean.login.MyStaff;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StaffMapper {

    //获取人员列表
    List<MyStaff> getStaffList(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode, @Param("staffType") Integer staffType);

}
