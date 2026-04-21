package com.higer.read_kafka.repository.user;

import com.higer.read_kafka.entity.user.TStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/1/24 15:35
 * @Description:
 */
@Transactional
public interface TStaffRepository extends JpaRepository<TStaff, Serializable>, JpaSpecificationExecutor<TStaff> {

    TStaff findByStaffId(String staffId);

    List<TStaff> findByStaffAirportCodeAndStaffAptareaCode(String staffAirportCode, String staffAptareaCode);
    List<TStaff> findByStaffAirportCode(String staffAirportCode);
}
