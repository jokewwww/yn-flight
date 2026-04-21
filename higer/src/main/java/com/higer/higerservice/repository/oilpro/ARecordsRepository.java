package com.higer.higerservice.repository.oilpro;

import com.higer.higerservice.entity.oilpro.ARecords;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/10/25 13:45
 * @Description:
 */
@Transactional(rollbackFor = Exception.class)
public interface ARecordsRepository extends JpaRepository<ARecords, Serializable>, JpaSpecificationExecutor<ARecords> {

    List<ARecords> findByState(Integer state);

    List<ARecords> findByInsertDate(String insertDate);

    List<ARecords> findByInsertDateAndState(String insertDate, Integer state);


    @Query("select A from ARecords A where A.func !=:func and A.state = :state ORDER BY A.id")
// order by A.insertDate desc
    Page<ARecords> findByFuncAndState(@Param("func") String func, @Param("state") Integer state, Pageable pageable);


    @Query("select A from ARecords A where A.func =:func and A.state = :state ORDER BY A.id")
//order by A.insertDate desc 默认根据id
    Page<ARecords> findByDoPost(@Param("func") String func, @Param("state") Integer state, Pageable pageable);
}
