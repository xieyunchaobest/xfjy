package com.xyc.proj.mapper;

import java.util.List;
import java.util.Map;

public interface ClientUserMapper {
	List getClientUserPage(Map m);

	Long getClientUserPageCount(Map m);
}
