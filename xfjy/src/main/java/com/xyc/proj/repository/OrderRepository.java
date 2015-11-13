package com.xyc.proj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyc.proj.entity.Order;



public interface OrderRepository extends JpaRepository<Order, Long> {
	
}
