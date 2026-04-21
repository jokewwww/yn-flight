package com.zh.service;

import com.zh.bean.flight.TFlightCodeTemporary;

import java.util.List;

/**
 * @Auther:
 * @Date: 2021/9/13 11:25
 * @Description:
 */
public interface TFlightCodeTemporaryService {

    /**
     * 飞机临时号码基本信息
     *
     * @return
     */
    List<TFlightCodeTemporary> getTFlightCodeTemporary(TFlightCodeTemporary tFlightCodeTemporary);

    /**
     * 新增飞机临时号码基本信息
     *
     * @param tFlightCodeTemporary
     * @return
     */
    TFlightCodeTemporary addTFlightCodeTemporary(TFlightCodeTemporary tFlightCodeTemporary);

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
     * @param tFlightCodeTemporary
     * @return
     */
    int deleteTFlightCodeTemporary(TFlightCodeTemporary tFlightCodeTemporary);

}
