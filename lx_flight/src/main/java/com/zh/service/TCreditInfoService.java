package com.zh.service;

import com.zh.bean.flight.TCreditInfo;

import java.util.List;


public interface TCreditInfoService {


    List<TCreditInfo> selectCreditInfo(TCreditInfo creditInfo);

    /**
     * 新建
     *
     * @param creditInfo
     */
    Integer insertCreditInfo(TCreditInfo creditInfo);

    Integer updateCreditInfo(TCreditInfo creditInfo);

    void deleteCreditInfo(TCreditInfo creditInfo);

}
