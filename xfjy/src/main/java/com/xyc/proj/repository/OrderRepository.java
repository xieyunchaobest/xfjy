package com.xyc.proj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xyc.proj.entity.Order;



public interface OrderRepository extends JpaRepository<Order, Long> {
	
	List findByOpenId(String openId);
	
	List findById(Long id);
	
	Order findByOutTradeNo(String outTradeNo);
	
	@Query("select  o,u from Order o,UserAddress u,Area a  where u.areaId=a.id and o.userAddressId=u.id   and o.outTradeNo=?1") 
	List getCleanOrderWithAddressInfo(String outTradeNo);
	
}
