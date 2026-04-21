package com.zh.service;

import com.zh.bean.flight.TFlight;
import com.zh.bean.login.MyStaff;
import com.zh.bean.login.MyVehi;
import com.zh.bean.login.TStaff;
import com.zh.bean.login.TVehiInfo;

import java.util.List;
import java.util.Map;

public interface VehiService {

    /**
     * 获取车辆数量（总数，在线，保障中）
     */
    Map<String, Object> getVehiSum(TStaff staff);

    /**
     * 车辆信息接口
     * 以（所属机场代码＝输入_机场ID）的条件，取得车辆表（DB）中对象记录的如下字段，写入输出接口
     */
    List<TVehiInfo> getVehiList(TFlight flight);

    /**
     * 手动添加车辆信息接口
     *
     * @param staff
     * @return
     */
    Integer addVehiInfo(MyVehi vehi, MyStaff staff);

    /**
     * 根据机场代码和车辆号获取车辆油单编号
     */
    Map<String, Object> getFuelRecptNo(MyVehi vehi);

    /**
     * 根据机场代码和车辆号获取车辆油单编号和序号
     */
    Map<String, Object> getFuelRecptNoAndFuelSno(MyVehi vehi);

    /**
     * 更新车辆油单序号
     *
     * @return
     */
    Integer updateVehiFuelSno(MyVehi vehi);

}
