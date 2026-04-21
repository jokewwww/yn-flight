package com.example.contrast_entity.repository;

import com.example.contrast_entity.entity.TFlightChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/3/8 13:48
 * @Description:
 */
@Transactional(rollbackFor = Exception.class)
public interface TFlightChangeLogRepository extends JpaRepository<TFlightChangeLog, Serializable>, JpaSpecificationExecutor<TFlightChangeLog> {

    List<TFlightChangeLog> findByFfid(String ffid);
}
