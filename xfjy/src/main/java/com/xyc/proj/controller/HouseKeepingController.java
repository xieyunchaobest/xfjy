/**
 * All rights, including trade secret rights, reserved.
 */
package com.xyc.proj.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xyc.proj.entity.Area;
import com.xyc.proj.entity.ClientUser;
import com.xyc.proj.entity.DepositSummary;
import com.xyc.proj.entity.Order;
import com.xyc.proj.entity.Worker;
import com.xyc.proj.global.Constants;
import com.xyc.proj.service.ClientService;
import com.xyc.proj.service.HouseKeepingService;
import com.xyc.proj.service.ServerService;
import com.xyc.proj.utility.PageView;
import com.xyc.proj.utility.StringUtil;
import com.xyc.proj.utility.Tools;

/**
 * 幸福加缘客户端控制类
 * 
 * @author xieyunchao
 *
 */
@Controller
public class HouseKeepingController {
	@Autowired
	ClientService clientService;
	@Autowired
	ServerService serverService;
	@Autowired
	HouseKeepingService houseKeepingService;

	 
	@RequestMapping("/client/houseKeepingIndex.html")
	public String houseKeepingIndex( 
			@RequestParam(value = "openId", required = true) String openId,
			@RequestParam(value = "areaId", required = false) String areaId,
			@RequestParam(value = "areaName", required = false) String areaName,
			@RequestParam(value = "serviceTypeTwo", required = false) String serviceTypeTwo,
			@RequestParam(value = "serviceTypeTwoText", required = false) String serviceTypeTwoText,
			@RequestParam(value = "workTime", required = false) String workTime,
			@RequestParam(value = "workTimeText", required = false) String workTimeText,
			@RequestParam(value = "currentPageNum", required = false, defaultValue = "1") Integer currentPageNum,
			Model model,HttpSession session) {
		List areaList = clientService.findAreaList();
		Area area=new Area();
		area.setName("所有区域");
		areaList.add(area);
		Map parmMap=new HashMap();
		parmMap.put(Constants.CURRENT_PAGENUM, currentPageNum);
		parmMap.put("role", "A");
		parmMap.put("serviceTypeOne", "J");
		if (!StringUtil.isBlank(areaId)) {
			long lAreaId = Long.parseLong(areaId);
			parmMap.put("areaId", lAreaId);
		} else {
			parmMap.put("areaId", "");
		} 

		parmMap.put("serviceTypeTwo", serviceTypeTwo);
		parmMap.put("serviceTypeTwoText", serviceTypeTwoText);
		parmMap.put("workTime", workTime);
		parmMap.put("areaName", areaName);
		parmMap.put("workTimeText", workTimeText);
		
		PageView pageView = serverService.getWorkPage(parmMap);
		model.addAttribute("pageView", pageView);
		model.addAttribute("parms", parmMap);
		model.addAttribute("areaList", areaList);
		model.addAttribute("openId", openId);
		return "client/houseKeepingIndex";
	}
	
	@RequestMapping("/client/appendWorkerItems.html")
	public String appendWorkerItems( 
			@RequestParam(value = "areaId", required = false) String areaId,
			@RequestParam(value = "serviceTypeTwo", required = false) String serviceTypeTwo,
			@RequestParam(value = "workTime", required = false) String workTime,
			@RequestParam(value = "currentPageNum", required = false, defaultValue = "1") Integer currentPageNum,
			Model model,HttpSession session) { 
		Map parmMap=new HashMap();
		parmMap.put(Constants.CURRENT_PAGENUM, currentPageNum);
		parmMap.put("role", "A");
		parmMap.put("serviceTypeOne", "J");
		if (!StringUtil.isBlank(areaId)) {
			long lAreaId = Long.parseLong(areaId);
			parmMap.put("areaId", lAreaId);
		} else {
			parmMap.put("areaId", "");
		} 

		parmMap.put("serviceTypeTwo", serviceTypeTwo);
		parmMap.put("workTime", workTime);
		
		PageView pageView = serverService.getWorkPage(parmMap);
		model.addAttribute("pageView", pageView);
		model.addAttribute("parms", parmMap);
		
		return "client/workerItems";
	}
	
