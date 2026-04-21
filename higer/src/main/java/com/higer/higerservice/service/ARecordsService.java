package com.higer.higerservice.service;

import com.higer.higerservice.entity.oilpro.ARecords;
import com.higer.higerservice.repository.oilpro.ARecordsRepository;
import com.higer.higerservice.util.ModelAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/10/25 13:46
 * @Description:
 */
@Service
public class ARecordsService {

    @Autowired
    private ARecordsRepository aRecordsRepository;
    @PersistenceContext
    private EntityManager em;


    public AtomicBoolean save(ARecords aRecords) {
        try {
            ARecords save = aRecordsRepository.saveAndFlush(aRecords);
            em.clear();
            return new AtomicBoolean(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AtomicBoolean(false);
    }

    /**
     * @Description: state 状态 insertDate 格式 y-m-d 两个参数可任选其一 或者 都传
     * @Param: [state, insertDate]
     * @return: java.util.List<com.higer.higerservice.entity.oilpro.ARecords>
     * @Author: XiuHongXin
     * @Date: 2018/10/25
     */
    public List<ARecords> getAll(Integer state, String insertDate) {
        List<ARecords> byState = null;
        try {
            if (state == null && StringUtils.isEmpty(insertDate)) {
                return null;
            }
            if (state != null && StringUtils.isEmpty(insertDate)) {
                byState = aRecordsRepository.findByState(state);
            }
            if (state == null && !StringUtils.isEmpty(insertDate)) {
                byState = aRecordsRepository.findByInsertDate(insertDate);
            }
            if (state != null && !StringUtils.isEmpty(insertDate)) {
                byState = aRecordsRepository.findByInsertDateAndState(insertDate, state);
            }
            return byState;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public AtomicBoolean update(Integer id, ARecords aRecords) {
        try {
            if (id == null) {
                return new AtomicBoolean(true);
            }
            ARecords one = aRecordsRepository.findOne(id);
            ModelAssistant.copyProperties(aRecords, one);
            ARecords save = aRecordsRepository.save(one);
            em.clear();
            return new AtomicBoolean(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AtomicBoolean(false);
    }

    public AtomicBoolean delete(Integer id) {
        try {
            if (id == null) {
                return new AtomicBoolean(true);
            }
            aRecordsRepository.delete(aRecordsRepository.findOne(id));
            em.clear();
            return new AtomicBoolean(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AtomicBoolean(false);
    }


    public Page<ARecords> findByFuncAndState(String kafka, int i, PageRequest pageRequest) {
        try {
            return aRecordsRepository.findByFuncAndState(kafka, i, pageRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
