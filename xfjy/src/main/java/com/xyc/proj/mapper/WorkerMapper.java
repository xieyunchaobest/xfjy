package com.xyc.proj.mapper;

import java.util.List;
import java.util.Map;

public interface WorkerMapper {
	List getWorkPageList(Map m);
	
	Long getWorkerCount(Map m);
}
