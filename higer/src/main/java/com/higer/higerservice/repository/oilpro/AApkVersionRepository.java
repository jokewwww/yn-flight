package com.higer.higerservice.repository.oilpro;

import com.higer.higerservice.entity.oilpro.AApkVersion;
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
 * @Date: 2018/11/29 10:50
 * @Description:
 */
@Transactional(rollbackFor = Exception.class)
public interface AApkVersionRepository extends JpaRepository<AApkVersion, Serializable>, JpaSpecificationExecutor<AApkVersion> {
    @Query(value = "select * from a_apk_version  where  ver = ?1  and type = ?2", nativeQuery = true)
    AApkVersion findByVer_Type(String ver, Integer type);

    @Query(value = "select * from a_apk_version where id=(select max(id) from a_apk_version where type= ?1 )", nativeQuery = true)
    public AApkVersion getMaxId(Integer type);

    void deleteByType(Integer type);

    AApkVersion findByVerAndType(String ver, Integer type);

    Page<AApkVersion> findByTypeNot(Integer type, Pageable pageable);


    @Query(value = "select A from AApkVersion A  where  A.ver = :ver  and A.type = :type")
    AApkVersion findByVerAndType2(@Param("ver") String ver, @Param("type") Integer type);


    List<AApkVersion> findByType(int type);

}
