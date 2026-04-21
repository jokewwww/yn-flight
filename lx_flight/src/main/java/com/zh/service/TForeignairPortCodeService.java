package com.zh.service;

import com.zh.bean.flight.TForeignairportCode;

import java.util.List;

public interface TForeignairPortCodeService {

    Integer insert(TForeignairportCode foreignairportCode);

    void update(TForeignairportCode foreignairportCode);

    void delete(TForeignairportCode foreignairportCode);

    List<TForeignairportCode> selectList();

    TForeignairportCode selectByCode(TForeignairportCode foreignairportCode);

}
