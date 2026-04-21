package com.higer.higerservice.repository.flight;

import com.higer.higerservice.entity.flight.TAirlinesCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/4/12 17:04
 * @Description:
 */
@Transactional(rollbackFor = Exception.class)
public interface TAirlinesCodeRepository extends JpaRepository<TAirlinesCode, Serializable>, JpaSpecificationExecutor<TAirlinesCode> {

    TAirlinesCode findByAlcdArlnName(String alcdArlnName);

}
