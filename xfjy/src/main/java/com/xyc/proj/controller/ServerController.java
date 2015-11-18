/**
 * All rights, including trade secret rights, reserved.
 */
package com.xyc.proj.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xyc.proj.entity.Worker;
import com.xyc.proj.global.Constants;
import com.xyc.proj.service.ServerService;
import com.xyc.proj.utility.PageView;
import com.xyc.proj.utility.StringUtil;

@Controller
public class ServerController {
	
	@Autowired
	ServerService serverService;
	
	 @RequestMapping(value="/server/queryWorker.html",method = {RequestMethod.POST,RequestMethod.GET})
	 public String addAddressInit(
	            @RequestParam(value = "areaId", required = false) String areaId,
	            @RequestParam(value = "storeId", required = false) String storeId,
	            @RequestParam(value = "name", required = false) String name,
	            @RequestParam(value = "serviceTypeTwo", required = false) String serviceTypeTwo,
	            @RequestParam(value = "role", required = false) String role,
	            @RequestParam(value = "workTime", required = false) String workTime,
	            @RequestParam(value = "currentPageNum", required = false, defaultValue = "1") Integer currentPageNum,
				@RequestParam(value = "orderByStr", required = false, defaultValue = "name,desc") String orderBy,
	            Model model) {
		 Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put(Constants.CURRENT_PAGENUM, currentPageNum);
		parmMap.put(Constants.ORDERBY, StringUtil.formatSortBy(orderBy));
			
		 if(areaId!="") {
			 long lAreaId=Long.parseLong(areaId);
			 parmMap.put("areaId", lAreaId);
		 }
		 if(storeId!="") {
			 long lstoreId=Long.parseLong(storeId);
			 parmMap.put("storeId", lstoreId);
		 }
		 
		 parmMap.put("name", name);
		 parmMap.put("serviceTypeTwo", serviceTypeTwo);
		 parmMap.put("role", role);
		 parmMap.put("workTime", workTime);
		 PageView pageView=serverService.getWorkPage(parmMap);
		 model.addAttribute("pageView", pageView);
		 model.addAttribute("parms", parmMap);
		 
		 return "client/addAddress";
	 }
	 
	 @RequestMapping(value="/server/updateWorkInit.html",method = {RequestMethod.POST,RequestMethod.GET})
	 public String updateWorkInit(Model model,
			 @RequestParam(value = "workerId", required = false) Long workerId) {	
		 Worker woker=serverService.getWorker(workerId);
		 model.addAttribute("worker", woker);
		 return "server/updateWorkInit";
	 }
	 
	 
	 @ResponseBody
	 @RequestMapping(value="/server/delteWorker",method = {RequestMethod.POST,RequestMethod.GET})
	 public String deleteWorker(Model model,
			 @RequestParam(value = "workerIds", required = false) String workerIds) {
		 String res="S";
		 try {
			 serverService.deleteWorker(workerIds);
		 }catch(Exception e) {
			 res="F";
		 }
		 return res;
	 }

}