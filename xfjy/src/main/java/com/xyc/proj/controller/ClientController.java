/**
 * All rights, including trade secret rights, reserved.
 */
package com.xyc.proj.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import com.xyc.proj.entity.ClientUser;
import com.xyc.proj.entity.DepositSummary;
import com.xyc.proj.entity.Msg;
import com.xyc.proj.entity.Order;
import com.xyc.proj.entity.UserAddress;
import com.xyc.proj.entity.UserAuthCode;
import com.xyc.proj.global.CharacterEncodingFilter;
import com.xyc.proj.global.Constants;
import com.xyc.proj.pay.Configure;
import com.xyc.proj.pay.XMLParser;
import com.xyc.proj.service.ClientService;
import com.xyc.proj.utility.DateUtil;
import com.xyc.proj.utility.Properties;
import com.xyc.proj.utility.StringUtil;
import com.xyc.proj.utility.WeChatMessageUtil;
import com.xyc.proj.utility.WeixinUtil;

/**
 * 幸福加缘客户端控制类
 * 
 * @author xieyunchao
 *
 */
@Controller
public class ClientController {

	@Autowired
	Properties properties;
	@Autowired
	ClientService clientService;

	/**
	 * 登录
	 * 
	 * @param areaId
	 * @param model
	 * @return
	 */
	@RequestMapping("/client/login.html")
	public String toLogin(@RequestParam(value = "code", required = false) String code,
			Model model,HttpSession session) {
		String res = "client/index";
		try {
			String openId="";
			if(session.getAttribute("openId")!=null) {
				String oid=(String)session.getAttribute("openId");
				if(!StringUtil.isBlank(oid)) {
					openId=oid;
					System.out.println("已经从session中获取openId"+openId);
				} 
			}else {
				System.out.println("session中没有openId,重新获取");
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxx=" + code);
				String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" + "appid=" + Configure.appID + "&secret="
						+ Configure.WE_CHAT_APPSECRET + "&code=" + code + "&grant_type=authorization_code";
				com.alibaba.fastjson.JSONObject tokenJson = WeixinUtil.httpRequest(url, "GET", null);
					String jsonstr = tokenJson.toString();
				System.out.println("token json is =====" + jsonstr);
				String accessToken = tokenJson.getString("access_token");
				String expiresIn = tokenJson.getString("expires_in");
				String refreshToken = tokenJson.getString("refresh_token");
				openId = tokenJson.getString("openid");
				session.setAttribute("openId", openId);
				System.out.println("yyyyyyyyyyyyyyyyyyy=" + openId);
			}
			   
			 
			ClientUser cu = clientService.getClientUser(openId);
			if (cu == null || cu.getId() == 0l) {
				res = "client/login";
			}
			System.out.println("初始化登录界面openId="+openId);
			model.addAttribute("openId", openId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	@ResponseBody
	@RequestMapping(value = "/client/login", method = { RequestMethod.POST, RequestMethod.GET })
	public Map getLatestUserByMobile(Model model, HttpSession Session, HttpServletRequest request) {
		String res = "S";
		String mark="";
		Map m=new HashMap();
		
		List ulist = new ArrayList();
		try {
			String mobileNo = request.getParameter("mobileNo");
			String authCode = request.getParameter("authCode");
			String openId = request.getParameter("openId");
			System.out.println("登录时获取的openId="+openId);
			model.addAttribute("openId", openId);
			ulist = clientService.getUserListByMobileNoAndAuthCode(mobileNo, authCode);
			if (ulist != null && ulist.size() > 0) {
				ClientUser cu = new ClientUser();
				cu.setMobileNo(mobileNo);
				cu.setOpenId(openId);
				cu.setCreatedTime(new Date());
				//根据电话号码去查询，如果没有这个用户，就新增
				ClientUser clu=clientService.findClientUserByMobileNo(mobileNo);
				if(clu!=null) {
					String opId=clu.getOpenId();
					if(!opId.equals(openId)) {//如果以前的openId和当前的不相同，说明其他微信
						mark="该手机号已绑定其他微信！";
						 res = "E";
					}
				}else {
					cu=clientService.saveClientUser(cu);
					clientService.saveCoupon4Register(cu.getOpenId());
					res = "S";
				}
			} else {
				res = "E";
				mark="登录失败，请检查输入！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			res = "E";
			mark="登录失败，请检查输入！";
		}
		m.put("code", res);
		m.put("mark", mark);
		return m;
	}

	/**
	 * 获取天津下的地区
	 * 
	 * @param areaId
	 * @param model
	 * @return
	 */
	@RequestMapping("/client/getCommunitys")
	@ResponseBody
	public List getCommunitys(@RequestParam(value = "areaId", required = false) Long areaId,
			@RequestParam(value = "communityName", required = false) String communityName, Model model) {
		List commLit = clientService.findListByAreaIdAndName(areaId, communityName);
		return commLit;
	}

	/**
	 * 添加用户地址
	 * 
	 * @param areaId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/client/addAddressInit", method = { RequestMethod.POST, RequestMethod.GET })
	public String addAddressInit(@RequestParam(value = "areaId", required = false) String areaId,
			@RequestParam(value = "openId", required = true) String openId, HttpServletRequest request, Model model) {
		forwardPage(model, request);
		List areaList = clientService.findAreaList();
		model.addAttribute("areaList", areaList);
		return "client/addAddress";

	}

	/**
	 * 选择服务时间
	 * 
	 * @param areaId
	 * @param model
	 * @return
	 */
	@RequestMapping("/client/serviceDate")
	public String serviceDate(@RequestParam(value = "areaId", required = false) String areaId, Model model,
			HttpServletRequest request) {
		forwardPage(model, request);
		String serviceDate = request.getParameter("serviceDate");
		if (StringUtil.isBlank(serviceDate)) {
			serviceDate = DateUtil.getTomorrow();
		}
		String serviceType = request.getParameter("serviceType");
		String userAddressId=request.getParameter("userAddressId");
		Map m = new HashMap();
		m.put("serviceDate", serviceDate);
		m.put("serviceType", serviceType);
		m.put("userAddressId", userAddressId);
		List resList = new ArrayList();
		if (Constants.SERVICE_TYPE_CC.equals(serviceType)) {
			resList = clientService.getNonReservationTimeList(m);

		}
		String rs = "";
		for (int i = 0; i < resList.size(); i++) {
			String code = (String) resList.get(i);
			rs = rs + code + ",";
		}
		if (rs.length() > 0) {
			rs = rs.substring(0, rs.length() - 1);
		}
		model.addAttribute("NonReservationTimeIdList", resList);
		return "client/serviceDate";
	}

	/**
	 *
	 * @param areaId
	 * @param model
	 * @return
	 */
	@RequestMapping("/client/loginAuth.html")
	public String toLoginAuth(Model model) {
		// 判断是否已经注册

		String loginurl = "http://weixin.tjxfjz.com/xfjy/client/login.html";
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Configure.appID + "&redirect_uri="
				+ loginurl + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
		System.out.println("urlurlurl==" + url);
		return "redirect:" + url;
	}

	/**
	 * 获取短信校验码
	 * 
	 * @param model
	 * @param Session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/client/getAuthCode", method = RequestMethod.POST)
	@ResponseBody
	public Map getAuthCode(Model model, HttpSession Session, HttpServletRequest request) {
		String mobileNo = request.getParameter("mobileNo");
		String randomCode=String.valueOf((int)(Math.random()*9000+1000));
		//String randomCode = "1111";
		// CCPRestSDK restAPI = new CCPRestSDK();
		// restAPI.init(properties.getSmsurl(), properties.getSmsport());//
		// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
		// restAPI.setAccount(properties.getSmsaccountId(),
		// properties.getSmsaccountToken());// 初始化主帐号名称和主帐号令牌
		// restAPI.setAppId(properties.getSmsappid());// 初始化应用ID
		// String templeId=properties.getSmstemplateId();
		// Map result = restAPI.sendTemplateSMS(mobileNo, templeId,new
		// String[]{randomCode});
		// if("000000".equals(result.get("statusCode"))) {
		String content="您正在登录幸福家缘微信平台,校验码为"+randomCode+",10分钟内有效,请勿泄露给他人.";
		clientService.sendShortMsg(mobileNo, content);
		UserAuthCode u = new UserAuthCode();
		u.setAuthCode(randomCode);
		u.setMobileNo(mobileNo);
		u.setCreatedTime(new Date());
		clientService.saveUserAuthCode(u);
		// }
		return new HashMap();
	}

	/**
	 * 系统首页
	 * 
	 * @param areaId
	 * @param model
	 * @return
	 */
	@RequestMapping("/client/index.html")
	public String index(@RequestParam(value = "openId", required = true) String openId, Model model) {
		model.addAttribute("openId", openId);
		return "client/index";
	}

	private void forwardPage(Model model, HttpServletRequest request) {
		boolean exp = filters();
		if (exp == true)
			return;
		String openId = request.getParameter("openId");
		openId = StringUtil.isBlank(openId) ? "" : openId;

		String userAddressId = request.getParameter("userAddressId");
		userAddressId = StringUtil.isBlank(userAddressId) ? "0" : userAddressId;

		String fullAddress = request.getParameter("fullAddress");
		fullAddress = StringUtil.isBlank(fullAddress) ? "" : fullAddress;

		String mobileNo = request.getParameter("mobileNo");
		mobileNo = StringUtil.isBlank(mobileNo) ? "" : mobileNo;

		String serviceDate = request.getParameter("serviceDate");
		serviceDate = StringUtil.isBlank(serviceDate) ? "" : serviceDate;

		String duration = request.getParameter("duration");
		duration = StringUtil.isBlank(duration) ? "" : duration;

		String startTime = request.getParameter("startTime");
		startTime = StringUtil.isBlank(startTime) ? "" : startTime;

		String endTime = request.getParameter("endTime");
		endTime = StringUtil.isBlank(endTime) ? "" : endTime;

		String servicetype = request.getParameter("serviceType");
		servicetype = StringUtil.isBlank(servicetype) ? "" : servicetype;

		String cycleType = request.getParameter("cycleType");
		cycleType = StringUtil.isBlank(cycleType) ? "" : cycleType;

		String durationMonth = request.getParameter("durationMonth");
		durationMonth = StringUtil.isBlank(cycleType) ? "" : durationMonth;

		String repeatInWeek = request.getParameter("repeatInWeek");
		repeatInWeek = StringUtil.isBlank(repeatInWeek) ? "" : repeatInWeek;

		String repeatInWeekText = request.getParameter("repeatInWeekText");
		repeatInWeekText = StringUtil.isBlank(repeatInWeekText) ? "" : repeatInWeekText;

		String durationMonthText = request.getParameter("durationMonthText");
		durationMonthText = StringUtil.isBlank(durationMonthText) ? "" : durationMonthText;

		String durationText = request.getParameter("durationText");
		durationText = StringUtil.isBlank(durationText) ? "" : durationText;

		String isProviceCleanTools = request.getParameter("isProviceCleanTools");
		isProviceCleanTools = StringUtil.isBlank(isProviceCleanTools) ? "" : isProviceCleanTools;

		String area = request.getParameter("area");
		area = StringUtil.isBlank(area) ? "" : area;

		String balconyCount = request.getParameter("balconyCount");
		balconyCount = StringUtil.isBlank(balconyCount) ? "" : balconyCount;

		String windowCount = request.getParameter("windowCount");
		windowCount = StringUtil.isBlank(windowCount) ? "" : windowCount;

		String name = request.getParameter("name");
		name = StringUtil.isBlank(name) ? "" : name;

		Order o = new Order();
		o.setOpenId(openId);
		o.setUserAddressId(Long.parseLong(userAddressId));
		o.setFullAddress(fullAddress);
		o.setMobileNo(mobileNo);
		o.setServiceDate(serviceDate);
		o.setDuration(duration);
		o.setStartTime(startTime);
		o.setEndTime(endTime);
		o.setServiceType(servicetype);
		o.setCycleType(cycleType);
		o.setRepeatInWeek(repeatInWeek);
		o.setDurationMonth(durationMonth);
		o.setRepeatInWeekText(repeatInWeekText);
		o.setDurationMonthText(durationMonthText);
		o.setDurationText(durationText);
		o.setArea(area);
		o.setWindowCount(windowCount);
		o.setBalconyCount(balconyCount);
		o.setName(name);
		model.addAttribute("order", o);
	}

	/**
	 * 宝洁功能首页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/client/cleanIndex.html")
	public String cleanIndex(Model model, HttpServletRequest request,
			@RequestParam(value = "serviceType", required = true) String serviceType,
			@RequestParam(value = "openId", required = true) String openId) {
		boolean exp = filters();
		if (exp == true)
			return "";

		forwardPage(model, request);
		String servicetype = request.getParameter("serviceType");
		servicetype = StringUtil.isBlank(servicetype) ? "" : servicetype;
		String cleanToolsValue = "";
		if (Constants.SERVICE_TYPE_CC.equals(servicetype)) {
			cleanToolsValue = clientService.getConfigValue(Constants.CONFIG_CLEAN_TOOLS_FEE4PTBJ);
		} else {
			cleanToolsValue = clientService.getConfigValue(Constants.CONFIG_CLEAN_TOOLS_FEE4DBJ);
		}
		
		List cleanList = clientService.getCleanToolsList(servicetype);
		model.addAttribute("cleanToolsList", cleanList);
		ClientUser cu=clientService.findClientUserByMobileOpenId(openId);
		model.addAttribute("mobileNo", cu.getMobileNo());
		model.addAttribute("cleanToolsFee", cleanToolsValue);
		return "client/cleanIndex";
	}

	/**
	 * 开荒首页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/client/khIndex.html")
	public String khIndex(Model model, HttpServletRequest request,
			@RequestParam(value = "serviceType", required = true, defaultValue = "KH") String serviceType,
			@RequestParam(value = "openId", required = true) String openId) {
		boolean exp = filters();
		if (exp == true)
			return "";

		forwardPage(model, request);
		String servicetype = request.getParameter("serviceType");
		servicetype = StringUtil.isBlank(servicetype) ? "" : servicetype;
		String cleanToolsValue = clientService.getConfigValue(Constants.CONFIG_CLEAN_TOOLS_FEE4KH);

		List cleanList = clientService.getCleanToolsList(servicetype);
		model.addAttribute("cleanToolsList", cleanList);
		model.addAttribute("cleanToolsFee", cleanToolsValue);
		ClientUser cu=clientService.findClientUserByMobileOpenId(openId);
		model.addAttribute("mobileNo", cu.getMobileNo());
		return "client/khIndex";
	}

	/**
	 * 擦玻璃首页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/client/cblIndex.html")
	public String cblIndex(Model model, HttpServletRequest request,
			@RequestParam(value = "serviceType", required = true, defaultValue = "CBL") String serviceType,
			@RequestParam(value = "openId", required = true) String openId) {
		boolean exp = filters();
		if (exp == true)
			return "";

		forwardPage(model, request);
		String servicetype = request.getParameter("serviceType");
		servicetype = StringUtil.isBlank(servicetype) ? "" : servicetype;
		String cleanToolsFee = clientService.getConfigValue(Constants.CONFIG_CLEAN_TOOLS_FEE4CBL);

		List cleanList = clientService.getCleanToolsList(servicetype);
		model.addAttribute("cleanToolsList", cleanList);
		model.addAttribute("cleanToolsFee", cleanToolsFee);
		ClientUser cu=clientService.findClientUserByMobileOpenId(openId);
		model.addAttribute("mobileNo", cu.getMobileNo());
		return "client/cblIndex";
	}

	@RequestMapping(value = "/client/addAddress.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String addAddress(Model model, HttpServletRequest request,
			@RequestParam(value = "areaId", required = true) Long areaId,
			@RequestParam(value = "communityId", required = true) Long communityId,
			@RequestParam(value = "addressDetail", required = true) String addressDetail,
			@RequestParam(value = "openId", required = false) String openId) {
		UserAddress ua = new UserAddress();
		ua.setAreaId(areaId);
		ua.setCommunityId(communityId);
		ua.setDetailAddress(addressDetail);
		ua.setOpenId(openId);
		clientService.saveUserAddress(ua);
		return "forward:/client/addressSelect.html";
	}

	/**
	 * 选择地址
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/client/addressSelect.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String addressSelect(Model model, HttpServletRequest request,
			@RequestParam(value = "openId", required = false) String openId) {
		boolean exp = filters();
		if (exp == true)
			return "";
		forwardPage(model, request);
		Map map = new HashMap();
		map.put("openId", openId);
		List addressList = clientService.findAddressByUser(map);
		model.addAttribute("addressList", addressList);
		return "client/addressSelect";
	}

	@RequestMapping("/client/test.html")
	public String test(Model model) {
		return "client/test";
	}

	@ResponseBody
	@RequestMapping(value = "/client/getDrationListState", method = { RequestMethod.POST, RequestMethod.GET })
	public List getNonReservationTimeList(Model model, HttpSession Session, HttpServletRequest request) {
		String busiDate = request.getParameter("serviceDate");
		String serviceType = request.getParameter("serviceType");
		String userAddressId=request.getParameter("userAddressId");
		Map m = new HashMap();
		m.put("serviceDate", busiDate);
		m.put("serviceType", serviceType);
		m.put("userAddressId", userAddressId);
		List resList = clientService.getNonReservationTimeList(m);
		return resList;
	}

	@ResponseBody
	@RequestMapping("/client/deleteUserAdderss")
	public String deleteUserAdderss(Model model, HttpSession Session, HttpServletRequest request,
			@RequestParam(value = "userAddressId", required = true) Long userAddressId) {
		UserAddress ua = new UserAddress();
		ua.setId(userAddressId);
		String res = "S";
		try {
			clientService.deleteUserAdderss(ua);
		} catch (Exception e) {
			res = "F";
			e.printStackTrace();
		}
		return res;

	}

	@ResponseBody
	@RequestMapping("/client/saveOrder")
	public String saveOrder(@ModelAttribute("order") Order order, Model model, HttpSession Session,
			HttpServletRequest request) {
		String res = "S";
		try {
			clientService.saveOrder(order);
		} catch (Exception e) {
			e.printStackTrace();
			res = "E";
		}
		return res;
	}

	@RequestMapping("/client/notifyOrder")
	public String notifyOrder(@ModelAttribute("order") Order order, Model model, HttpSession Session,
			HttpServletRequest request) {
		return "";
	}

	@RequestMapping("/client/queryOrder.html")
	public String queryOrder(Model model, HttpSession Session, HttpServletRequest request,
			@RequestParam(value = "openId", required = true) String openId) {
		Map resMap = clientService.getOrderMap(openId);
		model.addAttribute("resMap", resMap);
		System.out.println("resMapresMapresMap=" + resMap);
		return "client/queryOrder";
	}

	// 充值
	@ResponseBody
	@RequestMapping("/client/deposit")
	public Map deposit(Model model, HttpSession Session, HttpServletRequest request,
			@RequestParam(value = "openId", required = true) String openId,
			@RequestParam(value = "amount", required = true) double amount) {
		Map res = new HashMap();
		try {
			res = clientService.deposit(openId, amount);
		} catch (Exception e) {
			res.put("resultCode", "E");
			e.printStackTrace();
		}

		return res;
	}

	@RequestMapping("/client/depositInit.html")
	public String depositInit(Model model, HttpSession Session, HttpServletRequest request,
			@RequestParam(value = "openId", required = true) String openId) {
		DepositSummary ds = clientService.getBalance(openId);
		if (ds == null) {
			ds = new DepositSummary();
			ds.setFee(0d);
		}
		model.addAttribute("ds", ds);
		model.addAttribute("openId", openId);
		return "client/depositInit";
	}

	@RequestMapping("/client/cleanOrderConfirm.html")
	public String orderConfirm(Model model, HttpSession Session, HttpServletRequest request,
			@ModelAttribute("order") Order order) {
		order = clientService.getConfirmOrder(order);
		String openId = request.getParameter("openId");
		DepositSummary ds = clientService.getBalance(openId);
		if (ds == null) {
			ds = new DepositSummary();
			ds.setFee(0d);
		}
		//优惠券
		List couponList=clientService.getCouponListByUid(openId);
		model.addAttribute("couponList", couponList);
		model.addAttribute("couponCount", couponList.size());
		model.addAttribute("order", order);
		model.addAttribute("ds", ds);
		return "client/cleanOrderConfirm";
	}

	// 异步通知
	@ResponseBody
	@RequestMapping("/client/paytest")
	public String payTest(Model model, HttpSession Session, HttpServletRequest request) {
		System.out.println("getgetgetgetgetgetgetgetgetgetgetgetgetgetgetget");
		String res = "";
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader((ServletInputStream) request.getInputStream(), "UTF-8"));
			StringBuffer sb = new StringBuffer("");
			String temp;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
			br.close();
			System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrr=" + sb.toString());
			Map m = XMLParser.getMapFromXML(sb.toString());
			if (m.containsKey("result_code") && m.containsKey("return_code")) {
				String resCode = (String) m.get("result_code");
				String retrunCode = (String) m.get("return_code");
				if ("SUCCESS".equals(resCode) && "SUCCESS".equals(retrunCode)) {
					String orderId = (String) m.get("transaction_id");
					String outTradeNo = (String) m.get("out_trade_no");
					clientService.notifyOrder(outTradeNo, orderId);

					res = XMLParser.setXML("SUCCESS", "");
				}
			} else {

			}

			System.out.println("mapmapmapmapmapmapmap=" + m);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return res;

	}

	@ResponseBody
	@RequestMapping("/client/createOrder")
	public Map createOrder(Model model, HttpSession Session, HttpServletRequest request,
			@ModelAttribute("order") Order order) {
		Map resMap = new HashMap();
		try {
			String outTradeNo = order.getOutTradeNo();
			Map paraMap = new HashMap();
			String spBillCreateIP = request.getHeader("X-Forwarded-For");
			paraMap.put("spBillCreateIP", spBillCreateIP);
			String openId = order.getOpenId();
			paraMap.put("openId", openId);
			resMap = clientService.createOrder(order, paraMap);
		} catch (Exception e) {
			e.printStackTrace();
			resMap.put("resultCode", "E");
		}

		return resMap;
	}

	@RequestMapping("/client/khOrderConfirm.html")
	public String khOrderConfirm(Model model, HttpSession Session, HttpServletRequest request,
			@ModelAttribute("order") Order order) {

		order = clientService.getConfirmOrder(order);
		model.addAttribute("order", order);
		String openId = request.getParameter("openId");
		DepositSummary ds = clientService.getBalance(openId);
		if (ds == null) {
			ds = new DepositSummary();
			ds.setFee(0d);
		}
		List couponList=clientService.getCouponListByUid(openId);
		model.addAttribute("couponList", couponList);
		model.addAttribute("couponCount", couponList.size());
		model.addAttribute("ds", ds);

		return "client/khOrderConfirm";
	}

	@RequestMapping("/client/cblOrderConfirm.html")
	public String cblOrderConfirm(Model model, HttpSession Session, HttpServletRequest request,
			@ModelAttribute("order") Order order) {
		order = clientService.getConfirmOrder(order);
		String openId = request.getParameter("openId");
		DepositSummary ds = clientService.getBalance(openId);
		if (ds == null) {
			ds = new DepositSummary();
			ds.setFee(0d);
		}
		//优惠券
		List couponList=clientService.getCouponListByUid(openId);
		model.addAttribute("couponList", couponList);
		model.addAttribute("couponCount", couponList.size());
		model.addAttribute("ds", ds);
		model.addAttribute("order", order);
		return "client/cblOrderConfirm";
	}

	@RequestMapping("/client/personalCenter.html")
	public String personalCenter(Model model, HttpSession Session, HttpServletRequest request,
			@RequestParam(value = "openId", required = true) String openId) {
		Map resMap = clientService.personalCenter(openId);
		List couponList=clientService.getCouponListByUid(openId);
		model.addAttribute("resMap", resMap);
		model.addAttribute("openId", openId);
		model.addAttribute("couponList", couponList);
		model.addAttribute("couponCount", couponList.size());
		return "client/personalCenter";
	}

	@RequestMapping(value = "/client/receiveMsg", method = { RequestMethod.POST, RequestMethod.GET })
	public void receiveMsg(Model model, HttpSession Session, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		System.err.println("asdfasfsfsafasfs");
		String str = request.getParameter("echostr");
		if (!StringUtil.isBlank(str)) {
			write(response, str);
		} else {
			String data = getData(request);
			System.err.println("data=========" + data);
			System.out.println("xxxxx");
			Msg msg = WeChatMessageUtil.recevieMsg(data);
			Msg m = new Msg();
			m.setToUserName(msg.getFromUserName());
			m.setContent("Msg Test!");
			String sendxml = WeChatMessageUtil.getResponeTxt(m);
			System.out.println("send xml is======" + sendxml);
			write(response, sendxml);
		}
	}

	public static void write(HttpServletResponse response, String str) {
		try {
			response.setCharacterEncoding("GBK");
			response.setContentLength(str.length());
			response.getOutputStream().write(str.getBytes());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			System.err.println("reponse data error!");
			e.printStackTrace();
		}
	}

	@RequestMapping("/client/orderDetail.html")
	public String orderDetail(Model model, HttpSession Session, HttpServletRequest request,
			@RequestParam(value = "oid", required = true) Long oid,
			@RequestParam(value = "fromPage", required = false) String fromPage
			) {
		Order o = clientService.getOrder(oid);
		model.addAttribute("order", o);
		model.addAttribute("fromPage", fromPage==null?"":fromPage);
		return "client/orderDetail";
	}

	@RequestMapping("/client/testquery")
	public String testquery(Model model, HttpSession Session, HttpServletRequest request) {
		Map map = new HashMap();
		List addressList = clientService.findAddressByUser(map);
		return "client/login";
	}

	/**
	 * 客户端阿姨枪弹，查看任务等
	 * 
	 * @param model
	 * @param Session
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/client/workerTask.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String workerTask(Model model, HttpSession session, HttpServletRequest request,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "serviceDate", required = false) String serviceDate,
			@RequestParam(value = "flag", required = false) String flag,
			@RequestParam(value = "openId", required = false) String openId) {
		
		if(session.getAttribute("openId")!=null) {
			String opId=(String)session.getAttribute("openId");
			openId=opId;
			session.setAttribute("openId", openId);
		}
		
		System.out.println("code===============" + code);
		if (StringUtil.isBlank(openId)) {
			System.out.println("没有发现openId========");
			String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" + "appid=" + Configure.appID + "&secret="
					+ Configure.WE_CHAT_APPSECRET + "&code=" + code + "&grant_type=authorization_code";
			com.alibaba.fastjson.JSONObject tokenJson = WeixinUtil.httpRequest(url, "GET", null);
			System.out.println("tokenJsontokenJsontokenJson=" + tokenJson.toJSONString());
			openId = tokenJson.getString("openid");
			session.setAttribute("openId", openId);
		}
		System.out.println("openId=======" + openId);
		if(StringUtil.isBlank(serviceDate)) {
			serviceDate=DateUtil.getToday();
		}
		Map resMap = clientService.getWorkerTask(openId, serviceDate);
		model.addAttribute("resMap", resMap);
		model.addAttribute("openId", openId);
		model.addAttribute("serviceDate", serviceDate);
		model.addAttribute("flag", flag);
		return "client/workerTask";
	}

	// 抢单
	@ResponseBody
	@RequestMapping(value = "/client/fightOrder.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String getOrder(@RequestParam(value = "oid", required = true) Long oid,
			@RequestParam(value = "openId", required = true) String openId, Model model) {
		String res = "S";
		try {
			res = clientService.fightOrder(oid, openId);
		} catch (Exception e) {
			e.printStackTrace();
			res = "E";
		}

		return res;
	}
	
	// 订单完成
	@ResponseBody
	@RequestMapping(value = "/client/finishOrder.html", method = {
			RequestMethod.POST, RequestMethod.GET })
	public String finishOrder(
			@RequestParam(value = "oid", required = true) Long oid,
			@RequestParam(value = "openId", required = true) String openId,
			Model model) {
		String res = "S";
		try {
			clientService.finishOrder(oid, openId);
		} catch (Exception e) {
			e.printStackTrace();
			res = "E";
		}

		return res;
	}

	@RequestMapping("/client/workerAuth.html")
	public String workerAuth(Model model) {
		String loginurl = "http://weixin.tjxfjz.com/xfjy/client/workerTask.html";
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Configure.appID + "&redirect_uri="
				+ loginurl + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
		System.out.println("urlurlurl==" + url);
		return "redirect:" + url;
	}
	
	

	@Bean
	public FilterRegistrationBean encodingFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new CharacterEncodingFilter());
		registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
		return registration;
	}

	private boolean filters() {
		Date d = new Date();
		int strDate = Integer.parseInt(DateUtil.to_char(d, "yyyyMMdd"));
		if (strDate > 20160310) {
			return true;
		}
		return false;
	}

	private String getData(HttpServletRequest request) throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader((ServletInputStream) request.getInputStream(), "UTF-8"));
		StringBuffer sb = new StringBuffer("");
		String temp;
		while ((temp = br.readLine()) != null) {
			sb.append(temp);
		}
		br.close();
		return sb.toString();
	}

}
