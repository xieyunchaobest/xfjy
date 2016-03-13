package com.xyc.proj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyc.proj.entity.DepositLog;



public interface DepositLogRepository extends JpaRepository<DepositLog, Long> {
	DepositLog findByOutTradeNo(String outTradeNo);
	
	List findByOutTradeNoAndState(String outTradeNo,String state);
	
	List findByOutTradeNoOrderByCreatedTimeDesc(String outTradeNo);
	
}
