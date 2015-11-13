package com.xyc.proj.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.xyc.proj.entity.Order;
import com.xyc.proj.entity.Schedule;
import com.xyc.proj.entity.TimeSplit;
import com.xyc.proj.entity.UserAddress;
import com.xyc.proj.entity.UserAuthCode;
import com.xyc.proj.entity.Version;
import com.xyc.proj.entity.Worker;
import com.xyc.proj.global.Constants;
import com.xyc.proj.mapper.UserAddressMapper;
import com.xyc.proj.repository.AreaRepository;
import com.xyc.proj.repository.AyiRepository;
import com.xyc.proj.repository.CleanToolsRepository;
import com.xyc.proj.repository.CommunityRepository;
import com.xyc.proj.repository.ConfigRepository;
import com.xyc.proj.repository.OrderRepository;
import com.xyc.proj.repository.ScheduleRepository;
import com.xyc.proj.repository.TimeSplitRepository;
import com.xyc.proj.repository.UserAddressRepository;
import com.xyc.proj.repository.UserCodeRepository;
import com.xyc.proj.repository.VersionRepository;
import com.xyc.proj.utility.DateUtil;
import com.xyc.proj.utility.StringUtil;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    UserCodeRepository userCodeRepository;

    @Autowired
    VersionRepository versionRepository;
    
	
	@Autowired
    ConfigRepository configRepository; 
	@Lazy
	@Autowired
	UserAddressMapper userAddressMapper;
	
	@Autowired
	TimeSplitRepository timeSplitRepository;
	@Autowired
	ScheduleRepository scheduleRepository;
	@Autowired
	CleanToolsRepository cleanToolsRepository;
	
	@Autowired
	AreaRepository areaRepository;
	
	@Autowired
	AyiRepository ayiRepository;
	@Autowired
	CommunityRepository communityRepository;
	@Autowired
	UserAddressRepository userAddressRepository;
	@Autowired
	OrderRepository orderRepository;
	
	@Override
	public void saveUserAuthCode(UserAuthCode u) {
		userCodeRepository.save(u);
	}
 
	@Override
	public List getUserListByMobileNoAndAuthCode(String mobileNo,String authCode) {
		return userCodeRepository.findUserByMobileAndAuthCode(mobileNo, authCode);
	}
	 
 
	
	public Version findVersionByPfType(String pfType) {
		return versionRepository.findByPfType(pfType);
	}

	@Override
	public List findAddressByUser(Map m) {
		return userAddressMapper.findAddressByUser(m);
	}
	
	
	/**
	 * 先找出能预约的，然后在排除掉
	 */
	public List getNonReservationTimeList(Map m) {
		String serviceDate=(String)m.get("serviceDate");
		String serviceType=(String)m.get("serviceType");
		
		List<TimeSplit> timeSplitList=timeSplitRepository.findAll();
		List tempList=new ArrayList();
		for(int i=0;i<timeSplitList.size();i++) {
			TimeSplit ts=timeSplitList.get(i);
			String tscode=ts.getCode();
			
			int startTime=ts.getStartTime();
			int endTime=ts.getEndTime();
			
			List<Worker> aiyiList=ayiRepository.findByServiceType(serviceType);
			int ayisize=aiyiList.size();
			int ayin=0;
			for(int j=0;j<ayisize;j++) {
			Worker ayi=aiyiList.get(j);
				Long aid=ayi.getId();
				List<Schedule> scheList=scheduleRepository.findByAyiIdAndBusiDateAndState(aid,serviceDate,"A");
				int n=0;
				int scheSize=scheList.size();
				for(int k=0;k<scheSize;k++) {
					Schedule schedule=scheList.get(k);
					int sTime=Integer.parseInt(schedule.getStartTime());
					int eTime=Integer.parseInt(schedule.getEndTime());
					if(startTime<eTime && endTime>sTime) {//和某个阿姨的时间冲突
						n++;
						break;
					}
				}
				if(n!=0) {//
					ayin++;
				}
			}
			
			if(ayin==ayisize) {
				tempList.add(tscode);
			}
		} 
		return tempList;
	}

	@Override
	public List findAreaList() {
		return areaRepository.findAll();
	}
	
	
	public List findCommunityByAreaId(Long areaId) {
		List commList=communityRepository.findByAreaId(areaId);
		return commList;
	}
	
	public List findListByAreaIdAndName(Long areaId,String communityName){
		communityName="%"+communityName+"%";
		List commList=communityRepository.findListByAreaIdAndName(areaId, communityName);
		return commList;
	}
	
	public void saveUserAddress(UserAddress ua) {
		userAddressRepository.save(ua);
	}
	
	
	public void deleteUserAdderss(UserAddress ua) {
		UserAddress u=userAddressRepository.findOne(ua.getId());
		u.setState(com.xyc.proj.global.Constants.STATE_P);
		userAddressRepository.save(u);
	}
	
	
	public List getScheduleList4Month(Order o ) {
		List resList=new ArrayList();
		
		Date startD=DateUtil.strToDate(o.getServiceDate());
		Calendar startC = Calendar.getInstance();
		startC.setTime(startD);
		
		int drationDay=Integer.parseInt(o.getDurationMonth())*30;
		
		Calendar endC = Calendar.getInstance();
		endC.setTime(startD);
		endC.add(Calendar.DATE, drationDay);
		
		while(! (endC.before(startC))) {
			startC.add(Calendar.DATE, 1);
			 String w = ""+(startC.get(Calendar.DAY_OF_WEEK)-1);
			 if(o.getRepeatInWeek().contains(w)) {
				String d= DateUtil.date2Str(startC.getTime());
				System.out.println(d);
				 Schedule sd=new Schedule();
				 sd.setBusiDate(d);
				 sd.setCreatedTime(new Date());
				 sd.setStartTime(o.getStartTime());
				 sd.setEndTime(o.getEndTime());
				 sd.setOrderId(o.getOrderId());
				 sd.setState(Constants.STATE_P);
				 resList.add(sd);
			 }
		}
		return resList;
	}
 
	
	public double getTotalPrice(Order o) {
		if(StringUtil.isBlank(o.getServicetype()))return-1000d;
		double res=-10000d;
		if(Constants.SERVICE_TYPE_CC.equals(o.getServicetype())) {//普通宝洁
			double dprice=Double.parseDouble(configRepository.findByConfigCode(Constants.CONFIG_PTBJDJ).getConfigValue());
			int iduration=Integer.parseInt(o.getDuration());
			double cleanToolsfee=0.0d;
			if(Constants.YES.equals(o.getIsProviceCleanTools())) {
				cleanToolsfee=Double.parseDouble(configRepository.findByConfigCode(Constants.CONFIG_CLEAN_TOOLS_FEE4PTBJ).getConfigValue());
				o.setToolFeeClean(cleanToolsfee);
			}
			if(Constants.CYCLE_TYPE_SG.equals(o.getCycleType())) {
				res=iduration*dprice+cleanToolsfee;
			}else {
				int times=getScheduleList4Month(o).size();
				res=iduration*dprice*times+cleanToolsfee;
			}
		}else if(Constants.SERVICE_TYPE_DBJ.equals(o.getServicetype())) {//dabaojie
			double dprice=Double.parseDouble(configRepository.findByConfigCode(Constants.CONFIG_DBJDJ).getConfigValue());
			int area=Integer.parseInt(o.getArea());
			double cleanToolsfee=0.0d;
			if(Constants.YES.equals(o.getIsProviceCleanTools())) {
				cleanToolsfee=Double.parseDouble(configRepository.findByConfigCode(Constants.CONFIG_CLEAN_TOOLS_FEE4DBJ).getConfigValue());
				o.setToolFeeClean(cleanToolsfee);
			}
			if(Constants.CYCLE_TYPE_SG.equals(o.getCycleType())) {
				res=area*dprice+cleanToolsfee;
			}else {
				int times=getScheduleList4Month(o).size();
				res=area*dprice*times+cleanToolsfee;
			}
		}else if(Constants.SERVICE_TYPE_CBL.equals(o.getServicetype())) {//cbl
			double cleanToolsfee=0.0d;
			if(Constants.YES.equals(o.getIsProviceCleanTools())) {
				cleanToolsfee=Double.parseDouble(configRepository.findByConfigCode(Constants.CONFIG_CLEAN_TOOLS_FEE4CBL).getConfigValue());
				o.setToolFeeClean(cleanToolsfee);
			}
			double cytdprice=Double.parseDouble(configRepository.findByConfigCode(Constants.CONFIG_CYTDJ).getConfigValue());
			int iytCount=Integer.parseInt(o.getBalconyCount());
			double cncdprice=Double.parseDouble(configRepository.findByConfigCode(Constants.CONFIG_CNCDJ).getConfigValue());
			int incCount=Integer.parseInt(o.getWindowCount());
			res=cytdprice*iytCount+cncdprice*incCount+cleanToolsfee;
		}else if(Constants.SERVICE_TYPE_KH.equals(o.getServicetype())) {
			double dprice=Double.parseDouble(configRepository.findByConfigCode(Constants.CONFIG_KHDJ).getConfigValue());
			int area=Integer.parseInt(o.getArea());
			double cleanToolsfee=0.0d;
			if(Constants.YES.equals(o.getIsProviceCleanTools())) {
				cleanToolsfee=Double.parseDouble(configRepository.findByConfigCode(Constants.CONFIG_CLEAN_TOOLS_FEE4KH).getConfigValue());
				o.setToolFeeClean(cleanToolsfee);
			}
			res=area*dprice+cleanToolsfee;
		}
		return res;
	}
	
	
	public Order getConfirmOrder(Order o) {
		double totalFee=getTotalPrice(o);
		o.setTotalFee(totalFee);
		return o;
	}
	
	public void saveOrder(Order order) {
		orderRepository.save(order);
		saveSchedule(order);
	}
	
	public void saveSchedule(Order o ) {
		if(Constants.CYCLE_TYPE_BY.equals(o.getCycleType())) {
			List scheduleList=getScheduleList4Month(o);
			scheduleRepository.save(scheduleList);
		}else {
			Schedule sd=new Schedule();
			sd.setBusiDate(o.getServiceDate());
			sd.setCreatedTime(new Date());
			sd.setStartTime(o.getStartTime());
			sd.setEndTime(o.getStartTime());
			sd.setOrderId(o.getOrderId());
			scheduleRepository.save(sd);
		}
	}
	
	
	public List getCleanToolsList(String serviceType) {
		return cleanToolsRepository.findByServieType(serviceType);
	}
	
	public static void main(String arg[]) {
		//new ClientServiceImpl().getScheduleList4Month("2015-11-11", "3", "3");
	}
	
}
