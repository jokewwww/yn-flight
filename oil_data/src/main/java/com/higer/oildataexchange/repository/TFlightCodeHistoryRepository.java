package com.higer.oildataexchange.repository;

import com.higer.oildataexchange.entity.flight.TFlightCodeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TFlightCodeHistoryRepository extends JpaRepository<TFlightCodeHistory, String>, JpaSpecificationExecutor<TFlightCodeHistory> {

}