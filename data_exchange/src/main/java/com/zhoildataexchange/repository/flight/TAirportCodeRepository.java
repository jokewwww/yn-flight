package com.zhoildataexchange.repository.flight;

import com.zhoildataexchange.entity.flight.TAirportCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/2/14 13:25
 * @Description:
 */
@Transactional
public interface TAirportCodeRepository extends JpaRepository<TAirportCode, Serializable>, JpaSpecificationExecutor<TAirportCode> {

    TAirportCode findByApcdIataCode(String apcdIataCode);

    @Query("select a from TAirportCode a where a.apcdIataCode in(:ids)")
    List<TAirportCode> findByIds(@Param("ids") List<String> ids);

}
