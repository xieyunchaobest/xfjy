package com.xyc.proj.mapper;

import java.util.List;
import java.util.Map;

import com.xyc.proj.entity.ClientUser;

public interface ClientUserMapper {
	List<ClientUser> getClientUserPage(Map m);

	Long getClientUserPageCount(Map m);
}
