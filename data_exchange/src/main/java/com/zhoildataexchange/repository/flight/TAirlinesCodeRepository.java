package com.zhoildataexchange.repository.flight;

import com.zhoildataexchange.entity.flight.TAirlinesCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/2/15 09:54
 * @Description:
 */
@Transactional
public interface TAirlinesCodeRepository extends JpaRepository<TAirlinesCode, Serializable>, JpaSpecificationExecutor<TAirlinesCode> {



}
