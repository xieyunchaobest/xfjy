package com.xyc.proj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyc.proj.entity.DepositSummary;



public interface DepositSummaryRepository extends JpaRepository<DepositSummary, Long> {
	DepositSummary findByOpenId(String openId);
}
