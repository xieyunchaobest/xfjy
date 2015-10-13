/**
 * All rights, including trade secret rights, reserved.
 */
package com.xyc.proj.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClientController {

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
	 

}
