package com.xyc.proj.service;

import java.util.List;

import com.xyc.proj.entity.ElectricCar;
import com.xyc.proj.entity.Order;
import com.xyc.proj.entity.Question;
import com.xyc.proj.entity.UserAuthCode;
import com.xyc.proj.entity.Version;

public interface ClientService {

	void saveUserAuthCode(UserAuthCode u );
	
	List getUserListByMobileNoAndAuthCode(String mobileNo,String authCode);
	 
}
