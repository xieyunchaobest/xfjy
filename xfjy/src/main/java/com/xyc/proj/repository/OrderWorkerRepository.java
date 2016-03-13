package com.xyc.proj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyc.proj.entity.OrderWorker;



public interface OrderWorkerRepository extends JpaRepository<OrderWorker, Long> {
	List<OrderWorker> findByOrderId(Long orderId);
}
