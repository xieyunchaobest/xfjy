package com.xyc.proj.service;

import java.util.List;
import java.util.Map;

import com.xyc.proj.entity.Community;
import com.xyc.proj.entity.Config;
import com.xyc.proj.entity.Worker;
import com.xyc.proj.utility.PageView;

public interface ServerService {
	List findWorkList(Map pm);

	public PageView getWorkPage(Map m);

	public Worker getWorker(Long workerId);

	public void deleteWorker(String workIds);

	public PageView getOrderPageView(Map m);

	public void dispatchOrder(Long orderId, List workerList);

	public List findByCodeAndPassword(String code, String pwd);

	public List findStore();

	public PageView getCommunityPage(Map parm);

	public PageView getClientUserPage(Map parm);

	public String getDispatchedWorkerName(Long orderId);

	public List getDispatchedWorkerByOrderId(Long orderId);

	public List getWorkerTimeSheet(Long workerId);
	
	public void saveWorker(Worker w);
	
	public List findTeachers() ;
	public Worker findWorker(Long id) ;
	public Community findCommunity(Long id) ;
	
	public void saveCommunity(Community c);
	public void deleteCommu(String commuIds);
	
	public Map getConfigMap() ;
	
	public Config getConfigByCode(String code) ;
	
	public void saveConfig(Config config);
	
	public Worker findWorkerDetail(Worker w) ;
	
	public Worker findWorkerPlus(Worker w) ;
	
	public List findWorkerByTeacherId(Long teacherId) ;
	
	List findWorkerByStateAndRole(String state,String role);
	
	public String updatePwd(Worker w,String opwd,String newPwd) ;
	
	public List findDianZhang();

}
