package com.higer.higerservice.repository.oilpro;

import com.higer.higerservice.entity.oilpro.AGpsRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/11/20 15:36
 * @Description:
 */
@Transactional(rollbackFor = Exception.class)
public interface AGpsRecordsRepository extends JpaRepository<AGpsRecords, Serializable>, JpaSpecificationExecutor<AGpsRecords> {


}
