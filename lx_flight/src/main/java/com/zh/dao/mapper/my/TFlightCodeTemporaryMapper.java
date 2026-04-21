package com.zh.dao.mapper.my;

import com.zh.bean.flight.TFlightCodeTemporary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TFlightCodeTemporaryMapper {

    /**
     * 查询飞机临时号码基本信息
     *
     * @param tFlightCodeTemporary
     * @return
     */
    List<TFlightCodeTemporary> getTFlightCodeTemporary(TFlightCodeTemporary tFlightCodeTemporary);

    TFlightCodeTemporary getOne(TFlightCodeTemporary tFlightCodeTemporary);

    /**
     * 新增飞机临时号码基本信息
     *
     * @param tFlightCodeTemporary
     * @return
     */
    int addTFlightCodeTemporary(TFlightCodeTemporary tFlightCodeTemporary);

    /**
     * 修改飞机临时号码基本信息
     *
     * @param tFlightCodeTemporary
     * @return
     */
    int updateTFlightCodeTemporary(TFlightCodeTemporary tFlightCodeTemporary);

    /**
     * 删除飞机临时号码基本信息
     *
     * @param id
     * @return
     */
    int deleteTFlightCodeTemporary(@Param("id") Integer id);


}
