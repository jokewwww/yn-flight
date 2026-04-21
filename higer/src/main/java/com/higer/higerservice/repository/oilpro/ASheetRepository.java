package com.higer.higerservice.repository.oilpro;

import com.higer.higerservice.entity.oilpro.ASheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/11/2 10:21
 * @Description:
 */
@Transactional(rollbackFor = Exception.class)
public interface ASheetRepository extends JpaRepository<ASheet, Serializable>, JpaSpecificationExecutor<ASheet> {
}
