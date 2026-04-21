package com.higer.flightinfo.repository;

import com.higer.flightinfo.entity.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MetaRepository extends JpaRepository<Meta,Long> {

    @Query(value = "select a.* from tflight_cache a where a.type = 'DELETE' and a.create_time > '2020-08-31'",nativeQuery = true)
    List<Meta> findbyTypea();

}
