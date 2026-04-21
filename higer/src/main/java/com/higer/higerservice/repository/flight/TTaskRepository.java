package com.higer.higerservice.repository.flight;

import com.higer.higerservice.entity.flight.TTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface TTaskRepository extends JpaRepository<TTask, Serializable>, JpaSpecificationExecutor<TTask> {
}
