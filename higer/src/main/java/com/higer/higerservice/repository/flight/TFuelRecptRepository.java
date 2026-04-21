package com.higer.higerservice.repository.flight;

import com.higer.higerservice.entity.flight.TFuelRecpt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/1/24 15:35
 * @Description:
 */
@Transactional(rollbackFor = Exception.class)
public interface TFuelRecptRepository extends JpaRepository<TFuelRecpt, Serializable>, JpaSpecificationExecutor<TFuelRecpt> {
    List<TFuelRecpt> findByFlrcAirlCode(String flrcAirlCode);

    List<TFuelRecpt> findByFlrcAirlName(String flrcAirlName);

    List<TFuelRecpt> findByFlrcRevwStatusAndFlrcAirportCodeAndFlrcDate(int flrcRevwStatus, String code, Date date);

    List<TFuelRecpt> findByFlrcNo(String flrcNo);
}
