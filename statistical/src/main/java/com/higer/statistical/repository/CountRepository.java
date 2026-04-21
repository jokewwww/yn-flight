package com.higer.statistical.repository;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class CountRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Map<String,Object>> executeSqlForList(String sql, Map<String,Object> paramMap){
        Query query = executeSql(sql, paramMap);
        return query.getResultList();
    }

    public Map<String,Object> executeForOne(String sql,Map<String,Object> paramMap){
        Query query = executeSql(sql, paramMap);
        return (Map<String, Object>) query.getSingleResult();
    }

    private Query executeSql(String sql, Map<String,Object> paramMap){
        log.info(sql);
        log.info(JSONObject.toJSONString(paramMap));
        Query query = entityManager.createNativeQuery(sql);
        paramMap.forEach(query::setParameter);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query;
    }
}
