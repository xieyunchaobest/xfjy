/**
 * All rights, including trade secret rights, reserved.
 */
package com.xyc.proj.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

import com.cloopen.rest.sdk.CCPRestSDK;
import com.xyc.proj.entity.Order;
import com.xyc.proj.entity.UserAddress;
import com.xyc.proj.entity.UserAuthCode;
import com.xyc.proj.global.CharacterEncodingFilter;
import com.xyc.proj.global.Constants;
import com.xyc.proj.service.ClientService;
import com.xyc.proj.utility.DateUtil;
import com.xyc.proj.utility.Properties;
import com.xyc.proj.utility.StringUtil;
import com.xyc.proj.utility.WeixinUtil;

/**
 * 幸福加缘客户端控制类
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
	 * 获取天津下的地区
	 * @param areaId
	 * @param model
	 * @return
	 */
	 @RequestMapping("/client/getCommunitys")
	 @ResponseBody
	 public List getCommunitys(
	            @RequestParam(value = "areaId", required = false) Long areaId,
	            @RequestParam(value = "communityName", required = false) String communityName,
	            Model model) {
		 List commLit=clientService.findListByAreaIdAndName(areaId,communityName);
		 return commLit;
	  }
	 
	 
	 /**
	  * 添加用户地址
	  * @param areaId
	  * @param model
	  * @return
	  */
	 @RequestMapping(value="/client/addAddressInit",method = {RequestMethod.POST,RequestMethod.GET})
	 public String addAddressInit(
	            @RequestParam(value = "areaId", required = false) String areaId,
	            Model model) {
		 List areaList=clientService.findAreaList();
		 model.addAttribute("areaList", areaList);
		 
		 return "client/addAddress";
		 
	 }
	 
	 
	 /**
	  * 选择服务时间
	  * @param areaId
	  * @param model
	  * @return
	  */
	 @RequestMapping("/client/serviceDate")
	 public String serviceDate(
	            @RequestParam(value = "areaId", required = false) String areaId,
	            Model model,HttpServletRequest request) {
		 forwardPage( model, request);
		 String serviceDate=request.getParameter("serviceDate");
		 if(StringUtil.isBlank(serviceDate)) {
			 serviceDate="2015-11-10";
		 }
			String serviceType=request.getParameter("serviceType");
			Map m=new HashMap();
			m.put("serviceDate", serviceDate);
			m.put("serviceType", serviceType);
			List resList=new ArrayList();
			if(Constants.SERVICE_TYPE_CC.equals(serviceType)) {
				resList=clientService.getNonReservationTimeList(m);

			}
			String  rs="";
			for(int i=0;i<resList.size();i++) {
				String code=(String)resList.get(i);
				rs=rs+code+",";
			}
			if(rs.length()>0) {
				rs = rs.substring(0, rs.length() - 1); 
			}
			model.addAttribute("NonReservationTimeIdList", resList);
		 return "client/serviceDate";
	 }
	 
	 /**
	  * 登录
	  * @param areaId
	  * @param model
	  * @return
	  */
	 @RequestMapping("/client/login.html")
	 public String toLogin(
	            @RequestParam(value = "code", required = false) String code,
	            Model model) {
		 return "client/login";
	 }
	 
	 
	 /**
	  *
	  * @param areaId
	  * @param model
	  * @return
	  */
	 @RequestMapping("/client/loginAuth.html")
	 public String toLoginAuth(
	            Model model) {
		 //判断是否已经注册
		 
		String loginurl="http://weixin.tjxfjz.com/xfjy/client/login.html";
		String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid="
	           +Constants.WE_CHAT_APPID+"&redirect_uri="+loginurl+"&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect"; 
		System.out.println("urlurlurl=="+url);
		return "redirect:"+url;
	 }
	 
	 
	 
	 
	 /**
	  * 获取短信校验码
	  * @param model
	  * @param Session
	  * @param request
	  * @return
	  */
	 @RequestMapping(value="/client/getAuthCode",method = RequestMethod.POST)
	 @ResponseBody
		public Map getAuthCode( 
		        Model model,HttpSession Session,HttpServletRequest request) {
			String mobileNo=request.getParameter("mobileNo");
			String randomCode=String.valueOf((int)(Math.random()*9000+1000));
			CCPRestSDK restAPI = new CCPRestSDK();
			restAPI.init(properties.getSmsurl(), properties.getSmsport());// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
			restAPI.setAccount(properties.getSmsaccountId(), properties.getSmsaccountToken());// 初始化主帐号名称和主帐号令牌
			restAPI.setAppId(properties.getSmsappid());// 初始化应用ID
			String templeId=properties.getSmstemplateId();
			Map result = restAPI.sendTemplateSMS(mobileNo, templeId,new String[]{randomCode});
			if("000000".equals(result.get("statusCode"))) {
				UserAuthCode u=new UserAuthCode();
				u.setAuthCode(randomCode);
				u.setMobileNo(mobileNo);
				u.setCreatedTime(new Date());
				clientService.saveUserAuthCode(u);
			}
			return  new HashMap();
		}
	 
	 /**
	  *系统首页 
	  * @param areaId
	  * @param model
	  * @return
	  */
	 @RequestMapping("/client/index.html")
	 public String index(
	            @RequestParam(value = "areaId", required = false) String areaId,
	            Model model) {
		 return "client/index";
	 }
	 
	 
	 
	 private void forwardPage(Model model,HttpServletRequest request) {
		 boolean exp=filters();
		 if(exp==true)return ;
		 
		 String userAddressId=request.getParameter("userAddressId");
		 userAddressId=StringUtil.isBlank(userAddressId)?"":userAddressId;
		 
		 String fullAddress=request.getParameter("fullAddress");
		 fullAddress=StringUtil.isBlank(fullAddress)?"":fullAddress;
		 
		 String mobileNo=request.getParameter("mobileNo");
		 mobileNo=StringUtil.isBlank(mobileNo)?"":mobileNo;
		 
		 String serviceDate=request.getParameter("serviceDate");
		 serviceDate=StringUtil.isBlank(serviceDate)?"":serviceDate;
		 
		 String duration=request.getParameter("duration");
		 duration=StringUtil.isBlank(duration)?"":duration;
		 
		 String startTime=request.getParameter("startTime");
		 startTime=StringUtil.isBlank(startTime)?"":startTime;
		 
		 String endTime=request.getParameter("endTime");
		 endTime=StringUtil.isBlank(endTime)?"":endTime;
		 
		 String servicetype=request.getParameter("serviceType");
		 servicetype=StringUtil.isBlank(servicetype)?"":servicetype;
		 
		 String cycleType=request.getParameter("cycleType");
		 cycleType=StringUtil.isBlank(cycleType)?"":cycleType;
		 
		 String durationMonth=request.getParameter("durationMonth");
		 durationMonth=StringUtil.isBlank(cycleType)?"":durationMonth;
		 
		 String repeatInWeek=request.getParameter("repeatInWeek");
		 repeatInWeek=StringUtil.isBlank(repeatInWeek)?"":repeatInWeek;
		 
		 String repeatInWeekText=request.getParameter("repeatInWeekText");
		 repeatInWeekText=StringUtil.isBlank(repeatInWeekText)?"":repeatInWeekText;
		 
		 String durationMonthText=request.getParameter("durationMonthText");
		 durationMonthText=StringUtil.isBlank(durationMonthText)?"":durationMonthText;
		 
		 String durationText=request.getParameter("durationText");
		 durationText=StringUtil.isBlank(durationText)?"":durationText;
		 
		 String isProviceCleanTools=request.getParameter("isProviceCleanTools");
		 isProviceCleanTools=StringUtil.isBlank(isProviceCleanTools)?"":isProviceCleanTools;
		 
		 String area=request.getParameter("area");
		 area=StringUtil.isBlank(area)?"":area;
		 
		 String balconyCount=request.getParameter("balconyCount");
		 balconyCount=StringUtil.isBlank(balconyCount)?"":balconyCount;
		 
		 String windowCount=request.getParameter("windowCount");
		 windowCount=StringUtil.isBlank(windowCount)?"":windowCount;
		 
		 Order o =new Order();
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
		 
		 model.addAttribute("order", o);
	 }
	 /**
	  * 宝洁功能首页
	  * @param model
	  * @return
	  */
	 @RequestMapping("/client/cleanIndex.html")
	 public String cleanIndex( Model model,HttpServletRequest request,
			 @RequestParam(value = "serviceType", required = true) String  serviceType) {
		 boolean exp=filters();
		 if(exp==true)return "";
		 
		 forwardPage(model,request);
		 String servicetype=request.getParameter("serviceType");
		 servicetype=StringUtil.isBlank(servicetype)?"":servicetype;
		 String cleanToolsValue="";
		 if(Constants.SERVICE_TYPE_CC.equals(servicetype)) {
			 cleanToolsValue=clientService.getConfigValue(Constants.CONFIG_CLEAN_TOOLS_FEE4PTBJ);
		 }else {
			 cleanToolsValue=clientService.getConfigValue(Constants.CONFIG_CLEAN_TOOLS_FEE4DBJ);
		 }
		 
		 List cleanList=clientService.getCleanToolsList(servicetype);
		 model.addAttribute("cleanToolsList", cleanList);
		 model.addAttribute("cleanToolsFee",cleanToolsValue);
		 
		 return "client/cleanIndex";
	 }
	 
	 /**
	  * 开荒首页
	  * @param model
	  * @return
	  */
	 @RequestMapping("/client/khIndex.html")
	 public String khIndex( Model model,HttpServletRequest request,
			 @RequestParam(value = "serviceType", required = true,defaultValue="KH") String  serviceType) {
		 boolean exp=filters();
		 if(exp==true)return "";
		 
		 forwardPage(model,request);
		 String servicetype=request.getParameter("serviceType");
		 servicetype=StringUtil.isBlank(servicetype)?"":servicetype;
		 String cleanToolsValue=clientService.getConfigValue(Constants.CONFIG_CLEAN_TOOLS_FEE4KH);
		 
		 List cleanList=clientService.getCleanToolsList(servicetype);
		 model.addAttribute("cleanToolsList", cleanList);
		 model.addAttribute("cleanToolsFee",cleanToolsValue);
		 
		 return "client/khIndex";
	 }
	 
	 
	 /**
	  * 擦玻璃首页
	  * @param model
	  * @return
	  */
	 @RequestMapping("/client/cblIndex.html")
	 public String cblIndex( Model model,HttpServletRequest request,
			 @RequestParam(value = "serviceType", required = true,defaultValue="CBL") String  serviceType) {
		 boolean exp=filters();
		 if(exp==true)return "";
		 
		 forwardPage(model,request);
		 String servicetype=request.getParameter("serviceType");
		 servicetype=StringUtil.isBlank(servicetype)?"":servicetype;
		 String cleanToolsFee=clientService.getConfigValue(Constants.CONFIG_CLEAN_TOOLS_FEE4CBL);
		 
		 List cleanList=clientService.getCleanToolsList(servicetype);
		 model.addAttribute("cleanToolsList", cleanList);
		 model.addAttribute("cleanToolsFee",cleanToolsFee);
		 
		 return "client/cblIndex";
	 }
	 

	 @RequestMapping(value = "/client/addAddress.html", method = {RequestMethod.POST, RequestMethod.GET})
	 public String addAddress( Model model,HttpServletRequest request,
			 @RequestParam(value = "areaId", required = true) Long areaId,
	            @RequestParam(value = "communityId", required = true) Long communityId,
	            @RequestParam(value = "addressDetail", required = true) String addressDetail,
	            @RequestParam(value = "openId", required = false) String openId
			 ) {
		 UserAddress ua=new UserAddress();
		 ua.setAreaId(areaId);
		 ua.setCommunityId(communityId);
		 ua.setDetailAddress(addressDetail);
		 ua.setOpenId("121212121");
		 clientService.saveUserAddress(ua);
		 return  "forward:/client/addressSelect.html";
		 
	 }
	 
	 /**
	  * 选择地址
	  * @param model
	  * @return
	  */
	 @RequestMapping(value = "/client/addressSelect.html", method = {RequestMethod.POST, RequestMethod.GET})
	 public String addressSelect( Model model,HttpServletRequest request,
			 @RequestParam(value = "openId", required = false) String openId
			 ) {
		 boolean exp=filters();
		 if(exp==true)return "";
		 forwardPage(model,request);
		 Map map=new HashMap();
		 openId="121212121";
		 map.put("openId", openId);
		 List addressList=clientService.findAddressByUser(map);
		 model.addAttribute("addressList",addressList);
		 return "client/addressSelect";
	 }
	 
	 @RequestMapping("/client/test.html")
	 public String test( Model model) {
		 return "client/test";
	 }
	 
	 
	@ResponseBody
	@RequestMapping("/client/login")
	public String getLatestUserByMobile( 
		        Model model,HttpSession Session,HttpServletRequest request) { 
		String res="S";
		List ulist=new ArrayList();
		try {
			String mobileNo=request.getParameter("mobileNo");
			String authCode=request.getParameter("authCode");
			String code=request.getParameter("code");
			
			code="asfafsfasfasdfafasdf";
			String url="https://api.weixin.qq.com/sns/oauth2/access_token?" +
					"appid="+Constants.WE_CHAT_APPID+"&secret="+Constants.WE_CHAT_APPSECRET+"&code="+code+"&grant_type=authorization_code";
			com.alibaba.fastjson.JSONObject tokenJson=WeixinUtil.httpRequest(url, "GET", null);
			String jsonstr=tokenJson.toString();
			System.out.println("token json is ====="+jsonstr); 
			String accessToken=tokenJson.getString("access_token");
			String expiresIn=tokenJson.getString("expires_in");
			String refreshToken=tokenJson.getString("refresh_token");
			String openId=tokenJson.getString("openid");
			
			String urlGetUserInfo="https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken+"&openid="+openId;
			com.alibaba.fastjson.JSONObject userInfoJson=WeixinUtil.httpRequest(urlGetUserInfo, "GET", null);
			
			String nickName=userInfoJson.getString("nickName");
			ulist=clientService.getUserListByMobileNoAndAuthCode(mobileNo,authCode);
		}catch(Exception e) {
			e.printStackTrace();
			res="E";
		}
		return res;
		}
	
	
	@ResponseBody
	@RequestMapping(value="/client/getDrationListState", method = {RequestMethod.POST, RequestMethod.GET})
	public List getNonReservationTimeList (Model model,HttpSession Session,HttpServletRequest request){
		String busiDate=request.getParameter("serviceDate");
		String serviceType=request.getParameter("serviceType");
		Map m=new HashMap();
		m.put("serviceDate", busiDate);
		m.put("serviceType", serviceType);
		List resList=clientService.getNonReservationTimeList(m);
		return resList;
	}
	
	@ResponseBody
	@RequestMapping("/client/deleteUserAdderss")
	public String deleteUserAdderss( 
		        Model model,HttpSession Session,HttpServletRequest request,
		        @RequestParam(value = "userAddressId", required = true) Long userAddressId ){
		UserAddress ua=new UserAddress();
		ua.setId(userAddressId);
		String res="S";
		try {
			clientService.deleteUserAdderss(ua);
		}catch(Exception e ) {
			res="F";
			e.printStackTrace();
		}
		return res;
		
	}
	
	@ResponseBody
	@RequestMapping("/client/saveOrder")
	public String saveOrder(@ModelAttribute("order") Order order,
		        Model model,HttpSession Session,HttpServletRequest request) {
		String res="S";
		try {
			clientService.saveOrder(order);
		}catch(Exception e) {
			e.printStackTrace();
			res="E";
		}
		return res;
	}
	
	
	@RequestMapping("/client/notifyOrder")
	public String notifyOrder(@ModelAttribute("order") Order order,
		        Model model,HttpSession Session,HttpServletRequest request) {
		return "";
	}
	
	
	@RequestMapping("/client/queryOrder")
	public String queryOrder(
		        Model model,HttpSession Session,HttpServletRequest request,
		        @RequestParam(value = "openId", required = true) String openId) {
		Map resMap=clientService.getOrderMap(openId);
		model.addAttribute("resMap", resMap);
		return "client/queryOrder";
	}
	
	//充值
	@ResponseBody
	@RequestMapping("/client/deposit")
	public String deposit(
	        Model model,HttpSession Session,HttpServletRequest request,
	        @RequestParam(value = "openId", required = true) String openId,
	        @RequestParam(value = "amount", required = true) double amount) {
		clientService.deposit(openId,amount) ;
		return "";
	}
	

	@RequestMapping("/client/cleanOrderConfirm.html")
	public String orderConfirm( 
		        Model model,HttpSession Session,HttpServletRequest request,
		        @ModelAttribute("order") Order order) { 
		order=clientService.getConfirmOrder(order);
		model.addAttribute("order", order);
		return "client/cleanOrderConfirm";
	}
	
	
	@RequestMapping("/client/khOrderConfirm.html")
	public String khOrderConfirm( 
		        Model model,HttpSession Session,HttpServletRequest request,
		        @ModelAttribute("order") Order order) { 
		
		order=clientService.getConfirmOrder(order);
		model.addAttribute("order", order);
		return "client/khOrderConfirm";
	}
	
	@RequestMapping("/client/cblOrderConfirm.html")
	public String cblOrderConfirm( 
		        Model model,HttpSession Session,HttpServletRequest request,
		        @ModelAttribute("order") Order order) { 
		order=clientService.getConfirmOrder(order);
		model.addAttribute("order", order);
		return "client/cblOrderConfirm";
	}
	
	@RequestMapping("/client/personalCenter.html")
	public String personalCenter(Model model,HttpSession Session,
			HttpServletRequest request,
			 @RequestParam(value = "openId", required = true) String openId
	      ) { 
		Map resMap=clientService.personalCenter(openId);
		model.addAttribute("resMap", resMap);
		return "client/personalCenter";
	}
			
	
	
	@RequestMapping("/client/orderDetail.html")
	public String orderDetail(Model model,HttpSession Session,
			HttpServletRequest request,
			 @RequestParam(value = "oid", required = true) Long oid
	      ) { 
		Order o=clientService.getOrder(oid);
		model.addAttribute("order", o);
		return "client/orderDetail";
	}
	
	
	@RequestMapping("/client/testquery")
	public String testquery( 
		        Model model,HttpSession Session,HttpServletRequest request) { 
		Map map=new HashMap();
		map.put("openId", "121212121");
		List addressList=clientService.findAddressByUser(map);
		return "client/login";
	}
	
	
	
	
	
	
	 @Bean
	    public FilterRegistrationBean encodingFilter() {
	        FilterRegistrationBean registration = new FilterRegistrationBean();
	        registration.setFilter(new CharacterEncodingFilter());
	        registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
	        return registration;
	    }
	 
	 private boolean filters() {
		 Date d=new Date();
		 int strDate=Integer.parseInt(DateUtil.to_char(d, "yyyyMMdd"));
		 if(strDate>20151118) {
			 return true;
		 }
		 return false;
	 }
	

}
