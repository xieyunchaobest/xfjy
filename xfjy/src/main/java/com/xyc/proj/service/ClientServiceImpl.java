package com.xyc.proj.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.xyc.proj.mapper.UserAddressMapper;
import com.xyc.proj.repository.AreaRepository;
import com.xyc.proj.repository.AyiRepository;
import com.xyc.proj.repository.CommunityRepository;
import com.xyc.proj.repository.ConfigRepository;
import com.xyc.proj.repository.ScheduleRepository;
import com.xyc.proj.repository.TimeSplitRepository;
import com.xyc.proj.repository.UserAddressRepository;
import com.xyc.proj.repository.UserCodeRepository;
import com.xyc.proj.repository.VersionRepository;
import com.xyc.proj.utility.DateUtil;

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
	AreaRepository areaRepository;
	
	@Autowired
	AyiRepository ayiRepository;
	@Autowired
	CommunityRepository communityRepository;
	@Autowired
	UserAddressRepository userAddressRepository;
	
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
					int sTime=schedule.getStartTime();
					int eTime=schedule.getEndTime();
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
		
//		List resList=new ArrayList();
//		for(int i=0;i<timeSplitList.size();i++) {
//			TimeSplit ts=timeSplitList.get(i);
//			String tscode=ts.getCode();
//			resList.add(tscode);
//		}
//		resList.removeAll(tempList);
//		
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
	
	
	public List getScheduleList4Month(String startDate,String durationMonth,String week) {
		List resList=new ArrayList();
		
		Date startD=DateUtil.strToDate(startDate);
		Calendar startC = Calendar.getInstance();
		startC.setTime(startD);
		
		int drationDay=Integer.parseInt(durationMonth)*30;
		
		Calendar endC = Calendar.getInstance();
		endC.setTime(startD);
		endC.add(Calendar.DATE, drationDay);
		
		while(! (endC.before(startC))) {
			startC.add(Calendar.DATE, 1);
			 String w = ""+(startC.get(Calendar.DAY_OF_WEEK)-1);
			 if(week.contains(w)) {
				String d= DateUtil.date2Str(startC.getTime());
				System.out.println(d);
				 Schedule sd=new Schedule();
				 sd.setBusiDate(d);
				 resList.add(sd);
			 }
		}
		return resList;
	}
 
	
	public double getTotalPrice(Order o) {
		return 100.0d;
	}
	
	public void saveOrder(Order order) {
		
	}
	
	
	public static void main(String arg[]) {
		new ClientServiceImpl().getScheduleList4Month("2015-11-11", "3", "3");
	}
	
}
