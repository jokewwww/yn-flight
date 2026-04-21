package com.higer.read_kafka.repository.flight;

import com.higer.read_kafka.entity.flight.TFlight;
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
 * @Date: 2019/1/24 15:35
 * @Description:
 */
@Transactional
public interface TFlightRepository extends JpaRepository<TFlight, Serializable>, JpaSpecificationExecutor<TFlight> {

    @Modifying
    @Query(value = "update T_FLIGHT set flgt_num = ?1 where flgt_id = ?2",nativeQuery = true)
    int updateFlightNum(Integer num, String id);

    @Query(value = "select * from T_FLIGHT where flgt_flop = ?1 and flgt_adid = ?2",nativeQuery = true)
    List<TFlight> findFlgtFlop(String flgtFlop,String flgtAdid);

    TFlight findByFlgtFlnoAndFlgtFlopAndFlgtLinkRepeatAndFlgtRepeatAndFlgtAirportCodeAndFlgtAdid(String flgtFlno, Date flgtFlop, Integer flgtLinkRepeat, Integer flgtRepeat,String airportCode,String flgtAdid);

    TFlight findByFlgtFlnoAndFlgtFlopAndFlgtLinkRepeatAndFlgtRepeatAndFlgtAirportCode(String flgtFlno, Date flgtFlop, Integer flgtLinkRepeat, Integer flgtRepeat, String airportCode);

    List<TFlight> findByFlgtFlnoAndFlgtFlopAndFlgtLinkRepeatAndFlgtRepeatAndFlgtAirportCodeAndFlgtAdidAndFlgtLinkFlno(String flgtFlno, Date flgtFlop, Integer flgtLinkRepeat, Integer flgtRepeat,String airportCode,String flgtAdid,String flgtLinkFlno);

    TFlight findByFlgtFfid(String flgtFfid);

    TFlight findByFlgtLinkFfid(String flgtLinkFfid);

    TFlight findByFlgtFlopAndFlgtNum(Date flgtFlop , Integer flgtNum);

    @Modifying
    @Query(value = "update T_FLIGHT set flgt_num = null where flgt_flop = CURDATE() and flgt_airport_code = 2901 and flgt_adid = \"D\"",nativeQuery = true)
    int updateFlightNumNull();

    @Modifying
    @Query(value = "select * from  T_FLIGHT  where flgt_flop = CURDATE() and flgt_airport_code = 2901 and flgt_adid = \"D\"",nativeQuery = true)
    List<TFlight> selectNumNull();

    @Query(value = "select * from T_FLIGHT where flgt_flop = ?1 and flgt_adid = ?2 and flgt_airport_code = ?3 and flgt_d_atot is not null",nativeQuery = true)
    List<TFlight> findByflgtFlopAndFlgtAdidAndFlgtAirportCode(Date flgtFlop , String flgtAdid , String airportCode);


    @Modifying
    @Query(value = "update T_FLIGHT set flgt_flop = ?1 where flgt_id = ?2",nativeQuery = true)
    int updateFlightFlop(String flgtFlop , String id);

}
