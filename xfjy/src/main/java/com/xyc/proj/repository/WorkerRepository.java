package com.xyc.proj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyc.proj.entity.Worker;



public interface WorkerRepository extends JpaRepository<Worker, Long> {
	List<Worker> findByStoreId(Long storeId);
}
