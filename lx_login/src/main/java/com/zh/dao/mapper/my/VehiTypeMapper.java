package com.zh.dao.mapper.my;

import com.zh.bean.login.VehiTypeStaff;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VehiTypeMapper {

    /**
     * 根据人员id获取不准驾车型
     *
     * @return
     */
    VehiTypeStaff getNoVehiNoByStaffIdSfvhVehiNo(@Param("staffId") String staffId, @Param("sfvhVehiNo") String sfvhVehiNo, @Param("vehiAirportCode") String vehiAirportCode);

}
