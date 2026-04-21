package com.higer.higerservice.repository.flight;

import com.higer.higerservice.entity.flight.TFlightCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

public interface TFlightCodeRepository extends JpaRepository<TFlightCode, Serializable>, JpaSpecificationExecutor<TFlightCode> {

    @Query(name = "select * from T_FLIGHT_CODE where arcr_regn=:arcrRegn order by arcr_start_date desc  limit 1 ", nativeQuery = true)
    TFlightCode findByArcrRegn(@Param("arcrRegn") String arcrRegn);
}
