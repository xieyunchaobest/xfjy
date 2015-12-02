package com.xyc.proj.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.xyc.proj.entity.Order;
import com.xyc.proj.entity.OrderWorker;
import com.xyc.proj.entity.Schedule;
import com.xyc.proj.entity.Worker;
import com.xyc.proj.global.Constants;
import com.xyc.proj.mapper.ClientUserMapper;
import com.xyc.proj.mapper.CommunityMapper;
import com.xyc.proj.mapper.OrderMapper;
import com.xyc.proj.mapper.WorkerMapper;
import com.xyc.proj.repository.OrderRepository;
import com.xyc.proj.repository.OrderWorkerRepository;
import com.xyc.proj.repository.ScheduleRepository;
import com.xyc.proj.repository.StoreRepository;
import com.xyc.proj.repository.WorkerRepository;
import com.xyc.proj.utility.DateUtil;
import com.xyc.proj.utility.PageView;

@Service
public class ServerServiceImpl implements ServerService {
	
	@Autowired
	WorkerRepository workerRepository;
	@Autowired
	OrderRepository orderRepository;
	
	@Lazy
	@Autowired
	WorkerMapper workerMapper;
	
	@Lazy
	@Autowired
	OrderMapper orderMapper;
	
	@Lazy
	@Autowired
	CommunityMapper communityMapper;
	
	@Lazy
	@Autowired
	ClientUserMapper clientUserMapper;
	
	@Autowired
	ClientService clientService;
	@Autowired
	StoreRepository storeRepository;
	@Autowired
	OrderWorkerRepository orderWorkerRepository;
	@Autowired
	ScheduleRepository scheduleRepository;
	
	public PageView getWorkPage(Map m) {
		PageView pv = new PageView((Integer) m.get("currentPageNum"));
		m.put("start", pv.getStart());
		m.put("end", pv.getCurrentMaxCnt());
		pv.setResultList(findWorkList(m));
		pv.setTotalRecord(workerMapper.getWorkerCount(m));
		return pv;
	}
	
	

	public List findWorkList(Map pm) {
		List<Worker> workerList=workerMapper.getWorkPageList(pm);
		if(workerList!=null ) {
			for(int i=0;i<workerList.size();i++) {
				Worker w=workerList.get(i);
				if(Constants.EDUCATIONAL_LEVEL_MIDDLE.equals(w.getEducation())) {
					w.setEducationText("中学及以下");
				}else if(Constants.EDUCATIONAL_LEVEL_ZK.equals(w.getEducation())) {
					w.setEducationText("专科");
				}else if(Constants.EDUCATIONAL_LEVEL_BK.equals(w.getEducation())) {
					w.setEducationText("本科");
				}else if(Constants.EDUCATIONAL_LEVEL_SS.equals(w.getEducation())) {
					w.setEducationText("硕士");
				}
				
				if(Constants.WORK_SERVICE_TYPE_CLEAN.equals(w.getServiceTypeTwo())) {
					w.setServiceTypeTwoName("保洁");
				}else if(Constants.WORK_SERVICE_YS.equals(w.getServiceTypeTwo())) {
					w.setServiceTypeTwoName("月嫂");
				}else if(Constants.WORK_SERVICE_YY_YE.equals(w.getServiceTypeTwo())) {
					w.setServiceTypeTwoName("孕婴、育儿嫂");
				}else if(Constants.WORK_SERVICE_YL.equals(w.getServiceTypeTwo())) {
					w.setServiceTypeTwoName("养老护工");
				}else if(Constants.WORK_SERVICE_JZ.equals(w.getServiceTypeTwo())) {
					w.setServiceTypeTwoName("家政、小时工");
				}
				
				if(Constants.WORK_ROLE_ROLE_AY.equals(w.getRole())) {
					w.setRoleName("阿姨");
				}else if(Constants.WORK_ROLE_ROLE_TEACHER.equals(w.getRole())) {
					w.setRoleName("老师");
				}
				
				if(Constants.WORK_TIME_DAY.equals(w.getWorkTime())) {
					w.setWorkTimeName("白班");
				}else if(Constants.WORK_TIME_24H.equals(w.getWorkTime())) {
					w.setWorkTimeName("24小时");
				}
			}
		}
		
		return workerList;
	}
	
	public Worker getWorker(Long workerId) {
		return workerRepository.findOne(workerId);
	}
	
