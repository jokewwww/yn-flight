package com.higer.oildataexchange.repository;

import com.higer.oildataexchange.entity.flight.TFlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TFlightRepository extends JpaRepository<TFlight, String>, JpaSpecificationExecutor<TFlight> {

    @Query(value = "select * from T_FLIGHT where flgt_flno=?1 and flgt_flop=?2 and flgt_adid=?3 ", nativeQuery = true)
    List<TFlight> findFlgtInfo(String flno, String flop, String adid);

    @Query(value = "select * from T_FLIGHT where flgt_id=(select task_flight_id from T_TASK where task_id=?)", nativeQuery = true)
    TFlight findFlightByTaskId(String taskId);
}