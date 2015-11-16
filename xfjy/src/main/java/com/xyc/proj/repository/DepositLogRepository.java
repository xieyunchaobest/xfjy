package com.xyc.proj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyc.proj.entity.DepositLog;



public interface DepositLogRepository extends JpaRepository<DepositLog, Long> {
}
