package com.xyc.proj.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xyc.proj.entity.Schedule;



public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
	List findByAyiIdAndBusiDateAndState(Long ayid,String busiDate,String state);
	
	List findByOrderId(Long oid);
	
	@Query("select s from Schedule s where ayiId=?1 and busiDate>=?2 order by s.busiDate") 
	List findByAyiIdAndServiceDate(Long aid,String serviceDate);
}
