package com.xyc.proj.service;

import java.util.List;
import java.util.Map;

import com.xyc.proj.entity.UserAuthCode;

public interface ClientService {

	void saveUserAuthCode(UserAuthCode u );
	
	List getUserListByMobileNoAndAuthCode(String mobileNo,String authCode);
	
	List findAddressByUser(Map m);
	
	List getNonReservationTimeList(Map m) ;
	 
}
