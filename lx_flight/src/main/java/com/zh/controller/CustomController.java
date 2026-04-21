package com.zh.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zh.bean.ResponseObject;
import com.zh.entity.TCustom;
import com.zh.service.TCustomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @classname: CustomController
 * @author: zhaojiacan
 * @description: 加油客户信息
 * @date: 2024/4/10 9:49
 * @version:1.0
 */
@RequestMapping("/custom")
@RestController
@Api(tags = "加油客户信息")
public class CustomController {

    @Autowired
    private TCustomService tCustomService;

    /**
     * 分页查询
     *
     * @param tCustom 筛选条件
     * @return 查询结果
     */
    @PostMapping("/queryList")
    @ApiOperation(value = "查询加油客户信息")
    public ResponseObject<List<TCustom>> queryByPage(@RequestBody TCustom tCustom) {
        Integer paginate = tCustom.getPaginate();
        Integer pageSize = tCustom.getPageSize();
        if (paginate != null && pageSize != null) {
            PageHelper.startPage(paginate, pageSize);
            List<TCustom> tCustoms = this.tCustomService.queryList(tCustom);
            PageInfo<TCustom> tCustomPageInfo = new PageInfo<>(tCustoms);
            return new ResponseObject<>("0", "", tCustomPageInfo.getList(), tCustomPageInfo);
        } else {
            List<TCustom> tCustoms = this.tCustomService.queryList(tCustom);
            return new ResponseObject<>("0", "", tCustoms, null);
        }
    }
}
