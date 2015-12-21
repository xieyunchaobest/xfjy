package com.xyc.proj.mapper;

import java.util.List;
import java.util.Map;

public interface DepositStatMapper {
	List getDepositStatPage(Map m);
	
	Long getDepositStatPageCount(Map m);
}
