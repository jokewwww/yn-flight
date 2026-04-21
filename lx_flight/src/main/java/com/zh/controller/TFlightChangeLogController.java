package com.zh.controller;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.TFlightChangeLog;
import com.zh.constant.Constant;
import com.zh.service.ITFlightChangeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller
 *
 * @author ruoyi
 * @date 2020-06-09
 */
@RestController
@RequestMapping("/tFlightChangeLog")
public class TFlightChangeLogController extends BaseController {

    @Autowired
    private ITFlightChangeLogService tFlightChangeLogService;

    /**
     * 查询
     */
    @PostMapping(value = "/findFlightChangeLog")
    public ReturnMsg<List<TFlightChangeLog>> getTaskAndFlight(@RequestBody TFlightChangeLog tFlightChangeLog) {
        List<TFlightChangeLog> tFlightChangeLogs = tFlightChangeLogService.selectTFlightChangeLogList(tFlightChangeLog);
        ReturnMsg<List<TFlightChangeLog>> msg = new ReturnMsg<List<TFlightChangeLog>>(Constant.CODE_OK, null, tFlightChangeLogs);
        return msg;
    }

}
