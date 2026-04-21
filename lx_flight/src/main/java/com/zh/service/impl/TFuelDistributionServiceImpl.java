package com.zh.service.impl;

import com.zh.bean.flight.TFuelDistribution;
import com.zh.bean.flight.TFuelNo;
import com.zh.constant.Constant;
import com.zh.dao.mapper.my.TFuelDistributionMapper;
import com.zh.service.FuelRecptService;
import com.zh.service.TFuelDistributionService;
import com.zh.util.SendMsg2Redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/9/13 11:26
 * @Description:
 */
@Service
public class TFuelDistributionServiceImpl implements TFuelDistributionService {

    @Autowired
    private TFuelDistributionMapper tFuelDistributionMapper;

    @Autowired
    private FuelRecptService fuelRecptService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<TFuelDistribution> selectList(TFuelDistribution tFuelDistribution) {
        return tFuelDistributionMapper.selectList(tFuelDistribution);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateList(List<TFuelNo> record) {
        Map<String, List<TFuelNo>> map = record.stream().collect(Collectors.groupingBy(TFuelNo::getStaffId));
        map.entrySet().forEach(entry -> {
            SendMsg2Redis.testDingYue(stringRedisTemplate, entry.getKey(), Constant.STAFF, Constant.PC_RECYCLE_FUEL, entry.getValue());
        });
        return fuelRecptService.recycleFuelPc(record);
    }
}
