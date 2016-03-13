package com.xyc.proj.mapper;

import java.util.List;
import java.util.Map;

public interface CommunityMapper {
	List getCommunityPage(Map m);
	
	Long getCommunityPageCount(Map m);
}
