package com.ncse.zhhygis.service;


import java.io.IOException;
import java.util.List;

public interface CarGisDataService {
    /**
     * 汽车gis数据处理
     *
     * @param gisdata  gis数据
     * @param datatype 数据类型，real 最新数据，noReal 历史数据
     * @return
     */
    String carGisDataHandle(String gisdata, String datatype) throws IOException;

    /**
     * 离线数据处理
     *
     * @param aircodeList
     */
    void offlinecheck(List<String> aircodeList);
}
