package com.higer.higerservice.repository.flight;

import com.higer.higerservice.entity.flight.TAirportCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Transactional(rollbackFor = Exception.class)
public interface TAirportCodeRepository extends JpaRepository<TAirportCode, Serializable>, JpaSpecificationExecutor<TAirportCode> {

    TAirportCode findOneByApcdCnafAirportCode(String apcdCnafAirportCode);
}
