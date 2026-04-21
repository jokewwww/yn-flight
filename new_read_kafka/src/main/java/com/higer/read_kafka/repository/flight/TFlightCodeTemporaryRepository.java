package com.higer.read_kafka.repository.flight;

import com.higer.read_kafka.entity.flight.TFlightCodeTemporary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

public interface TFlightCodeTemporaryRepository extends JpaRepository<TFlightCodeTemporary, Serializable>, JpaSpecificationExecutor<TFlightCodeTemporary> {

    @Query(value = "select * from T_FLIGHT_CODE_TEMPORARY where arcr_regn=?1 AND (flno is null or flno='') AND CURDATE() BETWEEN arcr_start_date AND arcr_end_date order by arcr_start_date desc  limit 1 ",nativeQuery = true)
    List<TFlightCodeTemporary> selectByArcrRegnsAndTime(String arcrRegn);

    @Query(value = "select * from T_FLIGHT_CODE_TEMPORARY where arcr_regn=?1 AND flno=?2 AND CURDATE() BETWEEN arcr_start_date AND arcr_end_date order by arcr_start_date desc  limit 1 ",nativeQuery = true)
    List<TFlightCodeTemporary> selectByArcrRegnsFlnoAndTime(String arcrRegn,String flno);
}
