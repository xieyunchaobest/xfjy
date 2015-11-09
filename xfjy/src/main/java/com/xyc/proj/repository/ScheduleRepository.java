package com.xyc.proj.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyc.proj.entity.Schedule;



public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
	List findByAyiIdAndBusiDate(Long ayid,Date busiDate);
}
