package com.higer.statistical.service;

import com.google.common.collect.Maps;
import com.higer.statistical.entity.flight.TTask;
import com.higer.statistical.repository.CountRepository;
import com.higer.statistical.repository.flight.TFuelRecptRepository;
import com.higer.statistical.repository.flight.TTaskRepository;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RandomService {

    @Autowired
    private CountRepository countRepository;

    @Autowired
    private TTaskRepository tTaskRepository;

    @Transactional
    public void test() {
        String sql = "SELECT  task_id    FROM  T_TASK  ta,T_FLIGHT  tf  WHERE  ta.task_flight_id  =  tf.flgt_id  AND  tf.flgt_flop  =  CURDATE()  ";
        Map<String, Object> param = Maps.newHashMap();
        List<Map<String, Object>> maps = countRepository.executeSqlForList(sql, param);
        maps.forEach(map -> {
            String taskId = Optional.ofNullable(map.get("task_id")).orElse("").toString();
            int a = RandomUtils.nextInt(6, 10);
            int b = RandomUtils.nextInt(1, a);
            TTask one = tTaskRepository.findOne(taskId);
            one.setTaskTakeoffFuel(a+b);
            one.setTaskChockFuel(b);
            tTaskRepository.save(one);
        });
    }
}