	public void deleteWorker(String workIds) {
		String workArr[]=workIds.split(",");
		for(int i=0;i<workArr.length;i++) {
			Long id=Long.parseLong(workArr[i]);
			Worker w=workerRepository.findOne(id);
			w.setState(Constants.STATE_P);
			workerRepository.save(w);
		}
	}
	
	public List findOrderList(Map m) {
		List orderList=orderMapper.getOrderPage(m);
		for(int i=0;i<orderList.size();i++) {
			Order o=(Order)orderList.get(i);
			o=clientService.fillOrder(o);
		}
		
		return orderList;
	}
	
	public PageView getOrderPageView(Map m) {
		PageView pv = new PageView((Integer) m.get("currentPageNum"));
		m.put("start", pv.getStart());
		m.put("end", pv.getCurrentMaxCnt());
		pv.setResultList(findOrderList(m));
		pv.setTotalRecord(orderMapper.getOrderPageCount(m));
		return pv;
	}
	
	public void dispatchOrder(Long orderId,List scList){
		//先删除，后添加
		List oList=orderWorkerRepository.findByOrderId(orderId);
		if(oList!=null && oList.size()>0) {
			orderWorkerRepository.delete(oList);
		}
		orderWorkerRepository.save(scList);
	}
	
	public List findByCodeAndPassword(String code,String pwd) {
		return workerRepository.findByCodeAndPassword(code, pwd);
	}
	
	public List findStore() {
		return storeRepository.findAll();
	}
	
	
	public PageView getCommunityPage(Map parm) {
		PageView pv = new PageView((Integer) parm.get("currentPageNum"));
		parm.put("start", pv.getStart());
		parm.put("end", pv.getCurrentMaxCnt());
		pv.setResultList(communityMapper.getCommunityPage(parm));
		pv.setTotalRecord(communityMapper.getCommunityPageCount(parm));
		return pv;
	}
	
	public PageView getClientUserPage(Map parm) {
		PageView pv = new PageView((Integer) parm.get("currentPageNum"));
		parm.put("start", pv.getStart());
		parm.put("end", pv.getCurrentMaxCnt());
		pv.setResultList(clientUserMapper.getClientUserPage(parm));
		pv.setTotalRecord(clientUserMapper.getClientUserPageCount(parm));
		return pv;
	}
	
	//查询某个订单分配的对象
	public List getDispatchedWorkerByOrderId(Long orderId) {
		List workerList=workerRepository.findWorkeByOrderId(orderId);
		return workerList;
	}
	
	
	
	public String getDispatchedWorkerName(Long orderId) {
		List resList=getDispatchedWorkerByOrderId(orderId);
		if(resList==null || resList.size()==0) return "";
		String res="";
		for(int i=0;i<resList.size();i++) {
			Worker w=(Worker) resList.get(i);
			String name=w.getName();
			res = (i == resList.size() - 1) ? res + name
					: res +name + ",";
		}
		return res;
	}
	
	//查询某个阿姨的时间表
	public List getWorkerScheduleList(Long workerId) {
		String curDate=DateUtil.getToday();
		List schList=scheduleRepository.findByAyiIdAndServiceDate(workerId,curDate);
		return schList;
	}
	
	
	public List getWorkerTimeSheet(Long workerId) {
		List schList= getWorkerScheduleList(workerId);
		//创建7天的日程
		List dayList=new ArrayList();
		for(int i=0;i<=7;i++) {
			Map dayMap=new HashMap();
			String day=DateUtil.getDiffDate(new Date(),i+1);
			dayMap.put("day", day);
			for(int j=8;j<=20;j++) {
				dayMap.put("hour"+j+"", j+"");
				dayMap.put("free"+j, "Y");
			}
			dayList.add(dayMap);
		}
		
		if(schList!=null && schList.size()>0) {
			for(int i=0;i<schList.size();i++) {
				Schedule s=(Schedule)schList.get(i);
				String serviceDate=s.getBusiDate();
				int startTime=Integer.parseInt(s.getStartTime());
				int endTime=Integer.parseInt(s.getEndTime());
				for(int j=startTime;j<endTime;j++) {
					for(int k=0;k<dayList.size();k++) {
						Map md=(Map)dayList.get(k);
						String day=(String)md.get("day");
						if(day.equals(serviceDate)) {
							Set set = md.entrySet();         
							Iterator it = set.iterator();         
							while(it.hasNext()){      
							     Map.Entry<String, String> entry1=(Map.Entry<String, String>)it.next();    
							     if(entry1.getValue().equals(j+"")) {
							    	 md.put("free"+j, "N");
							     }
							}
						}
					}
				}
				
			}
		}
		return dayList;
	}
}
