package com.example.jobschedual.dao;

import com.example.jobschedual.entity.AJobschedual;
import com.example.jobschedual.entity.TStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/3/13 14:30
 * @Description:
 */
@Transactional(rollbackFor = Exception.class)
public interface AJobschedualRepository extends JpaRepository<AJobschedual, Serializable>, JpaSpecificationExecutor<AJobschedual> {
}
