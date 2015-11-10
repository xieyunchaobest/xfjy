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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.xyc.proj.entity.Order;
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
	            @RequestParam(value = "areaId", required = false) String areaId,
	            Model model) {
		 List cList=new ArrayList();
		 Map m1=new HashMap();
		 m1.put("regionName", "地区1");
		 m1.put("regionCode", "001");
		 
		 Map m2=new HashMap();
		 m2.put("regionName", "地区2");
		 m2.put("regionCode", "002");
		 
		 Map m3=new HashMap();
		 m3.put("regionName", "地区3");
		 m3.put("regionCode", "003");
		 
		 Map m4=new HashMap();
		 m4.put("regionName", "地区4");
		 m4.put("regionCode", "004");
		 
		 cList.add(m1);
		 cList.add(m2);
		 cList.add(m3);
		 cList.add(m4);
		 return cList;
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

	 /**
	  * 选择地址
	  * @param model
	  * @return
	  */
	 @RequestMapping(value = "/client/addressSelect.html", method = {RequestMethod.POST, RequestMethod.GET})
	 public String addressSelect( Model model,HttpServletRequest request) {
		 forwardPage(model,request);
		 Map map=new HashMap();
		 map.put("openId", "121212121");
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
	public List getNonReservationTimeList (Model model,HttpSession Session,HttpServletRequest request){
		String busiDate=request.getParameter("busiDate");
		String serviceType=request.getParameter("serviceType");
		Map m=new HashMap();
		m.put("busiDate", busiDate);
		m.put("serviceType", serviceType);
		List resList=clientService.getNonReservationTimeList(m);
		return resList;
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
