package com.higer.oildataexchange.repository;

import com.higer.oildataexchange.entity.oil.TFuelRecpt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TFuelRecptRepository extends JpaRepository<TFuelRecpt, String>, JpaSpecificationExecutor<TFuelRecpt> {

    Optional<TFuelRecpt> findByFlrcNo(String flrcNo);

    TFuelRecpt findByFlrcId(String id);

    // TFuelRecpt findByTaskId(String taskId);

//    @Query(value = "select flrc_id id,'I' IUD,flrc_airport_code APC3,flrc_date STTOP,count(*) FS_NUM,SUM(CASE WHEN flrc_status = 2 THEN 1 ELSE 0 END) FS_NUM_UPL, SUM(flrc_figuars) OIL_NUM,SUM(CASE WHEN flrc_status = 2 THEN flrc_figuars ELSE 0 END) OIL_NUM_UPL,now() LSTU " +
//            "FROM T_FUEL_RECPT GROUP BY flrc_airport_code,flrc_date ORDER BY flrc_date desc ",nativeQuery = true)
//    List<STTSDINFO> statistics();

}