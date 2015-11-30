package com.xyc.proj.service;

import java.util.List;
import java.util.Map;

import com.xyc.proj.entity.ClientUser;
import com.xyc.proj.entity.DepositSummary;
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
	
	public Order saveOrder(Order order);
	
	public List getCleanToolsList(String serviceType);
	
	public Order getConfirmOrder(Order o);
	
	public String getConfigValue(String code);
	
	public Map getOrderMap(String openId);
	
	public List getOrderList(String openId,Long oid);
	
	public Map deposit(String openId,double amount);
	
	public Map personalCenter(String openId);
	
	public Order getOrder(Long id) ;
	
	public void saveClientUser(ClientUser cu);
	
	public ClientUser getClientUser(String openId) ;
	
	public Map createOrder(Order o,Map paraMap);
	
	public void notifyOrder(String outTradeNo,String orderId);
	
	public Order fillOrder(Order o);
	
	public DepositSummary getBalance(String openId);
	
	public Map getWorkerTask(String openId,String serviceDate) ;
	
	public String fightOrder(Long oid,String openId);
}
