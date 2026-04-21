package com.higer.statistical.repository.flight;

import com.higer.statistical.entity.flight.TFlightCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

public interface TFlightCodeRepository extends JpaRepository<TFlightCode, Serializable>, JpaSpecificationExecutor<TFlightCode> {

    @Query(value = "select * from T_FLIGHT_CODE where arcr_regn=?1 AND (flno is null or flno='') AND del_flag=0 AND CURDATE() BETWEEN arcr_start_date AND arcr_end_date order by arcr_start_date desc  limit 1 ",nativeQuery = true)
    List<TFlightCode> selectByArcrRegnsAndTime(String arcrRegn);

    @Query(value = "select * from T_FLIGHT_CODE where arcr_regn=?1 AND flno=?2  AND del_flag=0 AND CURDATE() BETWEEN arcr_start_date AND arcr_end_date order by arcr_start_date desc  limit 1 ",nativeQuery = true)
    List<TFlightCode> selectByArcrRegnsFlnoAndTime(String arcrRegn,String flno);
}
