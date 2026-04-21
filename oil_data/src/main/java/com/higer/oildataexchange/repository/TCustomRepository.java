package com.higer.oildataexchange.repository;

import com.higer.oildataexchange.entity.TCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TCustomRepository extends JpaRepository<TCustom, String>, JpaSpecificationExecutor<TCustom> {

    @Query(value = "select * from T_CUSTOM where cstm_num=?1", nativeQuery = true)
    TCustom findInfo(String cstmNum);

    @Query(value = "select w.* from T_FLIGHT_CODE q,T_CUSTOM w " +
            "where q.arcr_custom_num = w.cstm_num " +
            "and q.arcr_regn=?1 and now() BETWEEN arcr_start_date and arcr_end_date ", nativeQuery = true)
    List<TCustom> findInfoByName(String regn);

    @Query(value = "select * from T_CUSTOM where cstm_name=?1 order by cstm_num desc limit 1", nativeQuery = true)
    TCustom findByCstmName(String cstmNum);


    @Query(value = "select * from T_CUSTOM  where cstm_num like %?1% limit 1", nativeQuery = true)
    TCustom findInfoLikeNum(String cstmNum);

}