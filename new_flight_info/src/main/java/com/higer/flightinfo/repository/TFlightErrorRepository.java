package com.higer.flightinfo.repository;

import com.higer.flightinfo.entity.TFlightError;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TFlightErrorRepository extends JpaRepository<TFlightError,Long> {
}
