package com.xyc.proj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyc.proj.entity.Area;
import com.xyc.proj.entity.CleanTools;



public interface CleanToolsRepository extends JpaRepository<CleanTools, Long> {
}
