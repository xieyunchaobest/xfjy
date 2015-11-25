package com.xyc.proj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xyc.proj.entity.Worker;



public interface WorkerRepository extends JpaRepository<Worker, Long> {
	List<Worker> findByStoreId(Long storeId);
	List<Worker> findByCodeAndPassword(String code,String password);
	
	List<Worker> findByAreaId(Long areaId);
	
	@Query("select  w,c from Worker w ,ClientUser c where  w.areaId=?1 and c.mobileNo=w.phone and w.serviceTypeOne='C'") 
	List<Worker> findWorkerAndOpenIdInArea(Long areaId);
}
