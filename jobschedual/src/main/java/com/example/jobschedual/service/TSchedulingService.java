package com.example.jobschedual.service;

import com.example.jobschedual.dao.TSchedulingRepository;
import com.example.jobschedual.dao.TStaffRepository;
import com.example.jobschedual.entity.TScheduling;
import com.example.jobschedual.entity.TStaff;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class TSchedulingService {

    @Autowired
    private TSchedulingRepository repository;

    @Autowired
    private TStaffRepository tStaffRepository;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void modify(String groupId, List<TScheduling> list){

        repository.deleteAllByGroupId(groupId);
        repository.save(list);
    }

}
