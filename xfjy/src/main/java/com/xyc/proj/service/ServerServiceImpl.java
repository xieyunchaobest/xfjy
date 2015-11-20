package com.xyc.proj.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.xyc.proj.entity.Order;
import com.xyc.proj.entity.Worker;
import com.xyc.proj.global.Constants;
import com.xyc.proj.mapper.OrderMapper;
import com.xyc.proj.mapper.WorkerMapper;
import com.xyc.proj.repository.OrderRepository;
import com.xyc.proj.repository.WorkerRepository;
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
		return orderMapper.getOrderPage(m);
	}
	
	public PageView getOrderPageView(Map m) {
		PageView pv = new PageView((Integer) m.get("currentPageNum"));
		m.put("start", pv.getStart());
		m.put("end", pv.getCurrentMaxCnt());
		pv.setResultList(findOrderList(m));
		pv.setTotalRecord(orderMapper.getOrderPageCount(m));
		return pv;
	}
	
	public void dispatchOrder(String orderIds,Long ayiId){
		String orderIdArr[]=orderIds.split(",");
		//List orderIdList=Arrays.asList(orderIdArr);
		for(int i=0;i<orderIdArr.length;i++) {
			Long oid=Long.parseLong(orderIdArr[i]);
			Order o=orderRepository.findOne(oid);
			o.setAyiId(ayiId);
			orderRepository.save(o);
		}
	}
}
