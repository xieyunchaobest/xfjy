package com.xyc.proj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyc.proj.entity.Config;

public interface ConfigRepository extends JpaRepository<Config, Long> {

	Config findByConfigCode(String code);
	
	List<Config> findByConfigName(String name);
}
