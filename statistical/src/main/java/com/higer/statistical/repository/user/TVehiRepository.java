package com.higer.statistical.repository.user;

import com.higer.statistical.entity.flight.TFuelRecpt;
import com.higer.statistical.entity.user.TStaff;
import com.higer.statistical.entity.user.TVehi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/1/24 15:35
 * @Description:
 */
@Transactional(rollbackFor = Exception.class)
public interface TVehiRepository extends JpaRepository<TVehi, Serializable>, JpaSpecificationExecutor<TVehi> {

    TVehi findByVehiPlateNo(String vehiPlateNo);
}
