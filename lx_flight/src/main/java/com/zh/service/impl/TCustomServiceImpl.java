package com.zh.service.impl;

import com.zh.dao.mapper.TCustomMapper;
import com.zh.entity.TCustom;
import com.zh.service.TCustomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (TCustom)表服务实现类
 *
 * @author wgh
 * @since 2024-04-10 09:52:37
 */
@Service
public class TCustomServiceImpl implements TCustomService {
    @Resource
    private TCustomMapper tCustomMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param cstmNum 主键
     * @return 实例对象
     */
    @Override
    public TCustom queryById(String cstmNum) {
        return this.tCustomMapper.queryById(cstmNum);
    }

    /**
     * 分页查询
     *
     * @param tCustom 筛选条件
     * @return 查询结果
     */
    @Override
    public List<TCustom> queryList(TCustom tCustom) {
        return this.tCustomMapper.queryList(tCustom);
    }

    /**
     * 新增数据
     *
     * @param tCustom 实例对象
     * @return 实例对象
     */
    @Override
    public TCustom insert(TCustom tCustom) {
        this.tCustomMapper.insert(tCustom);
        return tCustom;
    }

    /**
     * 修改数据
     *
     * @param tCustom 实例对象
     * @return 实例对象
     */
    @Override
    public TCustom update(TCustom tCustom) {
        this.tCustomMapper.update(tCustom);
        return this.queryById(tCustom.getCstmNum());
    }

    /**
     * 通过主键删除数据
     *
     * @param cstmNum 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String cstmNum) {
        return this.tCustomMapper.deleteById(cstmNum) > 0;
    }
}
