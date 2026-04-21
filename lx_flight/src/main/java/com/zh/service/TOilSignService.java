package com.zh.service;


import com.zh.bean.flight.TOilSign;

import java.util.List;

public interface TOilSignService {


    List<TOilSign> select();

    /**
     * 新建
     *
     * @param oilSign
     */
    Integer insertOilSign(TOilSign oilSign);

    Integer updateOilSign(TOilSign oilSign);

    void deleteOilSign(TOilSign oilSign);

    TOilSign selectOilSign(TOilSign oilSign);

    TOilSign selectByFlgtId(TOilSign oilSign);
}
