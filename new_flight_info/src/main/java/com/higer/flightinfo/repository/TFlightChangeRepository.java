package com.higer.flightinfo.repository;

import com.higer.flightinfo.entity.TFlightChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TFlightChangeRepository extends JpaRepository<TFlightChange,Long> {
}
