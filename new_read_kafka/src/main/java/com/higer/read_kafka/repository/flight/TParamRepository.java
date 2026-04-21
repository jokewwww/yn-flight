package com.higer.read_kafka.repository.flight;

import com.higer.read_kafka.entity.flight.TParamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/2/14 13:25
 * @Description:
 */
@Transactional
public interface TParamRepository extends JpaRepository<TParamEntity, Serializable>, JpaSpecificationExecutor<TParamEntity> {

    TParamEntity findByPName(String pName);
}
