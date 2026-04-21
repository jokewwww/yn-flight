package com.zh.dao.mapper.my;

import com.zh.bean.flight.TFlightChangeLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper接口
 *
 * @author ruoyi
 * @date 2020-06-09
 */
@Mapper
public interface TFlightChangeLogMapper {
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
     * 删除
     *
     * @param id ID
     * @return 结果
     */
    public int deleteTFlightChangeLogById(Long id);

    /**
     * 批量删除
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTFlightChangeLogByIds(String[] ids);

    List<TFlightChangeLog> findChangeLog(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("flgtFlno") String flgtFlno);

}
