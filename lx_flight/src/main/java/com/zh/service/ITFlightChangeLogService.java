package com.zh.service;

import com.zh.bean.flight.TFlightChangeLog;

import java.util.List;

/**
 * Service接口
 *
 * @author ruoyi
 * @date 2020-06-09
 */
public interface ITFlightChangeLogService {
    /**
     * 查询
     *
     * @param id ID
     * @return
     */
    public TFlightChangeLog selectTFlightChangeLogById(Long id);

    /**
     * 查询列表
     *
     * @param tFlightChangeLog
     * @return 集合
     */
    public List<TFlightChangeLog> selectTFlightChangeLogList(TFlightChangeLog tFlightChangeLog);

    /**
     * 新增
     *
     * @param tFlightChangeLog
     * @return 结果
     */
    public int insertTFlightChangeLog(TFlightChangeLog tFlightChangeLog);

    /**
     * 修改
     *
     * @param tFlightChangeLog
     * @return 结果
     */
    public int updateTFlightChangeLog(TFlightChangeLog tFlightChangeLog);

    /**
     * 批量删除
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
//    public int deleteTFlightChangeLogByIds(String ids);

    /**
     * 删除
     *
     * @param id ID
     * @return 结果
     */
    public int deleteTFlightChangeLogById(Long id);
}
