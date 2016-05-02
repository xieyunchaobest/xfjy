package com.xyc.proj.mapper;

import java.util.List;
import java.util.Map;

import com.xyc.proj.entity.Order;

public interface OrderMapper {
	List<Order> getOrderPage(Map m);
	
	Long getOrderPageCount(Map m);
}
