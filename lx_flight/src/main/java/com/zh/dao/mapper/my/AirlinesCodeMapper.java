package com.zh.dao.mapper.my;

import com.zh.bean.flight.MyAirlinesCode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface AirlinesCodeMapper {

    int insertAirlinesCode(MyAirlinesCode airlinesCode);

    int updateAirlinesCode(MyAirlinesCode airlinesCode);

    int deleteAirlinesCode(@Param("alcdIcaoCode") String alcdIcaoCode);

    List<MyAirlinesCode> selectAirlinesCode();

    MyAirlinesCode selectAirlinesCodeFind(@Param("alcdIcaoCode") String alcdIcaoCode);


}
