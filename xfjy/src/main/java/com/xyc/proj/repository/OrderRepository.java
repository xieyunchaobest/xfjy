package com.xyc.proj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xyc.proj.entity.Order;



public interface OrderRepository extends JpaRepository<Order, Long> {
	@Query("select o from Order o where o.openId=? and (o.state='P' or o.state='C' or o.state='F') order by o.serviceDate desc") 
	List findByOpenIdAndState(String openId);
	
	List findById(Long id);
	 
	Order findByOutTradeNo(String outTradeNo);
	
	@Query("select  o,u from Order o,UserAddress u,Area a  where u.areaId=a.id and o.userAddressId=u.id   and o.outTradeNo=?1") 
	List getCleanOrderWithAddressInfo(String outTradeNo);
	
	@Query("select  o,u from Order o,UserAddress u,Area a  where u.areaId=a.id and o.userAddressId=u.id and o.state=?1 and o.serviceType=?2 order by o.serviceDate desc") 
	List findByStateAndServiceType(String state,String serviceType);
	
	
	@Query("select o from OrderWorker ow,Order o where o.id=ow.orderId and ow.workerId=?1 and o.state='C' order by o.serviceDate desc") 
	List findByWorkerIdAndState(Long workerId);
	
	@Query("select distinct o from OrderWorker ow,Order o,Schedule s  where o.id=ow.orderId and ow.workerId=?1 and s.orderId=o.id and (o.state='C' or o.state='F') and s.busiDate=?2") 
	List findByWorkerIdAndState(Long workerId,String serviceDate);
	
}
