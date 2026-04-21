package com.higer.higerservice.repository.oilpro;

import com.higer.higerservice.entity.oilpro.ACarInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/12/3 08:25
 * @Description:
 */
@Transactional(rollbackFor = Exception.class)
public interface ACarInfoRepository extends JpaRepository<ACarInfo, Serializable>, JpaSpecificationExecutor<ACarInfo> {

    ACarInfo findByHpAndAirport(String hp, String airport);


}
