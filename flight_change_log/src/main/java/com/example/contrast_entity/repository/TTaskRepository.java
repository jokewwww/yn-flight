package com.example.contrast_entity.repository;

import com.example.contrast_entity.entity.TTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/2/18 10:28
 * @Description:
 */
@Transactional(rollbackFor = Exception.class)
public interface TTaskRepository extends JpaRepository<TTask, Serializable>, JpaSpecificationExecutor<TTask> {


    List<TTask> findByTaskFlightId(String taskFlightId);
}
