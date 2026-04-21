package com.higer.read_kafka.repository.flight;

import com.higer.read_kafka.entity.flight.TCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface TCustomRepository extends JpaRepository<TCustom, Serializable>, JpaSpecificationExecutor<TCustom> {

    TCustom findByCstmNum(String cstmNum);
}
