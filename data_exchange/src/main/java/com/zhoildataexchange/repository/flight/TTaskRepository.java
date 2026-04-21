package com.zhoildataexchange.repository.flight;

import com.zhoildataexchange.entity.flight.TTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/2/18 10:28
 * @Description:
 */
@Transactional
public interface TTaskRepository extends JpaRepository<TTask, Serializable>, JpaSpecificationExecutor<TTask> {


    @Modifying
    @Query(value = "update T_TASK set task_flight_no =?1 where task_flight_id = ?2", nativeQuery = true)
    void updateRRask(String taskFlightNo, String taskFlightId);


    @Modifying
    @Query(value = "update T_TASK set task_status =?1 where task_flight_id = ?2", nativeQuery = true)
    void updateTaskStuts(Integer taskStuts, String taskFlightId);

    List<TTask> findByTaskFlightId(String taskFlightid);
}
