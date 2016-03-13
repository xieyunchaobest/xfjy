package com.xyc.proj.service;

import com.xyc.proj.entity.Order;


public interface HouseKeepingService {
	public void dispatchOrder(Order o) ;
	
	public void createOrder(Order o);
	
	public Order getOrder(Long oid);
}
