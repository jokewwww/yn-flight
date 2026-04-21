package com.higer.read_kafka.repository.flight;

import com.higer.read_kafka.entity.flight.TFlight;
import com.higer.read_kafka.entity.flight.TForeignairportCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/10/6 12:28
 * @Description:
 */
@Transactional
public interface TForeignairportCodeRepository  extends JpaRepository<TForeignairportCode, Serializable>, JpaSpecificationExecutor<TForeignairportCode> {

    TForeignairportCode findByAlcdIcaoCode(String alcdIcaoCode);

}
