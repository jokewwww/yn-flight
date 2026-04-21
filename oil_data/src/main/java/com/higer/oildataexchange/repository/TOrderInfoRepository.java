package com.higer.oildataexchange.repository;

import com.higer.oildataexchange.entity.orderInfo.TOrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TOrderInfoRepository extends JpaRepository<TOrderInfo, String>, JpaSpecificationExecutor<TOrderInfo> {
    TOrderInfo findByOrderNo(String orderNo);

    TOrderInfo findByOrderId(Long id);
}