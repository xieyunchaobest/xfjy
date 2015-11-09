package com.xyc.proj.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.xyc.proj.entity.Ayi;
import com.xyc.proj.entity.Schedule;
import com.xyc.proj.entity.TimeSplit;
import com.xyc.proj.entity.UserAuthCode;
import com.xyc.proj.entity.Version;
import com.xyc.proj.mapper.UserAddressMapper;
import com.xyc.proj.repository.AyiRepository;
import com.xyc.proj.repository.ConfigRepository;
import com.xyc.proj.repository.ScheduleRepository;
import com.xyc.proj.repository.TimeSplitRepository;
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
	AyiRepository ayiRepository;
	
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
	
	
	public List getNonReservationTimeList(Map m) {
		Date busiDate=(Date)m.get("busiDate");
		String serviceType=(String)m.get("serviceType");
		
		List<TimeSplit> timeSplitList=timeSplitRepository.findAll();
		List resList=new ArrayList();
		for(int i=0;i<timeSplitList.size();i++) {
			TimeSplit ts=timeSplitList.get(i);
			String tscode=ts.getCode();
			
			int startTime=ts.getStartTime();
			int endTime=ts.getEndTime();
			
			List<Ayi> aiyiList=ayiRepository.findByServiceType(serviceType);
			
		loop2:	for(int j=0;j<aiyiList.size();j++) {
				Ayi ayi=aiyiList.get(j);
				Long aid=ayi.getId();
				List<Schedule> scheList=scheduleRepository.findByAyiIdAndBusiDate(aid, busiDate);
				for(int k=0;k<scheList.size();k++) {
					Schedule schedule=scheList.get(k);
					int sTime=schedule.getStartTime();
					int eTime=schedule.getEndTime();
					if( !(startTime<eTime && endTime>sTime) ) {
						resList.add(tscode);
						break loop2;
					}
				}
			}
		}
		return resList;
	}
	
	
	 
	
}
