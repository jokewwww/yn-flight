package com.higer.oildataexchange.repository;

import com.higer.oildataexchange.entity.HttpSendLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HttpSendLogRepository extends JpaRepository<HttpSendLog, String> {
}
