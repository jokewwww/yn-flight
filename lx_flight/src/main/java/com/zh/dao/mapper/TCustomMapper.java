package com.zh.dao.mapper;

import com.zh.entity.TCustom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (TCustom)表数据库访问层
 *
 * @author wgg
 * @since 2024-04-10 09:52:21
 */
@Mapper
public interface TCustomMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param cstmNum 主键
     * @return 实例对象
     */
    TCustom queryById(String cstmNum);

    /**
     * 查询指定行数据
     *
     * @param tCustom 查询条件
     * @return 对象列表
     */
    List<TCustom> queryList(TCustom tCustom);

    /**
     * 统计总行数
     *
     * @param tCustom 查询条件
     * @return 总行数
     */
    long count(TCustom tCustom);

    /**
     * 新增数据
     *
     * @param tCustom 实例对象
     * @return 影响行数
     */
    int insert(TCustom tCustom);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TCustom> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TCustom> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TCustom> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TCustom> entities);

    /**
     * 修改数据
     *
     * @param tCustom 实例对象
     * @return 影响行数
     */
    int update(TCustom tCustom);

    /**
     * 通过主键删除数据
     *
     * @param cstmNum 主键
     * @return 影响行数
     */
    int deleteById(String cstmNum);

}

