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
import com.xyc.proj.entity.Coupon;
import com.xyc.proj.entity.DepositLog;
import com.xyc.proj.entity.DepositSummary;
import com.xyc.proj.entity.Order;
import com.xyc.proj.entity.OrderWorker;
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
import com.xyc.proj.repository.CouponRepository;
import com.xyc.proj.repository.DepositLogRepository;
import com.xyc.proj.repository.DepositSummaryRepository;
import com.xyc.proj.repository.OrderRepository;
import com.xyc.proj.repository.OrderWorkerRepository;
import com.xyc.proj.repository.ScheduleRepository;
import com.xyc.proj.repository.TimeSplitRepository;
import com.xyc.proj.repository.UserAddressRepository;
import com.xyc.proj.repository.UserCodeRepository;
import com.xyc.proj.repository.VersionRepository;
import com.xyc.proj.repository.WorkerRepository;
import com.xyc.proj.utility.DateUtil;
import com.xyc.proj.utility.MsgUtil;
import com.xyc.proj.utility.StringUtil;
import com.xyc.proj.utility.WeixinUtil;

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
	@Autowired
	WorkerRepository workerRepository;
	@Autowired
	OrderWorkerRepository orderWorkerRepository;
	@Autowired
	CouponRepository couponRepository;

	@Override
	public void saveUserAuthCode(UserAuthCode u) {
		userCodeRepository.save(u);
	}

	@Override
	public List getUserListByMobileNoAndAuthCode(String mobileNo, String authCode) {
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
		String serviceDate = (String) m.get("serviceDate");
		String serviceType = (String) m.get("serviceType");

		List<TimeSplit> timeSplitList = timeSplitRepository.findAll();
		List tempList = new ArrayList();
		for (int i = 0; i < timeSplitList.size(); i++) {
			TimeSplit ts = timeSplitList.get(i);
			String tscode = ts.getCode();

			int startTime = ts.getStartTime();
			int endTime = ts.getEndTime();

			List<Worker> aiyiList = ayiRepository.serviceTypeOne(Constants.WORK_SERVICE_TYPE_CLEAN);
			int ayisize = aiyiList.size();
			int ayin = 0;
			for (int j = 0; j < ayisize; j++) {
				Worker ayi = aiyiList.get(j);
				Long aid = ayi.getId();
				List<Schedule> scheList = scheduleRepository.findByAyiIdAndBusiDateAndState(aid, serviceDate, "A");
				int n = 0;
				int scheSize = scheList.size();
				for (int k = 0; k < scheSize; k++) {
					Schedule schedule = scheList.get(k);
					int sTime = Integer.parseInt(schedule.getStartTime());
					int eTime = Integer.parseInt(schedule.getEndTime());
					if (startTime < eTime && endTime > sTime) {// 和某个阿姨的时间冲突
						n++;
						break;
					}
				}
				if (n != 0) {//
					ayin++;
				}
			}

			if (ayin == ayisize) {
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
		List commList = communityRepository.findByAreaId(areaId);
		return commList;
	}

	public List findListByAreaIdAndName(Long areaId, String communityName) {
		communityName = "%" + communityName + "%";
		List commList = communityRepository.findListByAreaIdAndName(areaId, communityName);
		return commList;
	}

	public void saveUserAddress(UserAddress ua) {
		userAddressRepository.save(ua);
	}

	public void deleteUserAdderss(UserAddress ua) {
		UserAddress u = userAddressRepository.findOne(ua.getId());
		u.setState(com.xyc.proj.global.Constants.STATE_P);
		userAddressRepository.save(u);
	}

	public List getScheduleList4OneDay(Order o, List mList) {
		List scList = new ArrayList();
		for (int i = 0; i < mList.size(); i++) {
			Map mm = (Map) mList.get(i);
			Long wid = Long.parseLong((String) mm.get("aid"));
			String duration = (String) (mm.get("druation"));
			int idruation = Integer.parseInt(duration);
			Schedule sd = new Schedule();
			sd.setBusiDate(o.getServiceDate());
			sd.setCreatedTime(new Date());
			sd.setStartTime(o.getStartTime());
			sd.setEndTime(Integer.parseInt(o.getStartTime()) + idruation + "");
			sd.setOrderId(o.getId());
			sd.setState(Constants.STATE_A);
			sd.setAyiId(wid);
			scList.add(sd);
		}
		return scList;
	}

	public List getScheduleList4Month(Order o, List mList) {
		List resList = new ArrayList();

		Date startD = DateUtil.strToDate(o.getServiceDate());
		Calendar startC = Calendar.getInstance();
		startC.setTime(startD);

		int drationDay = Integer.parseInt(o.getDurationMonth()) * 30;

		Calendar endC = Calendar.getInstance();
		endC.setTime(startD);
		endC.add(Calendar.DATE, drationDay);
		for (int i = 0; i < mList.size(); i++) {
			Map mm = (Map) mList.get(i);
			Long wid = Long.parseLong((String) mm.get("aid"));
			String duration = (String) (mm.get("druation"));
			int idruation = Integer.parseInt(duration);
			while (!(endC.before(startC))) {
				startC.add(Calendar.DATE, 1);
				String w = "" + (startC.get(Calendar.DAY_OF_WEEK) - 1);
				if (o.getRepeatInWeek().contains(w)) {
					String d = DateUtil.date2Str(startC.getTime());
					System.out.println(d);
					Schedule sd = new Schedule();
					sd.setBusiDate(d);
					sd.setCreatedTime(new Date());
					sd.setStartTime(o.getStartTime());
					sd.setEndTime(Integer.parseInt(o.getStartTime()) + idruation + "");
					sd.setOrderId(o.getId());
					sd.setState(Constants.STATE_A);
					sd.setAyiId(wid);
					resList.add(sd);
				}
			}
		}

		return resList;
	}

	public List getScheduleList4Month(Order o) {
		List resList = new ArrayList();

		Date startD = DateUtil.strToDate(o.getServiceDate());
		Calendar startC = Calendar.getInstance();
		startC.setTime(startD);

		int drationDay = Integer.parseInt(o.getDurationMonth()) * 30;

		Calendar endC = Calendar.getInstance();
		endC.setTime(startD);
		endC.add(Calendar.DATE, drationDay);

		while (!(endC.before(startC))) {
			startC.add(Calendar.DATE, 1);
			String w = "" + (startC.get(Calendar.DAY_OF_WEEK) - 1);
			if (o.getRepeatInWeek().contains(w)) {
				String d = DateUtil.date2Str(startC.getTime());
				System.out.println(d);
				Schedule sd = new Schedule();
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
		if (StringUtil.isBlank(o.getServiceType()))
			return -1000d;
		double res = -10000d;
		if (Constants.SERVICE_TYPE_CC.equals(o.getServiceType())) {// 普通宝洁
			double dprice = Double
					.parseDouble(configRepository.findByConfigCode(Constants.CONFIG_PTBJDJ).getConfigValue());
			int iduration = Integer.parseInt(o.getDuration());
			double cleanToolsfee = 0.0d;
			if (Constants.YES.equals(o.getIsProviceCleanTools())) {
				cleanToolsfee = Double.parseDouble(
						configRepository.findByConfigCode(Constants.CONFIG_CLEAN_TOOLS_FEE4PTBJ).getConfigValue());
				o.setToolFeeClean(cleanToolsfee);
			}
			if (Constants.CYCLE_TYPE_SG.equals(o.getCycleType())) {
				res = iduration * dprice + cleanToolsfee;
			} else {
				int times = getScheduleList4Month(o).size();
				res = iduration * dprice * times + cleanToolsfee;
			}
		} else if (Constants.SERVICE_TYPE_DBJ.equals(o.getServiceType())) {// dabaojie
			double dprice = Double
					.parseDouble(configRepository.findByConfigCode(Constants.CONFIG_DBJDJ).getConfigValue());
			int area = Integer.parseInt(o.getArea());
			double cleanToolsfee = 0.0d;
			if (Constants.YES.equals(o.getIsProviceCleanTools())) {
				cleanToolsfee = Double.parseDouble(
						configRepository.findByConfigCode(Constants.CONFIG_CLEAN_TOOLS_FEE4DBJ).getConfigValue());
				o.setToolFeeClean(cleanToolsfee);
			}
			if (Constants.CYCLE_TYPE_SG.equals(o.getCycleType())) {
				res = area * dprice + cleanToolsfee;
			} else {
				int times = getScheduleList4Month(o).size();
				res = area * dprice * times + cleanToolsfee;
			}
		} else if (Constants.SERVICE_TYPE_CBL.equals(o.getServiceType())) {// cbl
			double cleanToolsfee = 0.0d;
			if (Constants.YES.equals(o.getIsProviceCleanTools())) {
				cleanToolsfee = Double.parseDouble(
						configRepository.findByConfigCode(Constants.CONFIG_CLEAN_TOOLS_FEE4CBL).getConfigValue());
				o.setToolFeeClean(cleanToolsfee);
			}
			double cytdprice = Double
					.parseDouble(configRepository.findByConfigCode(Constants.CONFIG_CYTDJ).getConfigValue());
			int iytCount = Integer.parseInt(o.getBalconyCount());
			double cncdprice = Double
					.parseDouble(configRepository.findByConfigCode(Constants.CONFIG_CNCDJ).getConfigValue());
			int incCount = Integer.parseInt(o.getWindowCount());
			res = cytdprice * iytCount + cncdprice * incCount + cleanToolsfee;
		} else if (Constants.SERVICE_TYPE_KH.equals(o.getServiceType())) {
			double dprice = Double
					.parseDouble(configRepository.findByConfigCode(Constants.CONFIG_KHDJ).getConfigValue());
			int area = Integer.parseInt(o.getArea());
			double cleanToolsfee = 0.0d;
			if (Constants.YES.equals(o.getIsProviceCleanTools())) {
				cleanToolsfee = Double.parseDouble(
						configRepository.findByConfigCode(Constants.CONFIG_CLEAN_TOOLS_FEE4KH).getConfigValue());
				o.setToolFeeClean(cleanToolsfee);
			}
			res = area * dprice + cleanToolsfee;
		}
		return res;
	}

	public Order getConfirmOrder(Order o) {
		double totalFee = getTotalPrice(o);
		o.setTotalFee(totalFee);
		String durationText = "";
		if (!StringUtil.isBlank(o.getStartTime())) {
			Integer iStartTime = Integer.parseInt(o.getStartTime());
			durationText = iStartTime + "";
			if (!StringUtil.isBlank(o.getEndTime())) {
				Integer iEndTime = Integer.parseInt(o.getEndTime());
				durationText = durationText + ":" + iEndTime;
			}
		}
		o.setDurationText(durationText);
		return o;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Order saveOrder(Order order) {
		Order o = orderRepository.save(order);
		if (Constants.SERVICE_TYPE_CC.equals(o.getServiceType())) {
			saveSchedule(o);
		}
		return o;
	}

	@Transactional(propagation = Propagation.NESTED)
	public void saveSchedule(Order o) {
		if (Constants.CYCLE_TYPE_BY.equals(o.getCycleType())) {
			List scheduleList = getScheduleList4Month(o);
			scheduleRepository.save(scheduleList);
		} else {
			Schedule sd = new Schedule();
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

	// public static void main(String arg[]) {
	// //new ClientServiceImpl().getScheduleList4Month("2015-11-11", "3", "3");
	// String info = null;
	// try{
	// String content="质控消息:患者大是病历书写不合格,请查看!";
	// HttpClient httpclient = new HttpClient();
	// PostMethod post = new
	// PostMethod("http://sms.api.ums86.com:8899/sms/Api/Send.do");//
	// post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"gbk");
	// post.addParameter("SpCode", "217289");
	// post.addParameter("LoginName", "tj_xfjy");
	// post.addParameter("Password", "helloWORLD123");
	// post.addParameter("MessageContent", content);
	// post.addParameter("UserNumber", "18611298927");
	// post.addParameter("SerialNumber", "12345678901234567890");
	// post.addParameter("ScheduleTime", "");
	// post.addParameter("ExtendAccessNum", "");
	// post.addParameter("f", "1");
	// httpclient.executeMethod(post);
	// info = new String(post.getResponseBody(),"gbk");
	// System.out.println(info);
	// }catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public Map getOrderMap(String openId) {
		Map resMap = new HashMap();
		List<Order> orderList = getOrderList(openId, null);

		List unFinishedList = new ArrayList();
		List finishedList = new ArrayList();

		if (orderList != null) {
			for (int i = 0; i < orderList.size(); i++) {
				Order o = orderList.get(i);
				if (Constants.ORDER_STATE_FINISH.equals(o.getState())) {
					finishedList.add(o);
				} else {
					unFinishedList.add(o);
				}
			}
		}
		resMap.put("finishedOrders", finishedList);
		resMap.put("unFinishedOrders", unFinishedList);
		return resMap;
	}

	public Order fillOrder(Order o) {
		if (Constants.SERVICE_TYPE_CC.equals(o.getServiceType())) {
			o.setServiceTypeText("普通保洁");
		} else if (Constants.SERVICE_TYPE_DBJ.equals(o.getServiceType())) {
			o.setServiceTypeText("大保洁");
		} else if (Constants.SERVICE_TYPE_CBL.equals(o.getServiceType())) {
			o.setServiceTypeText("擦玻璃");
		} else if (Constants.SERVICE_TYPE_KH.equals(o.getServiceType())) {
			o.setServiceTypeText("开荒");
		}

		if (Constants.CYCLE_TYPE_BY.equals(o.getCycleType())) {
			o.setCycleTypeText("包月");
		} else {
			o.setCycleTypeText("零工");
		}
		if (Constants.ORDER_PAY_MODE_ONLY_WECHAT.equals(o.getPayMode())) {
			o.setPayModeText("微信");
		} else if(Constants.ORDER_PAY_MODE_ONLY_YUE.equals(o.getPayMode())){
			o.setPayModeText("余额");
		}else if(Constants.ORDER_PAY_MODE_ONLY_COUPON.equals(o.getPayMode())){
			o.setPayModeText("优惠券");
		}else if(Constants.ORDER_PAY_MODE_WECHAT_COUPON.equals(o.getPayMode())){
			o.setPayModeText("微信/优惠券");
		}else if(Constants.ORDER_PAY_MODE_WECHAT_YUE.equals(o.getPayMode())){
			o.setPayModeText("微信/余额");
		}

		if (Constants.ORDER_STATE_UNPAY.equals(o.getState())) {
			o.setStateText("未支付");
		} else if (Constants.ORDER_STATE_PAYED.equals(o.getState())) {
			o.setStateText("已支付");
		} else if (Constants.ORDER_STATE_CONFIRMED.equals(o.getState())) {
			o.setStateText("已确认");
		} else if (Constants.ORDER_STATE_FINISH.equals(o.getState())) {
			o.setStateText("已完成");
		}
		return o;
	}

	// 避免方法重写，让按照主键查询和openId查询共用一个方法体
	public List<Order> getOrderList(String openId, Long oid) {
		Map parmMap = new HashMap();
		parmMap.put("openId", openId);

		List<Order> orderList = new ArrayList();
		if (!StringUtil.isBlank(openId)) {
			orderList = orderRepository.findByOpenId(openId);
		} else {
			orderList = orderRepository.findById(oid);
		}

		if (orderList != null) {
			for (int i = 0; i < orderList.size(); i++) {
				Order o = orderList.get(i);
				o = fillOrder(o);
			}
		}
		return orderList;
	}

	public Map deposit(String openId, double amount) {

		Map resMap = new HashMap();
		try {

			DepositLog ds = new DepositLog();
			ds.setDepositAmount(amount);
			// ds.setBalance(sum.getFee()+amount);
			ds.setOpenId(openId);
			ds.setState(Constants.STATE_P);

			// int aamount=(int)amount*100;
			int aamount = 1;
			String outTradeNo = RandomStringGenerator.getRandomStringByLength(18);
			String spBillCreateIP = "127.0.0.1";
			String timeStart = DateUtil.Time2Str(new Date(), DateUtil.format1);
			String timeExpire = DateUtil.getDiffDate(1);
			ds.setOutTradeNo(outTradeNo);

			depositLogRepository.save(ds);

			ScanPayReqData scanPayReqData = new ScanPayReqData(outTradeNo, aamount, spBillCreateIP, timeStart,
					timeExpire, openId);

			String res = new ScanPayService().request(scanPayReqData);
			Map rm = XMLParser.getMapFromXML(res);
			System.out.println("rmmmmmmmmmmmm=" + rm);
			if (rm.containsKey("prepay_id")) {
				resMap.put("resultCode", "S");

				String prepay_id = (String) rm.get("prepay_id");
				SortedMap pm = new TreeMap();
				resMap.put("prepay_id", prepay_id);

				pm.put("appId", Configure.appID);
				String tmp = DateUtil.getTimeStamp();
				pm.put("timeStamp", tmp);
				String nonceStr = RandomStringGenerator.getRandomStringByLength(32);
				pm.put("nonceStr", nonceStr);
				pm.put("package", "prepay_id=" + prepay_id);
				pm.put("signType", "MD5");
				String sign = Signature.getSign(pm);
				pm.put("paySign", sign);

				resMap.put("parm", pm);
			} else {
				resMap.put("resultCode", "E");
			}
		} catch (IllegalAccessException e) {
			resMap.put("resultCode", "E");
			e.printStackTrace();
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
		return resMap;
	}

	public Map personalCenter(String openId) {
		Map resMap = new HashMap();
		ClientUser cu = clientUserRepository.findByOpenId(openId);
		DepositSummary ds = depositSummaryRepository.findByOpenId(openId);
		if (ds == null) {
			ds = new DepositSummary();
			ds.setFee(0d);
		}

		// Map parmMap=new HashMap();
		// parmMap.put("openId", openId);
		// List addressList=userAddressMapper.findAddressByUser(parmMap);

		resMap.put("clientUser", cu);
		resMap.put("depositSummary", ds);
		return resMap;
	}

	public Order getOrder(Long id) {
		List orderList = this.getOrderList(null, id);
		if (orderList == null || orderList.size() == 0) {
			return new Order();
		} else {
			return (Order) orderList.get(0);
		}
	}

	public ClientUser saveClientUser(ClientUser cu) {
		 cu=clientUserRepository.save(cu);
		 return cu;
	}

	public ClientUser getClientUser(String openId) {
		return clientUserRepository.findByOpenId(openId);
	}

	public Map createOrder(Order o, Map paraMap) {
		Map resMap = new HashMap();
		resMap.put("resultCode", "S");
		Order order = new Order();
		String outTradeNo = "";
		if (StringUtil.isBlank(o.getOutTradeNo())) { // 第一次支付，需要保存
			order = getConfirmOrder(o);
			order.setState(Constants.ORDER_STATE_UNPAY);

			outTradeNo = RandomStringGenerator.getRandomStringByLength(32);
			order.setOutTradeNo(outTradeNo);
			order = saveOrder(order);
		} else {// 从未完成订单中，不需要重新创建订单
			outTradeNo = o.getOutTradeNo();
			order = orderRepository.findByOutTradeNo(outTradeNo);
			// 删除余额日志，避免重复创建
			DepositLog dl = depositLogRepository.findByOutTradeNo(outTradeNo);
			if (dl != null) {
				depositLogRepository.delete(dl);
			}
		}

		int totalFee = 1;
		// int totalFee=order.getTotalFee().intValue()*100;
		// int totalFee=5;
		// 判断是否用了余额或优惠券，如果是，减去余额
		String payMode = order.getPayMode();
		boolean goWechat = true;// 是否还需要走微信支付
		double fee = 0;
		int finalPay4Wechat=0;//抛去 余额，优惠券 ，最后需要支付的金额
		String finalPayMode=Constants.ORDER_PAY_MODE_ONLY_WECHAT;
		if (Constants.ORDER_PAY_MODE_ONLY_YUE.equals(payMode)) {// 如果使用了余额,取这个人的余额
			DepositSummary ds = getBalance(o.getOpenId());
			if (ds != null) {
				fee = ds.getFee();
				// fee=0.02d;
				if (fee * 100 < totalFee) { // 余额不足，还是需要走微信接口
					System.out.println("余额不足，走微信");
					finalPay4Wechat = (int) (totalFee - fee * 100);
					goWechat = true;
					finalPayMode=Constants.ORDER_PAY_MODE_WECHAT_YUE;
				} else {
					goWechat = false;
					finalPayMode=Constants.ORDER_PAY_MODE_ONLY_YUE;
				}
			}
		}else if(Constants.ORDER_PAY_MODE_ONLY_COUPON.equals(payMode)) {//如果使用了优惠券
			//取优惠券的id
			Long cid=order.getCouponId();
			Coupon cp=couponRepository.findOne(cid);
			fee=cp.getCash();
			if (fee * 100 < totalFee) { // 余额不足，还是需要走微信接口
				System.out.println("优惠券不足，走微信");
				finalPay4Wechat = (int) (totalFee - fee * 100);
				goWechat = true;
				finalPayMode=Constants.ORDER_PAY_MODE_WECHAT_COUPON;
			} else {
				goWechat = false;
				finalPayMode=Constants.ORDER_PAY_MODE_ONLY_COUPON;
			}
		}
		try {
			if (goWechat == true) {
				if (finalPayMode.equals(Constants.ORDER_PAY_MODE_WECHAT_YUE)) {//如果威信和余额同时使用
					DepositLog dl = new DepositLog();
					dl.setDepositAmount(0 - fee);
					dl.setOpenId(order.getOpenId());
					dl.setOrderId(order.getOrderId());
					dl.setOutTradeNo(order.getOutTradeNo());
					dl.setState(Constants.STATE_P);//在付款通知方法中修改成A
					depositLogRepository.save(dl);
				}else if(finalPayMode.equals(Constants.ORDER_PAY_MODE_WECHAT_COUPON)) {//如果威信和优惠券同时使用，不做处理，在付款通知方法中做处理
					
				}

				String timeStart = DateUtil.Time2Str(new Date(), DateUtil.format1);
				String timeExpire = DateUtil.getDiffDate(1);
				// String openId="o74xjw9vCvp6Wxa5zPEPu4ghP8gA";
				String openId = (String) paraMap.get("openId");
				String spBillCreateIP = (String) paraMap.get("spBillCreateIP");

				ScanPayReqData scanPayReqData = new ScanPayReqData(outTradeNo, finalPay4Wechat, spBillCreateIP, timeStart,
						timeExpire, openId);
				String res = new ScanPayService().request(scanPayReqData);
				Map rm = XMLParser.getMapFromXML(res);
				if (rm.containsKey("prepay_id")) {
					String prepay_id = (String) rm.get("prepay_id");
					SortedMap pm = new TreeMap();
					resMap.put("prepay_id", prepay_id);

					pm.put("appId", Configure.appID);
					String tmp = DateUtil.getTimeStamp();
					pm.put("timeStamp", tmp);
					String nonceStr = RandomStringGenerator.getRandomStringByLength(32);
					pm.put("nonceStr", nonceStr);
					pm.put("package", "prepay_id=" + prepay_id);
					pm.put("signType", "MD5");
					String sign = Signature.getSign(pm);
					pm.put("paySign", sign);

					resMap.put("parm", pm);

					System.out.println("parmssssssssss is =" + pm);
				} else {
					resMap.put("resultCode", "E");
				}
				System.out.println("res======" + res);
			} else {//如果不走威信
				if(Constants.ORDER_PAY_MODE_ONLY_YUE.equals(finalPayMode)) {//只走余额
					System.out.println("只走了余额！！！！！");
					DepositLog dl = new DepositLog();
					dl.setDepositAmount(0d - totalFee / 100);
					dl.setOpenId(order.getOpenId());
					dl.setOrderId(String.valueOf(order.getId()));
					dl.setOutTradeNo(order.getOutTradeNo());
					dl.setState(Constants.STATE_A);
					depositLogRepository.save(dl);
					DepositSummary ds = depositSummaryRepository.findByOpenId(order.getOpenId());
					ds.setFee(ds.getFee() - totalFee / 100);
					depositSummaryRepository.save(ds);
				}else if(Constants.ORDER_PAY_MODE_ONLY_COUPON.equals(finalPayMode)) {//只走优惠券
					System.out.println("只走了优惠券！！！！");
					Long cid=order.getCouponId();
					Coupon c=couponRepository.findOne(cid);
					c.setState(Constants.STATE_P);
					couponRepository.save(c);
				}
				order.setState(Constants.ORDER_STATE_PAYED);
				order.setPayMode(finalPayMode);
				order.setPayTime(new Date());
				orderRepository.save(order);
			}
			resMap.put("payMode", finalPayMode);
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
		System.out.println("resMap=======" + resMap);
		return resMap;
	}
	
	private double getMockTotalFee(double totalFee) {
		
	}

	public void notifyOrder(String outTradeNo, String orderId) {
		if (outTradeNo.length() == 32) {// 消费
			Order order = orderRepository.findByOutTradeNo(outTradeNo);
			if (order != null) {
				order.setOrderId(orderId);
				order.setState(Constants.ORDER_STATE_PAYED);
				order.setPayTime(new Date());
				orderRepository.save(order);

				if ("CC".equals(order.getServiceType())) {
					List olist = orderRepository.getCleanOrderWithAddressInfo(outTradeNo);
					if (olist != null && olist.size() > 0) {
						Object obj[] = (Object[]) olist.get(0);
						Order o = (Order) obj[0];
						String startTime = o.getStartTime();
						String endTime = o.getEndTime();
						String serviceDate = o.getServiceDate();
						UserAddress ua = (UserAddress) obj[1];
						Long areaId = ua.getAreaId();
						List ayiList = workerRepository.findWorkerAndOpenIdInArea(areaId);
						List confictList = new ArrayList();
						for (int k = 0; k < ayiList.size(); k++) {
							Object ob[] = (Object[]) ayiList.get(k);
							Worker w = (Worker) ob[0];
							List sl = scheduleRepository.findByAyiIdAndBusiDateAndState(w.getId(), serviceDate, "A");
							if (sl != null) {
								for (int l = 0; l < sl.size(); l++) {
									Schedule s = (Schedule) sl.get(l);
									String sTime = s.getStartTime();
									String eTime = s.getEndTime();
									if ((int) Integer.parseInt(startTime) < (int) Integer.parseInt(eTime)
											&& (int) Integer.parseInt(endTime) > (int) Integer.parseInt(sTime)) {
										confictList.add(ob);
										break;
									}
								}
							}
						}

						ayiList.removeAll(confictList);
						for (int k = 0; k < ayiList.size(); k++) {
							Object ob[] = (Object[]) ayiList.get(k);
							ClientUser w = (ClientUser) ob[1];
							String loginurl = "http://weixin.tjxfjz.com/xfjy/client/workerTask.html";
							String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Configure.appID
									+ "&redirect_uri=" + loginurl
									+ "&response_type=code&scope=snsapi_base&state=1#wechat_redirect";

							MsgUtil.sendTemplateMsg(Constants.MSG_KF_TEMPLATE_ID, w.getOpenId(), url, "您有新的任务",
									order.getFullAddress(), "待办", "点击查看详情");
						}
					}
				}
				// 处理余额信息，用于一半用了微信，一半用了余额
				DepositLog dl = depositLogRepository.findByOutTradeNo(outTradeNo);
				if (dl != null) {
					System.out.println("发现使用了余额");
					dl.setState(Constants.STATE_A);
					String openId = dl.getOpenId();
					double amount = dl.getDepositAmount();
					DepositSummary ds = depositSummaryRepository.findByOpenId(openId);
					ds.setFee(ds.getFee() + amount); // 加上一个负值
					depositLogRepository.save(dl);
					depositSummaryRepository.save(ds);
				}

			}
		} else {// 充值
			DepositLog log = depositLogRepository.findByOutTradeNo(outTradeNo);
			if (log != null) {
				log.setOrderId(orderId);
				log.setState(Constants.STATE_A);
				log.setPayTime(new Date());
				depositLogRepository.save(log);

				String openId = log.getOpenId();
				double amount = log.getDepositAmount();

				DepositSummary sum = depositSummaryRepository.findByOpenId(openId);
				if (sum == null) {
					sum = new DepositSummary();
					sum.setFee(amount);
					sum.setOpenId(openId);
					sum = depositSummaryRepository.save(sum);
				} else {
					sum.setFee(sum.getFee() + amount);
					sum.setOpenId(openId);
					sum = depositSummaryRepository.save(sum);
				}
			}

		}

	}

	public static String createWeChatMenu() {
		Map mainMap = new HashMap();
		List menuList = new ArrayList();

		Map item1 = new HashMap();
		item1.put("type", "view");
		item1.put("name", "约工");
		item1.put("url",
				"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxce20c658c1eb222f&redirect_uri=http://weixin.tjxfjz.com/xfjy/client/login.html&response_type=code&scope=snsapi_userinfo&state=1&connect_redirect=1#wechat_redirect");

		Map item2 = new HashMap();
		item2.put("type", "view");
		item2.put("name", "帮助");
		item2.put("url", "http://www.baidu.com");

		menuList.add(item1);
		menuList.add(item2);

		mainMap.put("button", menuList);

		String json = com.alibaba.fastjson.JSONObject.toJSONString(mainMap);

		System.out.println("json is====" + json);
		return json;
	}

	/**
	 * 
	 * @return
	 */
	public DepositSummary getBalance(String openId) {
		DepositSummary ds = depositSummaryRepository.findByOpenId(openId);
		return ds;
	}

	public Map getWorkerTask(String openId, String serviceDate) {
		Map resMap = new HashMap();
		System.out.println("ppppppppppppppppppp" + openId);
		ClientUser cu = clientUserRepository.findByOpenId(openId);
		String phone = cu.getMobileNo();
		Worker worker = workerRepository.findByPhone(phone);
		Long areaId = worker.getAreaId();
		List waitedOrderList = new ArrayList();
		List objList = orderRepository.findByAreaIdAndStateAndServiceType("P", "CC", areaId);
		if (waitedOrderList != null) {
			for (int i = 0; i < objList.size(); i++) {
				Object obj[] = (Object[]) objList.get(i);

				Order o = (Order) obj[0];
				o = fillOrder(o);
				waitedOrderList.add(o);
			}
		}

		resMap.put("waitOrderList", waitedOrderList);// 需要抢的

		List todoOrderList = new ArrayList();
		if (StringUtil.isBlank(serviceDate)) {
			todoOrderList = orderRepository.findByWorkerIdAndState(worker.getId());
		} else {
			todoOrderList = orderRepository.findByWorkerIdAndState(worker.getId(), serviceDate);
		}

		for (int i = 0; i < todoOrderList.size(); i++) {
			Order o = (Order) todoOrderList.get(i);
			o = fillOrder(o);
		}
		resMap.put("todoOrderList", todoOrderList);// 需要做的

		return resMap;

	}

	public synchronized String fightOrder(Long oid, String openId) {
		String res = "S";
		Order o = getOrder(oid);
		if (o.getState().equals(Constants.ORDER_STATE_CONFIRMED)) {
			res = "L";
			return res;
		}

		o.setState(Constants.ORDER_STATE_CONFIRMED);
		orderRepository.save(o);
		// 插入日程
		ClientUser cu = clientUserRepository.findByOpenId(openId);
		Worker w = workerRepository.findByPhone(cu.getMobileNo());
		OrderWorker ow = new OrderWorker();
		ow.setWorkerId(w.getId());
		ow.setOrderId(oid);
		orderWorkerRepository.save(ow);
		// 修改时间表
		List scheduleList = scheduleRepository.findByOrderId(oid);
		for (int i = 0; i < scheduleList.size(); i++) {
			Schedule s = (Schedule) scheduleList.get(i);
			s.setAyiId(w.getId());
			s.setState(Constants.STATE_A);
			scheduleRepository.save(s);
		}

		return res;
	}
	
	
	public ClientUser findClientUserByMobileNo(String mobileNo) {
		return clientUserRepository.findByMobileNo(mobileNo);
	}

	public static void main(String args[]) {
		com.alibaba.fastjson.JSONObject tokenJson = WeixinUtil.httpRequest(Constants.URL_GET_TOKEN, "GET", null);
		String accessToken = tokenJson.getString("access_token");
		com.alibaba.fastjson.JSONObject res = WeixinUtil.httpRequest(Constants.URL_CREATE_MENU + accessToken, "POST",
				createWeChatMenu());
		System.out.println("res===" + res);
	}
	
	// 注册的时候，送三张优惠券，每到一个月，过期一张
	public void saveCoupon4Register(long uid) {
		for (int i = 0; i < 3; i++) {
			Coupon c = new Coupon();
			c.setCash(20.0d);
			c.setState(Constants.STATE_A);
			c.setPromotionId(1l);
			c.setType(Constants.COUPON_TYPE_CASH);
			c.setUid(uid);
			c.setExpireDate(DateUtil.strToDate(DateUtil.getDiffDate(new Date(),
					(i + 1) * 30)));
			couponRepository.save(c);
		}
	}

}
