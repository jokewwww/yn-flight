package com.higer.oildataexchange.repository;

import com.higer.oildataexchange.entity.airUnit.TAirlinesCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TAirLinesCodeRepository extends JpaRepository<TAirlinesCode, String>, JpaSpecificationExecutor<TAirlinesCode> {
    TAirlinesCode findByAlcdIcaoCode(String alcdIcaoCode);

}