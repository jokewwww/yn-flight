package com.higer.oildataexchange.repository;

import com.higer.oildataexchange.entity.flight.TFlightError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
public interface TFlightErrorRepository extends JpaRepository<TFlightError, Integer> {
}
