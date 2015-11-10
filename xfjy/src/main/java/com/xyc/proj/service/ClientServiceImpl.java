package com.xyc.proj.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

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
		String busiDate=(String)m.get("serviceDate");
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
				List<Schedule> scheList=scheduleRepository.findByAyiIdAndBusiDate(aid, busiDate);
				int n=0;
				int scheSize=scheList.size();
				for(int k=0;k<scheSize;k++) {
					Schedule schedule=scheList.get(k);
					int sTime=schedule.getStartTime();
					int eTime=schedule.getEndTime();
					if(startTime<eTime && endTime>sTime) {//和某个阿姨的时间都不冲突
						n++;
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
	 
	
}
