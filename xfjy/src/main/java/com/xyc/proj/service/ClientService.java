package com.xyc.proj.service;

import java.util.List;
import java.util.Map;

import com.xyc.proj.entity.Order;
import com.xyc.proj.entity.UserAddress;
import com.xyc.proj.entity.UserAuthCode;

public interface ClientService {

	void saveUserAuthCode(UserAuthCode u );
	
	List getUserListByMobileNoAndAuthCode(String mobileNo,String authCode);
	
	List findAddressByUser(Map m);
	
	List getNonReservationTimeList(Map m) ;
	
	List findAreaList();
	
	public List findCommunityByAreaId(Long areaId) ;
	 
	List findListByAreaIdAndName(Long areaId,String communityName);
	
	public void saveUserAddress(UserAddress ua) ;
	
	public void deleteUserAdderss(UserAddress ua);
	
	public void saveOrder(Order order);
	
	public List getCleanToolsList(String serviceType);
	
	public Order getConfirmOrder(Order o);
}
