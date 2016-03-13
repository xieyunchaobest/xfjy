package com.xyc.proj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyc.proj.entity.TimeSplit;



public interface TimeSplitRepository extends JpaRepository<TimeSplit, Long> {
	
}
