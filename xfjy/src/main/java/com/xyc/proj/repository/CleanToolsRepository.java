package com.xyc.proj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyc.proj.entity.CleanTools;



public interface CleanToolsRepository extends JpaRepository<CleanTools, Long> {
	List findByServieType(String serviceType);
	
	
}
