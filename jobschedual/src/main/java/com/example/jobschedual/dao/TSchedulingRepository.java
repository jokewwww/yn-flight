package com.example.jobschedual.dao;

import com.example.jobschedual.entity.TScheduling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Transactional(rollbackFor = Exception.class)
public interface TSchedulingRepository extends JpaRepository<TScheduling, Serializable> {

    void deleteAllByGroupId(String groupId);

    List<TScheduling> findAllByGroupIdOrderById(String groupId);
}

