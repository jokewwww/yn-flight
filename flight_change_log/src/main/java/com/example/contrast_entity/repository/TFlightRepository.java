package com.example.contrast_entity.repository;

import com.example.contrast_entity.entity.TFlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/3/8 16:37
 * @Description:
 */
@Transactional(rollbackFor = Exception.class)
public interface TFlightRepository extends JpaRepository<TFlight, Serializable>, JpaSpecificationExecutor<TFlight> {

    TFlight findByFlgtFfid(String flgtFfid);

    @Query(value = "select a.* from T_FLIGHT a where a.flgt_flno = ?1 and a.flgt_flop = ?2 and a.flgt_repeat = ?3 and a.flgt_link_repeat = ?4", nativeQuery = true)
    TFlight findTflightChangeLog(String flgtFlno, Date flgtLinkFlop, Integer flgtLinkRepeat, Integer flgtRepeat);


    @Query(value = "select * from T_FLIGHT where flgt_flop = CURDATE() and flgt_placecode_status = 1",nativeQuery = true)
    List<TFlight> findAlls();

    @Modifying
    @Query(value = "update  T_FLIGHT set flgt_placecode_status = ?1 , flgt_regn_status =?2 ,  flgt_regn = ?3 ,flgt_placecode = ?4 where flgt_ffid = ?5",nativeQuery = true)
    int update(Integer a , Integer b ,String flgtRegn, String flgtPlacecode ,String ffid);


}
