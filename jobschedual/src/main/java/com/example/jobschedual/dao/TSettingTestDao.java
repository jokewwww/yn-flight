package com.example.jobschedual.dao;

import com.example.jobschedual.entity.TSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/4/24 19:31
 * @Description:
 */
@Transactional(rollbackFor = Exception.class)
public interface TSettingTestDao extends JpaRepository<TSetting, Serializable>, JpaSpecificationExecutor<TSetting> {
}
