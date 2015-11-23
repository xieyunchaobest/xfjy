package com.xyc.proj.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xyc.proj.entity.ClientUser;
import com.xyc.proj.entity.DepositLog;
import com.xyc.proj.entity.DepositSummary;
import com.xyc.proj.entity.Order;
import com.xyc.proj.entity.Schedule;
import com.xyc.proj.entity.TimeSplit;
import com.xyc.proj.entity.UserAddress;
import com.xyc.proj.entity.UserAuthCode;
import com.xyc.proj.entity.Version;
import com.xyc.proj.entity.Worker;
import com.xyc.proj.global.Constants;
import com.xyc.proj.mapper.UserAddressMapper;
import com.xyc.proj.pay.Configure;
import com.xyc.proj.pay.RandomStringGenerator;
import com.xyc.proj.pay.ScanPayReqData;
import com.xyc.proj.pay.ScanPayService;
import com.xyc.proj.pay.Signature;
import com.xyc.proj.pay.XMLParser;
import com.xyc.proj.repository.AreaRepository;
import com.xyc.proj.repository.AyiRepository;
import com.xyc.proj.repository.CleanToolsRepository;
import com.xyc.proj.repository.ClientUserRepository;
import com.xyc.proj.repository.CommunityRepository;
import com.xyc.proj.repository.ConfigRepository;
import com.xyc.proj.repository.DepositLogRepository;
import com.xyc.proj.repository.DepositSummaryRepository;
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
	@Autowired
	DepositLogRepository depositLogRepository;
	@Autowired
	DepositSummaryRepository depositSummaryRepository;
	@Autowired
	ClientUserRepository clientUserRepository;
	
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
			
			List<Worker> aiyiList=ayiRepository.serviceTypeOne(serviceType);
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
				 sd.setOrderId(o.getId());
				 sd.setState(Constants.STATE_P);
				 resList.add(sd);
			 }
		}
		return resList;
	}
 
	
	public double getTotalPrice(Order o) {
		if(StringUtil.isBlank(o.getServiceType()))return-1000d;
		double res=-10000d;
		if(Constants.SERVICE_TYPE_CC.equals(o.getServiceType())) {//普通宝洁
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
		}else if(Constants.SERVICE_TYPE_DBJ.equals(o.getServiceType())) {//dabaojie
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
		}else if(Constants.SERVICE_TYPE_CBL.equals(o.getServiceType())) {//cbl
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
		}else if(Constants.SERVICE_TYPE_KH.equals(o.getServiceType())) {
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
		String durationText="";
		if(!StringUtil.isBlank(o.getStartTime())) {
			Integer iStartTime=Integer.parseInt(o.getStartTime());
			durationText=iStartTime+"";
			if(!StringUtil.isBlank(o.getEndTime())) {
				Integer iEndTime=Integer.parseInt(o.getEndTime());
				durationText=durationText+":"+iEndTime;
			}
		}
		o.setDurationText(durationText);
		return o;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveOrder(Order order) {
		Order o=orderRepository.save(order);
		if(Constants.SERVICE_TYPE_CC.equals(o.getServiceType())) {
			saveSchedule(o);
		}
		
	}
	
	
	@Transactional(propagation=Propagation.NESTED)
	public void saveSchedule(Order o ) {
		if(Constants.CYCLE_TYPE_BY.equals(o.getCycleType())) {
			List scheduleList=getScheduleList4Month(o);
			scheduleRepository.save(scheduleList);
		}else {
			Schedule sd=new Schedule();
			sd.setBusiDate(o.getServiceDate());
			sd.setCreatedTime(new Date());
			sd.setStartTime(o.getStartTime());
			sd.setEndTime(o.getEndTime());
			sd.setOrderId(o.getId());
			scheduleRepository.save(sd);
		}
	}
	
	
	
	public String getConfigValue(String configCode) {
		return configRepository.findByConfigCode(configCode).getConfigValue();
	}
	
	
	public List getCleanToolsList(String serviceType) {
		return cleanToolsRepository.findByServieType(serviceType);
	}
	
//	public static void main(String arg[]) {
//		//new ClientServiceImpl().getScheduleList4Month("2015-11-11", "3", "3");
//		String info = null;
//		try{
//			String content="质控消息:患者大是病历书写不合格,请查看!";
//			HttpClient httpclient = new HttpClient();
//			PostMethod post = new PostMethod("http://sms.api.ums86.com:8899/sms/Api/Send.do");//
//			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"gbk");
//			post.addParameter("SpCode", "217289");
//			post.addParameter("LoginName", "tj_xfjy");
//			post.addParameter("Password", "helloWORLD123");
//			post.addParameter("MessageContent", content);
//			post.addParameter("UserNumber", "18611298927");
//			post.addParameter("SerialNumber", "12345678901234567890");
//			post.addParameter("ScheduleTime", "");
//			post.addParameter("ExtendAccessNum", "");
//			post.addParameter("f", "1");
//			httpclient.executeMethod(post);
//			info = new String(post.getResponseBody(),"gbk");
//			System.out.println(info);
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	
	public Map getOrderMap(String openId) {
		Map resMap=new HashMap();
		List<Order> orderList=getOrderList(openId,null);
		
		List unFinishedList=new ArrayList();
		List finishedList=new ArrayList();
		
		if(orderList!=null) {
			for(int i=0;i<orderList.size();i++) {
				Order o=orderList.get(i);
				if(Constants.ORDER_STATE_FINISH.equals(o.getState())) {
					finishedList.add(o);
				}else {
					unFinishedList.add(o);
				}
			}
		}
		resMap.put("finishedOrders", finishedList);
		resMap.put("unFinishedOrders", unFinishedList);
		return resMap;
	}
	
	
	public Order fillOrder(Order o) {
		if(Constants.SERVICE_TYPE_CC.equals(o.getServiceType())) {
			o.setServiceTypeText("普通宝洁");
		}else if(Constants.SERVICE_TYPE_DBJ.equals(o.getServiceType())) {
			o.setServiceTypeText("大宝洁");
		}else if(Constants.SERVICE_TYPE_CBL.equals(o.getServiceType())) {
			o.setServiceTypeText("擦玻璃");
		}else if(Constants.SERVICE_TYPE_KH.equals(o.getServiceType())) {
			o.setServiceTypeText("开荒");
		}
		
		if(Constants.CYCLE_TYPE_BY.equals(o.getCycleType())) {
			o.setCycleTypeText("包月");
		}else {
			o.setCycleTypeText("零工");
		}
		if(Constants.ORDER_PAY_MODE_WECHAT.equals(o.getPayMode())) {
			o.setPayModeText("微信");
		}else {
			o.setPayModeText("余额");
		}
		
		
		if(Constants.ORDER_STATE_UNPAY.equals(o.getState())) {
			o.setStateText("未支付");
		}else if(Constants.ORDER_STATE_PAYED.equals(o.getState())) {
			o.setStateText("已支付");
		}else if(Constants.ORDER_STATE_CONFIRMED.equals(o.getState())) {
			o.setStateText("已确认");
		}else if(Constants.ORDER_STATE_FINISH.equals(o.getState())) {
			o.setStateText("已完成");
		} 
		return o;
	}
	
	//避免方法重写，让按照主键查询和openId查询共用一个方法体
	public List<Order> getOrderList(String openId,Long oid) {
		Map parmMap=new HashMap();
		parmMap.put("openId", openId);
		
		List<Order> orderList=new ArrayList();
		if(!StringUtil.isBlank(openId)) {
			orderList=orderRepository.findByOpenId(openId);
		}else {
			orderList=orderRepository.findById(oid);
		}
		
		if(orderList!=null) {
			for(int i=0;i<orderList.size();i++) {
				Order o=orderList.get(i);
				o=fillOrder(o);
			}
		}
		return orderList;
	}
	
	public void deposit(String openId,double amount) {
		DepositSummary sum=depositSummaryRepository.findByOpenId(openId);
		if(sum==null) {
			sum=new DepositSummary();
			sum.setFee(0d);
			sum=depositSummaryRepository.save(sum);
		}
		
		DepositLog ds=new DepositLog();
		ds.setDepositAmount(amount);
		ds.setBalance(sum.getFee()+amount);
		depositLogRepository.save(ds);
	}
	
	public Map personalCenter(String openId) {
		Map resMap=new HashMap();
		ClientUser cu=clientUserRepository.findByOpenId(openId);
		DepositSummary ds=depositSummaryRepository.findByOpenId(openId);
		
//		Map parmMap=new HashMap();
//		parmMap.put("openId", openId);
//		List addressList=userAddressMapper.findAddressByUser(parmMap);
		
		resMap.put("clientUser", cu);
		resMap.put("depositSummary", ds);
		return resMap;
	}
	
	
	public Order getOrder(Long id) {
		List orderList= this.getOrderList(null, id);
		if(orderList==null || orderList.size()==0) {
			return new Order();
		}else {
			return (Order)orderList.get(0);
		}
	}
	
	public void saveClientUser(ClientUser cu) {
		clientUserRepository.save(cu);
	}
	
	public ClientUser getClientUser(String openId) {
		return clientUserRepository.findByOpenId(openId);
	}
	
	
	public Map createOrder(Order o,Map paraMap) {
		Map resMap=new HashMap();
		resMap.put("resultCode", "S");
		Order order=new Order();
		String outTradeNo="";
		if(StringUtil.isBlank(o.getOutTradeNo())) { //第一次支付，需要保存
			order=getConfirmOrder(o);
			order.setState(Constants.URL_PRE_PAY);

			outTradeNo = RandomStringGenerator.getRandomStringByLength(32);
			order.setOutTradeNo(outTradeNo);
			order.setPayMode(Constants.ORDER_PAY_MODE_WECHAT);
			saveOrder(order);
		}else {//从未完成订单中，不需要重新创建订单
			outTradeNo=o.getOutTradeNo();
			order=orderRepository.findByOutTradeNo(outTradeNo);
		}
		
		int totalFee = 1;
		//totalFee=order.getTotalFee().intValue()*100;
		
		String timeStart = DateUtil.Time2Str(new Date(),DateUtil.format1);
		String timeExpire = DateUtil.getDiffDate(1);
		//String openId="o74xjw9vCvp6Wxa5zPEPu4ghP8gA";
		String openId=(String)paraMap.get("openId");
		String spBillCreateIP=(String)paraMap.get("spBillCreateIP");
		
		ScanPayReqData scanPayReqData = new ScanPayReqData(outTradeNo, totalFee, spBillCreateIP,
				timeStart, timeExpire,openId);
		try {
			String res=new ScanPayService().request(scanPayReqData);
			Map rm=XMLParser.getMapFromXML(res);
			if(rm.containsKey("prepay_id")) {
				String prepay_id=(String)rm.get("prepay_id");
				SortedMap pm=new TreeMap();
				resMap.put("prepay_id",prepay_id);
				
				pm.put("appId", Configure.appID);
				String tmp= DateUtil.getTimeStamp();
				pm.put("timeStamp",tmp);
				String nonceStr=RandomStringGenerator.getRandomStringByLength(32);
				pm.put("nonceStr", nonceStr);
				pm.put("package", "prepay_id="+prepay_id);
				pm.put("signType", "MD5");
				String sign=Signature.getSign(pm);
				pm.put("paySign", sign);
				
				resMap.put("parm", pm);
				
				
				System.out.println("parmssssssssss is ="+pm);
				
			}else {
				resMap.put("resultCode", "E");
			}
			System.out.println("res======"+res);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			resMap.put("resultCode", "E");
		} catch (InstantiationException e) {
			resMap.put("resultCode", "E");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			resMap.put("resultCode", "E");
			e.printStackTrace();
		} catch (Exception e) {
			resMap.put("resultCode", "E");
			e.printStackTrace();
		}
		System.out.println("resMap======="+resMap);
		return resMap;
	}
	
	public void notifyOrder(String outTradeNo,String orderId) {
		Order order=orderRepository.findByOutTradeNo(outTradeNo);
		if(order!=null) {
			order.setOrderId(orderId);
			order.setState(Constants.ORDER_STATE_PAYED);
			order.setPayTime(new Date());
			orderRepository.save(order);
		}
	}
}
