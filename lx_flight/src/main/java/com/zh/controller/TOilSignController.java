package com.zh.controller;


import com.alibaba.fastjson.JSON;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.TOilSign;
import com.zh.constant.Constant;
import com.zh.service.TOilSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 */
@RestController
@RequestMapping(value = "/tOilSignController")
public class TOilSignController extends BaseController {

    @Autowired
    private TOilSignService oilSignService;

    /**
     * 查询
     */
    @PostMapping(value = "/select")
    public ReturnMsg<List<TOilSign>> select() {
        List<TOilSign> toilSigns = oilSignService.select();
        return new ReturnMsg<List<TOilSign>>(Constant.CODE_OK, null, toilSigns);
    }

    @PostMapping(value = "/selectOilSign")
    public ReturnMsg<TOilSign> selectOilSign(@RequestBody TOilSign oilSign) {
        TOilSign toilSign = oilSignService.selectOilSign(oilSign);
        return new ReturnMsg<TOilSign>(Constant.CODE_OK, null, toilSign);
    }

    @PostMapping(value = "/selectByFlgtId")
    public ReturnMsg<TOilSign> selectByFlgtId(@RequestBody TOilSign oilSign) {
        TOilSign toilSign = oilSignService.selectByFlgtId(oilSign);
        return new ReturnMsg<TOilSign>(Constant.CODE_OK, null, toilSign);
    }

    @PostMapping(value = "/insertOilSign")
    public ReturnMsg<TOilSign> insertOilSign(@RequestBody TOilSign oilSign) {
        System.out.println("新建--oilSign---" + JSON.toJSONString(oilSign));
        Integer integer = oilSignService.insertOilSign(oilSign);
        if (integer == 1) {
            return new ReturnMsg<TOilSign>(Constant.CODE_OK, "已经新增", null);
        }
        return new ReturnMsg<TOilSign>(Constant.CODE_OK, null, null);
    }

    @PostMapping(value = "/updateOilSign")
    public ReturnMsg<TOilSign> updateOrderInfo(
            @RequestBody TOilSign oilSign) {
        // 修改
        Integer integer = oilSignService.updateOilSign(oilSign);
        if (integer == 1) {
            return new ReturnMsg<TOilSign>(Constant.CODE_OK, "修改成功", null);
        }
        return new ReturnMsg<TOilSign>(Constant.CODE_OK, null, null);
    }

    /**
     * 删除
     */
    @DeleteMapping(value = "/deleteOilSign")
    public ReturnMsg<TOilSign> deleteOilSign(@RequestBody TOilSign oilSign) {
        oilSignService.deleteOilSign(oilSign);
        return new ReturnMsg<TOilSign>(Constant.CODE_OK, null, null);

    }

}
