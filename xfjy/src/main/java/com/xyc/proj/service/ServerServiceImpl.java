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

import com.xyc.proj.entity.ClientUser;
import com.xyc.proj.entity.Community;
import com.xyc.proj.entity.Config;
import com.xyc.proj.entity.Order;
import com.xyc.proj.entity.OrderWorker;
import com.xyc.proj.entity.Schedule;
import com.xyc.proj.entity.Worker;
import com.xyc.proj.global.Constants;
import com.xyc.proj.mapper.ClientUserMapper;
import com.xyc.proj.mapper.CommunityMapper;
import com.xyc.proj.mapper.OrderMapper;
import com.xyc.proj.mapper.WorkerMapper;
import com.xyc.proj.pay.Configure;
import com.xyc.proj.repository.CommunityRepository;
import com.xyc.proj.repository.ConfigRepository;
import com.xyc.proj.repository.OrderRepository;
import com.xyc.proj.repository.OrderWorkerRepository;
import com.xyc.proj.repository.ScheduleRepository;
import com.xyc.proj.repository.StoreRepository;
import com.xyc.proj.repository.WorkerRepository;
import com.xyc.proj.utility.DateUtil;
import com.xyc.proj.utility.MsgUtil;
import com.xyc.proj.utility.PageView;
import com.xyc.proj.utility.StringUtil;

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
	@Autowired
	CommunityRepository communityRepository;
	@Autowired
	ConfigRepository configRepository;

	public PageView getWorkPage(Map m) {
		PageView pv = new PageView((Integer) m.get("currentPageNum"));
		m.put("start", pv.getStart());
		m.put("end", pv.getCurrentMaxCnt());
		pv.setResultList(findWorkList(m));
		pv.setTotalRecord(workerMapper.getWorkerCount(m));
		return pv;
	}

	public Worker findWorkerDetail(Worker w) {
		if (Constants.EDUCATIONAL_LEVEL_MIDDLE.equals(w.getEducation())) {
			w.setEducationText("中学及以下");
		} else if (Constants.EDUCATIONAL_LEVEL_ZK.equals(w.getEducation())) {
			w.setEducationText("专科");
		} else if (Constants.EDUCATIONAL_LEVEL_BK.equals(w.getEducation())) {
			w.setEducationText("本科");
		} else if (Constants.EDUCATIONAL_LEVEL_SS.equals(w.getEducation())) {
			w.setEducationText("硕士");
		}
		
		if(Constants.WORK_SERVICE_TYPE_CLEAN.equals(w.getServiceTypeOne())) {
			w.setServiceTypeTwoName("保洁");
		}else if(Constants.WORK_SERVICE_TYPE_JZ.equals(w.getServiceTypeOne())) {
			w.setServiceTypeTwoName("家政");
		}

		if (Constants.WORK_SERVICE_TYPE_CLEAN.equals(w.getServiceTypeTwo())) {
			w.setServiceTypeTwoName("保洁");
		} else if (Constants.WORK_SERVICE_YS.equals(w.getServiceTypeTwo())) {
			w.setServiceTypeTwoName("月嫂");
		} else if (Constants.WORK_SERVICE_YY_YE.equals(w.getServiceTypeTwo())) {
			w.setServiceTypeTwoName("孕婴、育儿嫂");
		} else if (Constants.WORK_SERVICE_YL.equals(w.getServiceTypeTwo())) {
			w.setServiceTypeTwoName("养老护工");
		} else if (Constants.WORK_SERVICE_JZ.equals(w.getServiceTypeTwo())) {
			w.setServiceTypeTwoName("家政、小时工");
		}

		if (Constants.WORK_ROLE_ROLE_AY.equals(w.getRole())) {
			w.setRoleName("阿姨");
		} else if (Constants.WORK_ROLE_ROLE_TEACHER.equals(w.getRole())) {
			w.setRoleName("老师");
		}

		if (Constants.WORK_TIME_DAY.equals(w.getWorkTime())) {
			w.setWorkTimeName("白班");
		} else if (Constants.WORK_TIME_24H.equals(w.getWorkTime())) {
			w.setWorkTimeName("24小时");
		}
		
		int yeearsold=Integer.parseInt(DateUtil.getToday().substring(0,4))-Integer.parseInt(w.getBirthday().substring(0,4));
		w.setYearsOld(yeearsold+"");
		
		String timeofStartWork=w.getTimeOfStartWork();
		if(!StringUtil.isBlank(timeofStartWork)) {
			int iworkExperience=Integer.parseInt(DateUtil.getToday().substring(0,4))-Integer.parseInt(w.getTimeOfStartWork().substring(0, 4));
			String workExperience=iworkExperience+"";
			w.setWorkExperience(workExperience);
		}else {
			w.setWorkExperience("未知");
		}
		
		if(StringUtil.isBlank(w.getPoliticStatus())) {
			w.setPoliticStatus("群众");
		}
		if(StringUtil.isBlank(w.getReligion())) {
			w.setReligion("无宗教信仰");
		}
		if(StringUtil.isBlank(w.getLanguageLevel())) {
			w.setLanguageLevel("一般");
		}
		
		return w;
	}
	
	public Worker findWorkerPlus(Worker w) {
		w=findWorkerDetail(w);
		Long tearcherId=w.getTeacherId();
		Worker teacher=workerRepository.findOne(tearcherId);
		w.setTeacher(teacher);
		return w;
	}
	
	public List findWorkList(Map pm) {
		List<Worker> workerList = workerMapper.getWorkPageList(pm);
		if (workerList != null) {
			for (int i = 0; i < workerList.size(); i++) {
				Worker w = workerList.get(i);
				w=findWorkerDetail(w);
			}
		}

		return workerList;
	}

	public Worker getWorker(Long workerId) {
		return workerRepository.findOne(workerId);
	}

	public void deleteWorker(String workIds) {
		String workArr[] = workIds.split(",");
		for (int i = 0; i < workArr.length; i++) {
			Long id = Long.parseLong(workArr[i]);
			Worker w = workerRepository.findOne(id);
			w.setState(Constants.STATE_P);
			workerRepository.save(w);
		}
	}

	public List findOrderList(Map m) {
		List orderList = orderMapper.getOrderPage(m);
		for (int i = 0; i < orderList.size(); i++) {
			Order o = (Order) orderList.get(i);
			o = clientService.fillOrder(o);
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

	public void dispatchOrder(Long orderId, List mList) {
		// 先删除，后添加
		List oList = orderWorkerRepository.findByOrderId(orderId);
		List scheduleList = scheduleRepository.findByOrderId(orderId);
		if (oList != null && oList.size() > 0) {
			orderWorkerRepository.delete(oList);
		}
		if (scheduleList != null && scheduleList.size() > 0) {
			scheduleRepository.delete(scheduleList);
		}
		// 添加
		Order o = orderRepository.findOne(orderId);
		if (Constants.CYCLE_TYPE_BY.equals(o.getCycleType())) {
			List sList = clientService.getScheduleList4Month(o, mList);
			scheduleRepository.save(sList);
		} else {
			List sList = clientService.getScheduleList4OneDay(o, mList);
			System.out.println("mList========="+mList);
			scheduleRepository.save(sList);
		}

		List orderWorkerList = new ArrayList();
		for (int i = 0; i < mList.size(); i++) {
			Map w = (Map) mList.get(i);
			Long wid = Long.parseLong((String) w.get("aid"));
			OrderWorker ow = new OrderWorker();
			ow.setOrderId(orderId);
			ow.setWorkerId(wid);
			orderWorkerList.add(ow);
		}
		orderWorkerRepository.save(orderWorkerList);
		o.setState(Constants.ORDER_STATE_CONFIRMED);
		
		orderRepository.save(o);
		//发送手机短信
		o=clientService.fillOrder(o);
		String serviceTypeText=o.getServiceTypeText();
		String serviceCycleText=o.getCycleTypeText();
		String serviceDate=o.getServiceDate();
		if(Constants.CYCLE_TYPE_BY.equals(o.getCycleType())) {
			serviceDate=(String)clientService.getServiceDateSet(o).get(0);
		}
		String durationText=o.getStartTime()+":00"+"-"+o.getEndTime()+":00";
		String serviceDateAndTime=serviceDate+" "+durationText;
		String content="您收到客服分配的"+serviceTypeText+serviceCycleText+"订单首次服务时间"+serviceDateAndTime+"请尽快到待办中查看"+"!";

		// 发送客服消息
		String firstWorkerPhone="";;
		String firstWorkerName="";
		for (int i = 0; i < mList.size(); i++) {
			Map w = (Map) mList.get(i);
			Long wid = Long.parseLong((String) w.get("aid"));
			ClientUser clientUser = workerRepository.findOpenIdByWorkerphone(wid);
			Worker ww=workerRepository.findOne(wid);
			String phoneNo=ww.getPhone();
			String workerName=ww.getName();
			if(i==0) {
				firstWorkerPhone=phoneNo;
				firstWorkerName=workerName;
			}
			String openId = clientUser.getOpenId();
			String loginurl = "http://weixin.tjxfjz.com/xfjy/client/workerTask.html";
			String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Configure.appID
					+ "&redirect_uri=" + loginurl + "&response_type=code&scope=snsapi_base&state=1#wechat_redirect";

			MsgUtil.sendTemplateMsg(Constants.MSG_KF_TEMPLATE_ID, openId, url, "您有新的任务", o.getFullAddress(), "客服派单",
					"点击查看详情");
			
			clientService.sendShortMsg(phoneNo, content);
		}
		
		//发送消息给用户
		String msgContent="您预约的"+serviceTypeText+serviceCycleText+"已受理完成，"+"服务时间"+serviceDateAndTime+",上门阿姨姓名:"+firstWorkerName+",电话"+firstWorkerPhone;
		clientService.sendShortMsg(o.getMobileNo(), msgContent);
		
	}

	
	public static void main(String args[]) {
		String serviceTypeText="大宝洁";
		String serviceCycleText="零工";
		String serviceDate="2015-12-12";
		String serviceDateAndTime="2015-12-12 12:00-12:00";
		String firstWorkerName="你好箭";
		String firstWorkerPhone="18766789876";
		//String content="您收到客服分配的"+serviceTypeText+serviceCycleText+"订单首次服务时间"+serviceDate+"请尽快到待办中查看"+"!";
		String msgContent="您预约的"+serviceTypeText+serviceCycleText+"已受理完成，"+"服务时间"+serviceDateAndTime+",上门阿姨姓名:"+firstWorkerName+",电话"+firstWorkerPhone;
		
		new ClientServiceImpl().sendShortMsg("13820103966", msgContent);
	}
	
	
	// public void dispatchOrder(Long orderId,List scList,List orderWorkerList){
	// //先删除，后添加
	// List oList=orderWorkerRepository.findByOrderId(orderId);
	// List scheduleList=scheduleRepository.findByOrderId(orderId);
	// if(oList!=null && oList.size()>0) {
	// orderWorkerRepository.delete(oList);
	// }
	// if(scheduleList!=null && scheduleList.size()>0) {
	// scheduleRepository.delete(scheduleList);
	// }
	//
	// scheduleRepository.save(scList);
	// orderWorkerRepository.save(orderWorkerList);
	// Order o=orderRepository.findOne(orderId);
	// o.setState(Constants.ORDER_STATE_CONFIRMED);
	// orderRepository.save(o);//修改状态
	// }

	public List findByCodeAndPassword(String code, String pwd) {
		return workerRepository.findByCodeAndPasswordAndRole(code, pwd,"T");
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

	// 查询某个订单分配的对象
	public List getDispatchedWorkerByOrderId(Long orderId) {
		List workerList = workerRepository.findWorkeByOrderId(orderId);
		return workerList;
	}

	public String getDispatchedWorkerName(Long orderId) {
		List resList = getDispatchedWorkerByOrderId(orderId);
		if (resList == null || resList.size() == 0)
			return "";
		String res = "";
		for (int i = 0; i < resList.size(); i++) {
			Worker w = (Worker) resList.get(i);
			String name = w.getName();
			res = (i == resList.size() - 1) ? res + name : res + name + ",";
		}
		return res;
	}

	// 查询某个阿姨的时间表
	public List getWorkerScheduleList(Long workerId) {
		String curDate = DateUtil.getToday();
		List schList = scheduleRepository.findByAyiIdAndServiceDate(workerId, curDate);
		return schList;
	}

	public List getWorkerTimeSheet(Long workerId) {
		List schList = getWorkerScheduleList(workerId);
		// 创建7天的日程
		List dayList = new ArrayList();
		for (int i = 0; i <= 7; i++) {
			Map dayMap = new HashMap();
			String day = DateUtil.getDiffDate(new Date(), i + 1);
			dayMap.put("day", day);
			for (int j = 8; j <= 20; j++) {
				dayMap.put("hour" + j + "", j + "");
				dayMap.put("free" + j, "Y");
			}
			dayList.add(dayMap);
		}

		if (schList != null && schList.size() > 0) {
			for (int i = 0; i < schList.size(); i++) {
				Schedule s = (Schedule) schList.get(i);
				String serviceDate = s.getBusiDate();
				int startTime = Integer.parseInt(s.getStartTime());
				int endTime = Integer.parseInt(s.getEndTime());
				for (int j = startTime; j < endTime; j++) {
					for (int k = 0; k < dayList.size(); k++) {
						Map md = (Map) dayList.get(k);
						String day = (String) md.get("day");
						if (day.equals(serviceDate)) {
							Set set = md.entrySet();
							Iterator it = set.iterator();
							while (it.hasNext()) {
								Map.Entry<String, String> entry1 = (Map.Entry<String, String>) it.next();
								if (entry1.getValue().equals(j + "")) {
									md.put("free" + j, "N");
								}
							}
						}
					}
				}

			}
		}
		return dayList;
	}
	
	
	public void saveWorker(Worker w) {
		w.setPassword("abc123");
		w=workerRepository.save(w);
		long id=w.getId();
		String code = String.format("%06d", id);     
		 w.setCode("XFJY"+code);
		 workerRepository.save(w);
	}
	
	public List findTeachers() {
		return workerRepository.findByRoleAndState("T","A");
	}
	
	
	public Worker findWorker(Long id) {
		return workerRepository.findOne(id);
	}
	
	public Community findCommunity(Long id) {
		return communityRepository.findOne(id);
	}
	
	public void saveCommunity(Community c) {
		communityRepository.save(c); 
	}
	
	public void deleteCommu(String commuIds) {
		String workArr[] = commuIds.split(",");
		for (int i = 0; i < workArr.length; i++) {
			Long id = Long.parseLong(workArr[i]);
			Community w = communityRepository.findOne(id);
			w.setState(Constants.STATE_P);
			communityRepository.save(w);
		}
	}
	
	public Map getConfigMap() {
		List configList= configRepository.findAll();
		Map configMap=new HashMap();
		for(int i=0;i<configList.size();i++) {
			Config c=(Config)configList.get(i);
			if(c.getConfigCode().equals("PTBJDJ")) {
				configMap.put("PTBJDJ", c);
			}else if(c.getConfigCode().equals("DBJDJ")) {
				configMap.put("DBJDJ", c);
			}else if(c.getConfigCode().equals("CNCDJ")) {
				configMap.put("CNCDJ", c);
			}else if(c.getConfigCode().equals("CYTDJ")) {
				configMap.put("CYTDJ", c);
			}else if(c.getConfigCode().equals("KHDJ")) {
				configMap.put("KHDJ", c);
			}else if(c.getConfigCode().equals("QJGJFY4PTBJ")) {
				configMap.put("QJGJFY4PTBJ", c);
			}else if(c.getConfigCode().equals("QJGJFY4DBJ")) {
				configMap.put("QJGJFY4DBJ", c);
			}else if(c.getConfigCode().equals("QJGJFY4CBL")) {
				configMap.put("QJGJFY4CBL", c);
			}else if(c.getConfigCode().equals("QJGJFY4KH")) {
				configMap.put("QJGJFY4KH", c);
			}
		}
		return configMap;
	}

	public Config getConfigByCode(String code) {
		return configRepository.findByConfigCode(code);
	}
	
	public void saveConfig(Config config) {
		configRepository.save(config);
	}
}
