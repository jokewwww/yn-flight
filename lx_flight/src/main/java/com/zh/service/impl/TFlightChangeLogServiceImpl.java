package com.zh.service.impl;

import com.zh.bean.flight.TFlightChangeLog;
import com.zh.dao.mapper.my.TFlightChangeLogMapper;
import com.zh.service.ITFlightChangeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-09
 */
@Service
public class TFlightChangeLogServiceImpl implements ITFlightChangeLogService {
    @Autowired
    private TFlightChangeLogMapper tFlightChangeLogMapper;

    /**
     * 查询
     *
     * @param id ID
     * @return
     */
    @Override
    public TFlightChangeLog selectTFlightChangeLogById(Long id) {
        return tFlightChangeLogMapper.selectTFlightChangeLogById(id);
    }

    /**
     * 查询
     *
     * @param tFlightChangeLog
     * @return
     */
    @Override
    public List<TFlightChangeLog> selectTFlightChangeLogList(TFlightChangeLog tFlightChangeLog) {
        String[] times = tFlightChangeLog.getDataTime().split(",");
        String startTime = times[0] + " 00:00:00";
        String endTime = times[1] + " 23:59:59";
        List<TFlightChangeLog> fff = tFlightChangeLogMapper.findChangeLog(startTime, endTime, tFlightChangeLog.getFlgtFlno());
        return fff;
    }

    /**
     * 新增
     *
     * @param tFlightChangeLog
     * @return 结果
     */
    @Override
    public int insertTFlightChangeLog(TFlightChangeLog tFlightChangeLog) {
        tFlightChangeLog.setCreateTime(new Date());
        return tFlightChangeLogMapper.insertTFlightChangeLog(tFlightChangeLog);
    }

    /**
     * 修改
     *
     * @param tFlightChangeLog
     * @return 结果
     */
    @Override
    public int updateTFlightChangeLog(TFlightChangeLog tFlightChangeLog) {
        return tFlightChangeLogMapper.updateTFlightChangeLog(tFlightChangeLog);
    }

    /**
     * 删除
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
//    @Override
//    public int deleteTFlightChangeLogByIds(String ids)
//    {
//        return tFlightChangeLogMapper.deleteTFlightChangeLogByIds(Convert.toStrArray(ids));
//    }

    /**
     * 删除
     *
     * @param id ID
     * @return 结果
     */
    @Override
    public int deleteTFlightChangeLogById(Long id) {
        return tFlightChangeLogMapper.deleteTFlightChangeLogById(id);
    }
}
