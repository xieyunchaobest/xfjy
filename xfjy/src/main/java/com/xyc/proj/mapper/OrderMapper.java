package com.xyc.proj.mapper;

import java.util.List;
import java.util.Map;

public interface OrderMapper {
	List getOrderPage(Map m);
	
	Long getOrderPageCount(Map m);
}
