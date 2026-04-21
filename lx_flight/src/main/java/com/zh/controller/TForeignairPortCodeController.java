package com.zh.controller;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.TForeignairportCode;
import com.zh.constant.Constant;
import com.zh.service.TForeignairPortCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 航空公司代码表
 */
@RestController
@RequestMapping(value = "/foreignairAirPortCode")
public class TForeignairPortCodeController extends BaseController {

    @Autowired
    private TForeignairPortCodeService foreignairPortCodeService;

    /**
     * 添加外国航空公司
     *
     * @param foreignairportCode
     * @return
     */
    @PostMapping(value = "/insert")
    public ReturnMsg<TForeignairportCode> insertAirlinesCode(@RequestBody TForeignairportCode foreignairportCode) {
        if (foreignairportCode.getAlcdIcaoCode().length() != 2) {
            return new ReturnMsg(Constant.CODE_ERR, "机场二字码只能是2位长度的字符!", null);
        }
        Integer insertCode = foreignairPortCodeService.insert(foreignairportCode);
        if (insertCode == 1) {
            return new ReturnMsg<>(Constant.CODE_ERR, "已存在!", null);
        } else {
            return new ReturnMsg<>(Constant.CODE_OK, null, null);
        }
    }

    /**
     * 修改外国航空公司信息
     */
    @PutMapping(value = "/update")
    public ReturnMsg<TForeignairportCode> updateAirlinesCode(@RequestBody TForeignairportCode foreignairportCode) {
        foreignairPortCodeService.update(foreignairportCode);
        return new ReturnMsg<>(Constant.CODE_OK, null, null);
    }

    /**
     * 根据ID外国航空公司信息
     */
    @DeleteMapping(value = "/delete")
    public ReturnMsg<TForeignairportCode> deleteAirlinesCode(@RequestBody TForeignairportCode foreignairportCode) {
        foreignairPortCodeService.delete(foreignairportCode);
        return new ReturnMsg<>(Constant.CODE_OK, null, null);
    }

    /**
     * 查询外国航空公司
     */
    @PostMapping(value = "/selectAll")
    public ReturnMsg<List<TForeignairportCode>> selectAirlinesCode() {
        List<TForeignairportCode> airlinescode = foreignairPortCodeService.selectList();
        return new ReturnMsg<>(Constant.CODE_OK, null, airlinescode);

    }

    /**
     * 航空公司详情
     */
    @PostMapping(value = "/selectByCode")
    public ReturnMsg<TForeignairportCode> selectAirlinesCodeFind(@RequestBody TForeignairportCode foreignairportCode) {
        TForeignairportCode tForeignairportCode = foreignairPortCodeService.selectByCode(foreignairportCode);
        return new ReturnMsg<>(Constant.CODE_OK, null, tForeignairportCode);
    }
}
