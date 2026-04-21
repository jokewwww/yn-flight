package com.higer.oildataexchange.repository;

import com.higer.oildataexchange.entity.oil.ExcelOneOIL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ExcelOil1repository extends JpaRepository<ExcelOneOIL, String>, JpaSpecificationExecutor<ExcelOneOIL> {

    @Query(value = "select * from excel_oil1 where upload_status = ?1  and ydhm not like '%-D%'", nativeQuery = true)
    List<ExcelOneOIL> findByUploadStatus(String status);


    @Query(value = "update excel_oil1 set  upload_status=?1,upload_fail_reason=?2,fail_xml=?3 where ydhm=?4 ", nativeQuery = true)
    @Modifying
    @Transactional
    int updStatus(String status, String reason, String xml, String oil);


    @Query(value = "update excel_oil1 set  upload_status=?1,upload_fail_reason=?2,fail_xml=?3 where ydhm=?4 and date=?5", nativeQuery = true)
    @Modifying
    @Transactional
    int updStatusDate(String status, String reason, String xml, String oil, String date);
}