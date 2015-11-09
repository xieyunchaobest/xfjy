package com.xyc.proj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyc.proj.entity.Ayi;



public interface AyiRepository extends JpaRepository<Ayi, Long> {
	List findByServiceType(String type);
}
