package com.zh.dao.mapper.my;

import com.zh.bean.login.MyFuel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface NewFuelMapper {


    /**
     * 根据机场代码和化验单编号查询
     *
     * @param fuel
     * @return
     */
    MyFuel findById(@Param("fuel") MyFuel fuel);

    /**
     * 查询当天油料信息
     *
     * @param staffAirportCode
     * @return
     */
    List<MyFuel> findNewFuelsToday(@Param("staffAirportCode") String staffAirportCode);

    /**
     * 新增油料参数
     *
     * @param fuel
     * @param staffName
     * @return
     */
    int addNewFuel(@Param("fuel") MyFuel fuel, @Param("staffName") String staffName);

    /**
     * 更新油料参数
     *
     * @param fuel
     * @param staffName
     * @return
     */
    int updateNewFuel(@Param("fuel") MyFuel fuel, @Param("staffName") String staffName);

    /**
     * 根据机场代码和化验单编号更新和油料参数状态
     *
     * @param fuel
     * @param flag
     * @return
     */
    int updateNewFuelFlag(@Param("fuel") MyFuel fuel, @Param("flag") int flag);


    /**
     * 根据机场代码查询最近的可以使用的油料参数
     *
     * @param staffAirportCode
     * @return
     */
    List<MyFuel> findOneByNow(@Param("staffAirportCode") String staffAirportCode);


    /**
     * 根据机场代码查询未来可以使用的油料参数
     *
     * @param staffAirportCode
     * @return
     */
    List<MyFuel> findOneByFuture(@Param("staffAirportCode") String staffAirportCode);

    /**
     * 根据时间查询所有机场的油料参数
     *
     * @param date
     * @return
     */
    List<MyFuel> findByDate(@Param("date") Date date);

}
