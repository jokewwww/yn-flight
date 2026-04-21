package com.zh.dao.mapper.my;

import com.zh.bean.login.MyServAirport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SerAiportMapper {

    MyServAirport findfwflight(@Param("svapAptplacNo") String svapAptplacNo);

    // 根据机位查询机场区域代码CODE
    String getAirportAreaCode(@Param("svapAptplacNo") String svapAptplacNo, @Param("svapAirportCode") String svapAirportCode);

    //根据机场区域代码。机场代码查询服务表
    List<MyServAirport> selectairport(@Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //新增服务表
    int insertairport(MyServAirport servAirport);

    //对服务表进行删除
    int deleteairport(@Param("svapAptplacNo") String svapAptplacNo, @Param("svapHydrtPitNo") String svapHydrtPitNo, @Param("staffAirportCode") String staffAirportCode, @Param("staffAptareaCode") String staffAptareaCode);

    //同步信息
    void inserport(@Param("svapAptareaCode") String svapAptareaCode, @Param("svapAptplacNo") String svapAptplacNo, @Param("svapHydrtPitNo") String svapHydrtPitNo, @Param("staffAirportCode") String staffAirportCode);

    //根据机场代码 删除有关此机场代码的信息
    void delirport(@Param("staffAirportCode") String staffAirportCode);

    List<MyServAirport> selectServAirport(MyServAirport servAirport);

}
