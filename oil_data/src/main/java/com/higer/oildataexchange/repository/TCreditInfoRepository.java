package com.higer.oildataexchange.repository;

import com.higer.oildataexchange.entity.credit.TCreditInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TCreditInfoRepository extends JpaRepository<TCreditInfo, String>, JpaSpecificationExecutor<TCreditInfo> {


    TCreditInfo findByCstno(String cstno);


}