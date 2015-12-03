package com.xyc.proj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyc.proj.entity.ClientUser;

public interface ClientUserRepository extends JpaRepository<ClientUser, Long> {
	ClientUser findByOpenId(String openId);
	ClientUser findByMobileNo(String mobileNo);
}
