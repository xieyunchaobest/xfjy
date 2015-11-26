/**
 * All rights, including trade secret rights, reserved.
 */
package com.xyc.proj.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.xyc.proj.entity.Worker;
import com.xyc.proj.global.Constants;
import com.xyc.proj.service.ClientService;
import com.xyc.proj.service.ServerService;
import com.xyc.proj.utility.PageView;
import com.xyc.proj.utility.StringUtil;

@Controller
@SessionAttributes("user")
public class ServerController {
	
	@Autowired
	ServerService serverService;
	@Autowired
	ClientService clientService;
	
	 @RequestMapping(value="/server/login.html",method = {RequestMethod.POST,RequestMethod.GET})
	 public String loginInit() {
		 return "server/login";
	 }
	
	 
	 @RequestMapping(value="/server/doLogin",method = {RequestMethod.POST,RequestMethod.GET})
	 public String doLogin( @RequestParam(value = "code", required = true) String code,
			 HttpSession session,
	         @RequestParam(value = "password", required = true) String password,
	         Model model) {
		 List workList=serverService.findByCodeAndPassword(code, password);
		 String res="server/index";
		 if(workList==null || workList.size()==0) {
			 model.addAttribute("error", "用户名或密码错误！");
			 return "server/login";
		 }else {
			 Worker w=(Worker)workList.get(0);
			 session.setAttribute("user", w);
			 model.addAttribute("user", w);
		 }
		 
		 return res;
	 }
	
	
	 @RequestMapping(value="/server/queryWorker.html",method = {RequestMethod.POST,RequestMethod.GET})
	 public String queryWorker(
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
			
		 if(!StringUtil.isBlank(areaId)) {
			 long lAreaId=Long.parseLong(areaId);
			 parmMap.put("areaId", lAreaId);
		 }else {
			 parmMap.put("areaId", "");
		 }
		 if(!StringUtil.isBlank(storeId)) {
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
		 
		 List areaList=clientService.findAreaList();
		 List storeList=serverService.findStore();
		 model.addAttribute("areaList", areaList);
		 model.addAttribute("storeList", storeList);
		 return "server/queryWorker";
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
	 
	 
	 @RequestMapping(value="/server/queryOrder.html",method = {RequestMethod.POST,RequestMethod.GET})
	 public String queryOrder(Model model,
			 @RequestParam(value = "areaId", required = false,defaultValue="0") Long areaId,
			 @RequestParam(value = "startTime", required = false) String startTime,
			 @RequestParam(value = "endTime", required = false) String endTime,
			 @RequestParam(value = "serviceDate", required = false) String serviceDate,
			 @RequestParam(value = "serviceType", required = false) String serviceType,
			 @RequestParam(value = "currentPageNum", required = false, defaultValue = "1") Integer currentPageNum,
			@RequestParam(value = "orderByStr", required = false, defaultValue = "name,desc") String orderBy
			 ) {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put(Constants.CURRENT_PAGENUM, currentPageNum);
		parmMap.put(Constants.ORDERBY, StringUtil.formatSortBy(orderBy));
		parmMap.put("areaId", areaId);
		parmMap.put("startTime", startTime);
		parmMap.put("endTime", endTime);
		parmMap.put("serviceDate", serviceDate);
		parmMap.put("serviceType", serviceType);
		List areaList=clientService.findAreaList();
		
		PageView pageView = serverService.getOrderPageView(parmMap);
		model.addAttribute("pageView", pageView);
		model.addAttribute("parms", parmMap);
		model.addAttribute("areaId", areaId);
		model.addAttribute("areaList", areaList);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("serviceDate", serviceDate);
		model.addAttribute("serviceType", serviceType);
		 return "server/queryOrder";
	 }
	 
	 
	 @ResponseBody
	 @RequestMapping(value="/server/dispatchOrder.html",method = {RequestMethod.POST,RequestMethod.GET})
	 public String dispatch(Model model,
			 @RequestParam(value = "orderIds", required = true) String orderIds,
			 @RequestParam(value = "ayiId", required = true) Long aiyiId
			) {
		 String res="S";
		 try {
			 serverService.dispatchOrder(orderIds, aiyiId);
		 }catch(Exception e) {
			 e.printStackTrace();
			 res="E";
		 }
		 return res;
	 }
	 
	 @RequestMapping(value="/server/queryCommunity.html",method = {RequestMethod.POST,RequestMethod.GET})
	 public String queryCommunity(Model model,
			 @RequestParam(value = "areaId", required = false) Long areaId,
			 @RequestParam(value = "name", required = false) String name,
			 @RequestParam(value = "currentPageNum", required = false, defaultValue = "1") Integer currentPageNum
			) {
		 Map<String, Object> parmMap = new HashMap<String, Object>();
			parmMap.put(Constants.CURRENT_PAGENUM, currentPageNum);
			parmMap.put("areaId", areaId);
			parmMap.put("name", name);
			List areaList=clientService.findAreaList();
			
			PageView pageView = serverService.getCommunityPage(parmMap);
			model.addAttribute("pageView", pageView);
			model.addAttribute("parms", parmMap);
			model.addAttribute("areaList", areaList);
			 return "server/queryCommunity";
	 }
	 
	 @RequestMapping(value="/server/queryClientUser.html",method = {RequestMethod.POST,RequestMethod.GET})
	 public String queryCommunity(Model model,
			 @RequestParam(value = "mobileNo", required = false) String mobileNo,
			 @RequestParam(value = "currentPageNum", required = false, defaultValue = "1") Integer currentPageNum
			) {
		 Map<String, Object> parmMap = new HashMap<String, Object>();
			parmMap.put(Constants.CURRENT_PAGENUM, currentPageNum);
			parmMap.put("mobileNo", mobileNo);
			
			PageView pageView = serverService.getCommunityPage(parmMap);
			model.addAttribute("pageView", pageView);
			model.addAttribute("parms", parmMap);
			 return "server/queryCommunity";
	 }
}
