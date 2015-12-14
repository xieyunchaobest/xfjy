package com.xyc.proj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyc.proj.entity.Worker;

public interface AyiRepository extends JpaRepository<Worker, Long> {
	List serviceTypeOne(String type);
	
	List serviceTypeOneAndState(String type,String state);
}
