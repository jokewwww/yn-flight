package com.zh.service;

import com.zh.entity.TCustom;

import java.util.List;


/**
 * (TCustom)表服务接口
 *
 * @author makejava
 * @since 2024-04-10 09:52:24
 */
public interface TCustomService {

    /**
     * 通过ID查询单条数据
     *
     * @param cstmNum 主键
     * @return 实例对象
     */
    TCustom queryById(String cstmNum);

    /**
     * 分页查询
     *
     * @param tCustom 筛选条件
     * @return 查询结果
     */
    List<TCustom> queryList(TCustom tCustom);

    /**
     * 新增数据
     *
     * @param tCustom 实例对象
     * @return 实例对象
     */
    TCustom insert(TCustom tCustom);

    /**
     * 修改数据
     *
     * @param tCustom 实例对象
     * @return 实例对象
     */
    TCustom update(TCustom tCustom);

    /**
     * 通过主键删除数据
     *
     * @param cstmNum 主键
     * @return 是否成功
     */
    boolean deleteById(String cstmNum);

}
