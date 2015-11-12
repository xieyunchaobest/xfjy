/**
 * All rights, including trade secret rights, reserved.
 */
package com.xyc.proj.controller;

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
import com.xyc.proj.service.ClientService;
import com.xyc.proj.utility.Properties;
import com.xyc.proj.utility.StringUtil;

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
			serviceType="CC";	
			Map m=new HashMap();
			m.put("serviceDate", serviceDate);
			m.put("serviceType", serviceType);
			List resList=clientService.getNonReservationTimeList(m);
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
	            @RequestParam(value = "areaId", required = false) String areaId,
	            Model model) {
		 return "client/login";
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
		 
		 String servicetype=request.getParameter("servicetype");
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
		 
		 Order o =new Order();
		 o.setUserAddressId(userAddressId);
		 o.setFullAddress(fullAddress);
		 o.setMobileNo(mobileNo);
		 o.setServiceDate(serviceDate);
		 o.setDuration(duration);
		 o.setStartTime(startTime);
		 o.setEndTime(endTime);
		 o.setServicetype(servicetype);
		 o.setCycleType(cycleType);
		 o.setRepeatInWeek(repeatInWeek);
		 o.setDurationMonth(durationMonth);
		 o.setRepeatInWeekText(repeatInWeekText);
		 o.setDurationMonthText(durationMonthText);
		 o.setDurationText(durationText);
		 
		 model.addAttribute("order", o);
	 }
	 /**
	  * 宝洁功能首页
	  * @param model
	  * @return
	  */
	 @RequestMapping("/client/cleanIndex.html")
	 public String cleanIndex( Model model,HttpServletRequest request) {
		 forwardPage(model,request);
		 return "client/cleanIndex";
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
	 
	@RequestMapping("/client/login")
	public String getLatestUserByMobile( 
		        Model model,HttpSession Session,HttpServletRequest request) { 
		String mobileNo=request.getParameter("mobileNo");
		String authCode=request.getParameter("authCode");
			List ulist=clientService.getUserListByMobileNoAndAuthCode(mobileNo,authCode);
			if(ulist!=null && ulist.size()>0) {
				 return "client/index";
			}else {
				return "client/login";
			}
			
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
	

}
