package com.zh.service.impl;


import com.zh.bean.flight.TFlightTrouble;
import com.zh.dao.mapper.my.TFlightTroubleMapper;
import com.zh.service.FlightTroubleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FlightTroubleServiceImpl implements FlightTroubleService {

    private final static Logger log = LoggerFactory.getLogger(FlightTroubleServiceImpl.class);
    @Autowired
    private TFlightTroubleMapper tFlightTroubleMapper;

    @Override
    public int deleteByPrimaryKey(String id) {
        return tFlightTroubleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(TFlightTrouble record) {
        String troubleId = UUID.randomUUID().toString();
        record.setId(troubleId);
        return tFlightTroubleMapper.insert(record);
    }

    @Override
    public TFlightTrouble insertSelective(TFlightTrouble record) {
        String troubleId = UUID.randomUUID().toString();
        record.setId(troubleId);
        tFlightTroubleMapper.insertSelective(record);
        return record;
    }

    @Override
    public TFlightTrouble selectByPrimaryKey(String id) {
        return tFlightTroubleMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TFlightTrouble> selectAll(TFlightTrouble tFlightTrouble) {
        return tFlightTroubleMapper.selectAll(tFlightTrouble);
    }

    @Override
    public TFlightTrouble updateByPrimaryKeySelective(TFlightTrouble record) {
        int i = tFlightTroubleMapper.updateByPrimaryKeySelective(record);
        return record;
    }

    @Override
    public int updateByPrimaryKey(TFlightTrouble record) {
        return tFlightTroubleMapper.updateByPrimaryKey(record);
    }
}