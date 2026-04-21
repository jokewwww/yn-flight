package com.zh.controller;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.TFlightCodeTemporary;
import com.zh.bean.login.MyStaff;
import com.zh.constant.Constant;
import com.zh.service.TFlightCodeTemporaryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 飞机临时号码基本信息
 */
@RestController
@RequestMapping(value = "/tFlightCodeTemporaryController")
@Api(tags = "飞机临时号码基本信息")
public class TFlightCodeTemporaryController extends BaseController {

    @Autowired
    private TFlightCodeTemporaryService tFlightCodeTemporaryService;

    /**
     * 查询飞机临时号码基本信息
     */
    @GetMapping(value = "/getTFlightCodeTemporary")
    public ReturnMsg<List<TFlightCodeTemporary>> getTFlightCodeTemporary(TFlightCodeTemporary tFlightCodeTemporary, MyStaff staff) {
        List<TFlightCodeTemporary> vehiType = tFlightCodeTemporaryService.getTFlightCodeTemporary(tFlightCodeTemporary);
        ReturnMsg<List<TFlightCodeTemporary>> msg = new ReturnMsg<List<TFlightCodeTemporary>>(Constant.CODE_OK, null, vehiType);
        return msg;
    }

    /**
     * 新增飞机临时号码基本信息
     */
    @PostMapping(value = "/addTFlightCodeTemporary")
    public ReturnMsg<Object> addVehiType(@RequestBody TFlightCodeTemporary tFlightCodeTemporary) {
        TFlightCodeTemporary entity = tFlightCodeTemporaryService.addTFlightCodeTemporary(tFlightCodeTemporary);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, entity);
        return msg;
    }

    /**
     * 修改飞机临时号码基本信息
     */
    @PostMapping(value = "/updateTFlightCodeTemporary")
    public ReturnMsg<Object> updateTFlightCodeTemporary(@RequestBody TFlightCodeTemporary tFlightCodeTemporary) {
        int entity = tFlightCodeTemporaryService.updateTFlightCodeTemporary(tFlightCodeTemporary);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, entity);
        return msg;
    }

    /**
     * 删除飞机临时号码基本信息
     */
    @PostMapping(value = "/deleteTFlightCodeTemporary")
    public ReturnMsg<Object> deleteTFlightCodeTemporary(@RequestBody TFlightCodeTemporary tFlightCodeTemporary) {
        int entity = tFlightCodeTemporaryService.deleteTFlightCodeTemporary(tFlightCodeTemporary);
        ReturnMsg<Object> msg = new ReturnMsg<Object>(Constant.CODE_OK, null, entity);
        return msg;
    }

}
