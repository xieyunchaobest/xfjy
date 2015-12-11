package com.xyc.proj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xyc.proj.entity.ClientUser;
import com.xyc.proj.entity.Worker;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
	List<Worker> findByStoreId(Long storeId);

	List<Worker> findByCodeAndPassword(String code, String password);

	List<Worker> findByAreaId(Long areaId);

	@Query("select  w,c from Worker w ,ClientUser c where  w.areaId=?1 and c.mobileNo=w.phone and w.serviceTypeOne='C' and w.role='A' and w.state='A'")
	List findWorkerAndOpenIdInArea(Long areaId);
	
	@Query("select  w,c from Worker w ,ClientUser c where c.mobileNo=w.phone and w.serviceTypeOne='C' and w.role='A' and w.state='A'")
	List findByRoleAndServiceTypeOneAndState();

	Worker findByPhone(String phone);

	@Query("select  w from Worker w ,Order o ,OrderWorker  ow  where w.id=ow.workerId and o.id=ow.orderId and  o.id=?1")
	List findWorkeByOrderId(Long orderId);

	@Query("select  cu from Worker w ,ClientUser cu   where w.phone=cu.mobileNo and  w.id=?1")
	ClientUser findOpenIdByWorkerphone(Long workerId);
	
	List findByRoleAndState(String role,String state);
	
	@Query("select  w from Worker w ,ClientUser cu   where w.phone=cu.mobileNo and  cu.openId=?1 and w.state='A'")
	Worker findWorkerByOpenId(String openId);
	
	
}
