package com.zh.service;

import com.zh.bean.flight.MyAirlinesCode;

import java.util.List;

public interface AirlinesCodeService {

    Integer insertAirlinesCode(MyAirlinesCode airlinesCode);

    void updateAirlinesCode(MyAirlinesCode airlinesCode);

    void deleteAirlinesCode(MyAirlinesCode airlinesCode);

    List<MyAirlinesCode> selectAirlinesCode();

    MyAirlinesCode selectAirlinesCodeFind(MyAirlinesCode airlinesCode);

}
