package com.higer.oildataexchange.repository;

import com.higer.oildataexchange.entity.flight.TAirportCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TAirportCodeRepository extends JpaRepository<TAirportCode, String>, JpaSpecificationExecutor<TAirportCode> {

    Optional<TAirportCode> findByApcdAirportName(String apcdAirportName);

    TAirportCode findByApcdCnafAirportCode(String apcdCnafAirportCode);

    TAirportCode findByApcdIataCode(String apcdIataCode);

}