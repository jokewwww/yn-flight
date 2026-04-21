package com.zh.controller;


import com.zh.annotation.OperationLogs;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.TFlightTrouble;
import com.zh.bean.login.MyStaff;
import com.zh.constant.Constant;
import com.zh.service.FlightTroubleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther:
 * @Date: 2019/11/20
 * @Description:
 */
@RestController
@RequestMapping(value = "/tFlightTroubleController")
@OperationLogs("机障管理")
public class TFlightTroubleController {

    @Autowired
    private FlightTroubleService flightTroubleService;

    /**
     * 查询机障
     */
    @PostMapping(value = "/getFlightTrouble")
    public ReturnMsg<List<TFlightTrouble>> getFlightTrouble(@RequestBody TFlightTrouble tFlightTrouble) {
        List<TFlightTrouble> flightTroubles = flightTroubleService.selectAll(tFlightTrouble);
        ReturnMsg<List<TFlightTrouble>> msg = new ReturnMsg<List<TFlightTrouble>>(Constant.CODE_OK, null, flightTroubles);
        return msg;
    }

    /**
     * 查询机障详情
     */
    @PostMapping(value = "/getTFlightTroubleInfo")
    public ReturnMsg<TFlightTrouble> getTFlightTrouble(@RequestBody TFlightTrouble tFlightTrouble) {
        TFlightTrouble flightTrouble = flightTroubleService.selectByPrimaryKey(tFlightTrouble.getId());
        ReturnMsg<TFlightTrouble> msg = new ReturnMsg<TFlightTrouble>(Constant.CODE_OK, null, flightTrouble);
        return msg;
    }

    /**
     * 机障增加
     *
     * @param tFlightTrouble
     * @return
     */
    @PostMapping(value = "/insertTFlightTrouble")
    @OperationLogs("新增机障")
    public ReturnMsg<TFlightTrouble> insertTFlightTrouble(@RequestBody MyStaff staff, @RequestBody TFlightTrouble tFlightTrouble) {
        TFlightTrouble insertCode = flightTroubleService.insertSelective(tFlightTrouble);
        return new ReturnMsg<TFlightTrouble>(Constant.CODE_OK, null, insertCode);
    }

    /**
     * 机障修改
     *
     * @param tFlightTrouble
     * @return
     */
    @PostMapping(value = "/updateTFlightTrouble")
    @OperationLogs("修改机障")
    public ReturnMsg<TFlightTrouble> updateTFlightTrouble(@RequestBody MyStaff staff, @RequestBody TFlightTrouble tFlightTrouble) {
        TFlightTrouble insertCode = flightTroubleService.updateByPrimaryKeySelective(tFlightTrouble);
        return new ReturnMsg<TFlightTrouble>(Constant.CODE_OK, null, insertCode);
    }

    /**
     * 删除机障
     */
    @PostMapping(value = "/deleteTFlightTrouble")
    @OperationLogs("删除机障")
    public ReturnMsg<TFlightTrouble> deleteTFlightTrouble(@RequestBody MyStaff staff, @RequestBody TFlightTrouble tFlightTrouble) {
        flightTroubleService.deleteByPrimaryKey(tFlightTrouble.getId());
        return new ReturnMsg<TFlightTrouble>(Constant.CODE_OK, null, tFlightTrouble);

    }

}
