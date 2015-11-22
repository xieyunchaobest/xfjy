package com.xyc.proj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyc.proj.entity.Order;



public interface OrderRepository extends JpaRepository<Order, Long> {
	
	List findByOpenId(String openId);
	
	List findById(Long id);
	
	Order findByOutTradeNo(String outTradeNo);
	
}
