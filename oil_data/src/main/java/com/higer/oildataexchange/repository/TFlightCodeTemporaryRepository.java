package com.higer.oildataexchange.repository;

import com.higer.oildataexchange.entity.flight.TFlightCodeTemporary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface TFlightCodeTemporaryRepository extends JpaRepository<TFlightCodeTemporary, String>, JpaSpecificationExecutor<TFlightCodeTemporary> {
    @Query(value = "select * from T_FLIGHT_CODE_TEMPORARY where arcr_regn=?1 and\n" +
            "now() BETWEEN arcr_start_date and arcr_end_date", nativeQuery = true)
    List<TFlightCodeTemporary> findByArcrRegnValid(String regn);

    TFlightCodeTemporary findByArcrRegnAndFlno(String arcrRegn, String flno);

    @Transactional
    @Modifying
    @Query(value =
            "INSERT INTO T_FLIGHT_CODE_TEMPORARY(arcr_regn, arcr_acname, arcr_custom_num, arcr_start_date, arcr_end_date, flno) VALUES " +
                    "(?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    int saveFlightCode(String arrcrRegn, String arcrAcName, String arcrCustomNum, Date start, Date end, String flno);

    @Transactional
    @Modifying
    @Query(value =
            "UPDATE T_FLIGHT_CODE_TEMPORARY SET arcr_acname = ?1, arcr_custom_num = ?2, arcr_end_date = ?3, flno = ?4 " +
                    "WHERE arcr_regn = ?5 AND arcr_start_date = ?6", nativeQuery = true)
    int updFlightCode(String arcrAcName, String arcrCustomNum, Date end, String flno, String arrcrRegn, Date start);

    TFlightCodeTemporary findByArcrRegn(String arcrRegn);


    @Query(value = "select * from T_FLIGHT_CODE_TEMPORARY where arcr_regn=?1 and\n" +
            "flno is null", nativeQuery = true)
    TFlightCodeTemporary findByArcrRegnAndFlno(String regn);
}