package com.higer.oildataexchange.repository;

import com.higer.oildataexchange.entity.flight.TStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TStaffRepository extends JpaRepository<TStaff, String>, JpaSpecificationExecutor<TStaff> {
    List<TStaff> findByStaffName(String name);
}