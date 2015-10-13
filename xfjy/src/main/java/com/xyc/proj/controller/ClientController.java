/**
 * All rights, including trade secret rights, reserved.
 */
package com.xyc.proj.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.xyc.proj.entity.UserAuthCode;
import com.xyc.proj.service.ClientService;
import com.xyc.proj.utility.Properties;

@Controller
public class ClientController {
	
	@Autowired
	Properties properties;  
	@Autowired
	ClientService clientService;
	

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
	 
	 @RequestMapping("/client/addAddressInit")
	 public String addAddressInit(
	            @RequestParam(value = "areaId", required = false) String areaId,
	            Model model) {
		 return "client/addAddress";
	 }
	 
	 
	 @RequestMapping("/client/serviceDate")
	 public String serviceDate(
	            @RequestParam(value = "areaId", required = false) String areaId,
	            Model model) {
		 return "client/serviceDate";
	 }
	 
	 @RequestMapping("/client/login.html")
	 public String toLogin(
	            @RequestParam(value = "areaId", required = false) String areaId,
	            Model model) {
		 return "client/login";
	 }
	 
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

}