	@RequestMapping("/client/workerDetail.html")
	public String workerDetail(@RequestParam(value = "workerId", required = true) Long workerId,
			@RequestParam(value = "openId", required = true) String openId,
			Model model) {
		Worker worker=serverService.findWorker(workerId);
		worker=serverService.findWorkerPlus(worker);
		model.addAttribute("worker", worker);
		model.addAttribute("openId", openId);
		return "client/workerDetail";
		
	}
	
	@RequestMapping("/client/housekeepingConfirm.html")
	public String housekeepingConfirm(@RequestParam(value = "workerId", required = true) Long workerId,
			@RequestParam(value = "openId", required = true) String openId,
			HttpServletRequest request,
			Model model) {
		Tools.forwardPage(model, request);
		Worker worker=serverService.findWorker(workerId);
		worker=serverService.findWorkerPlus(worker);
		model.addAttribute("worker", worker);
		model.addAttribute("openId", openId);
		ClientUser cu=clientService.findClientUserByMobileOpenId(openId);
		model.addAttribute("mobileNo", cu.getMobileNo());
		return "client/housekeepingConfirm";
	} 
	
	@RequestMapping(value = "/server/orderDispatchInit4jzInit.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String orderDispatchInit4jzInit(
			@RequestParam(value = "orderId", required = true) String orderId,
			Model model,HttpSession session) {
		Order o=clientService.getOrder(Long.parseLong(orderId));
		Worker w=(Worker)session.getAttribute("user");
		List ayiList=new ArrayList();
		if(w!=null && Constants.WORK_ROLE_ROLE_TEACHER.equals(w.getRole())) {
			ayiList=serverService.findWorkerByTeacherId(w.getId());
		}
		model.addAttribute("order", o);
		model.addAttribute("ayiList", ayiList);
		return "server/orderDispatch4jz";
	}
	 
	@ResponseBody
	@RequestMapping(value = "/server/orderDispatch4jz", method = { RequestMethod.POST, RequestMethod.GET })
	public String orderDispatch4jz(
			@RequestParam(value = "orderId", required = true) String orderId,
			@RequestParam(value = "serviceDate", required = true) String serviceDate,
			@RequestParam(value = "ayiId", required = true) String ayiId,
			@RequestParam(value = "totalFee", required = true) double totalFee,
			Model model,HttpSession session) {
		String res="S";
		try {
			 Order o=new Order();
			 o.setId(Long.parseLong(orderId));
			 o.setServiceDate(serviceDate);
			 o.setTotalFee(totalFee);
			 o.setWorkerId(Long.parseLong(ayiId));
			 houseKeepingService.dispatchOrder(o);
		}catch(Exception e) {
			e.printStackTrace();
			res="E";
		}
		return res;
	}
	
	@ResponseBody
	@RequestMapping("/client/createOrder4jz")
	public Map createOrder4jz(Model model, HttpSession Session, HttpServletRequest request,
			@ModelAttribute("order") Order order) {
		Map resMap = new HashMap();
		resMap.put("resultCode", "S");
		try {
			 houseKeepingService.createOrder(order);
		} catch (Exception e) {
			e.printStackTrace();
			resMap.put("resultCode", "E");
		}

		return resMap;
	}

	@RequestMapping(value = "/client/houseKeepingPay.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String houseKeepingPay(
			@RequestParam(value = "orderId", required = true) String orderId,
			Model model,HttpSession session) {
		Order o=houseKeepingService.getOrder(Long.parseLong(orderId));
		Worker worker=serverService.findWorker(o.getWorkerId());
		worker=serverService.findWorkerPlus(worker);
		DepositSummary ds = clientService.getBalance(o.getOpenId());
		if (ds == null) {
			ds = new DepositSummary();
			ds.setFee(0d);
		}
		List couponList=clientService.getCouponListByUid(o.getOpenId());
		model.addAttribute("couponList", couponList);
		model.addAttribute("couponCount", couponList.size());
		model.addAttribute("ds", ds);
		model.addAttribute("order", o);
		model.addAttribute("worker", worker);
		return "client/houseKeepingPay";
	}
}
