package com.zhoildataexchange.repository.flight;

import com.zhoildataexchange.entity.flight.TFlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/1/24 15:35
 * @Description:
 */
@Transactional
public interface TFlightRepository extends JpaRepository<TFlight, Serializable>, JpaSpecificationExecutor<TFlight> {

    @Modifying
    @Query(value = "update T_FLIGHT set flgt_num = ?1 where flgt_id = ?2", nativeQuery = true)
    int updateFlightNum(Integer num, String id);

    TFlight findByFlgtFlnoAndFlgtFlopAndFlgtLinkRepeatAndFlgtRepeatAndFlgtAirportCodeAndFlgtAdid(String flgtFlno, Date flgtFlop, Integer flgtLinkRepeat, Integer flgtRepeat, String airportCode, String flgtAdid);

    @Query(value = "select  t from TFlight t where t.flgtFlno like %?1%  and t.flgtFlop= ?2 and t.flgtAirportCode = ?3 and t.flgtAdid = ?4")
    TFlight findByFlgtFlnoLikeAndFlgtFlopAndFlgtAirportCodeAndFlgtAdid(String flgtFlno, Date flgtFlop, String airportCode,String flgtAdid);


    TFlight findByFlgtFfid(String flgtFfid);


}
