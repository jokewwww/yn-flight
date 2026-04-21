package com.higer.flightinfo.repository;

import com.higer.flightinfo.entity.TFlightData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TFlightDataRepository extends JpaRepository<TFlightData,String> {
}
