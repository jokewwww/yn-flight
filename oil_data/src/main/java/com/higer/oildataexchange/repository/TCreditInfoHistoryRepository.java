package com.higer.oildataexchange.repository;

import com.higer.oildataexchange.entity.credit.TCreditInfoHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TCreditInfoHistoryRepository extends JpaRepository<TCreditInfoHistory, String>, JpaSpecificationExecutor<TCreditInfoHistory> {

}