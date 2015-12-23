/**
 * All rights, including trade secret rights, reserved.
 */
package com.xyc.proj.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xyc.proj.global.Constants;
import com.xyc.proj.service.StatService;
import com.xyc.proj.utility.DateUtil;
import com.xyc.proj.utility.PageView;
import com.xyc.proj.utility.StringUtil;

/**
 * 幸福加缘客户端控制类
 * 
 * @author xieyunchao
 *
 */
@Controller
public class StatController {

	@Autowired
	StatService statService; 

	/**
	 * 
	 * 
	 * @param areaId
	 * @param model
	 * @return
	 */
	@RequestMapping("/server/orderStat.html")
	public String orderStat(@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "currentPageNum", required = false, defaultValue = "1") Integer currentPageNum,
			Model model,HttpSession session) {
		if(StringUtil.isBlank(startTime)) {
			startTime=DateUtil.getToday();
		}
		if(StringUtil.isBlank(endTime)) {
			endTime=DateUtil.getToday();
		}
		Map paraMap=new HashMap();
		paraMap.put("startTime", startTime);
		paraMap.put("endTime", endTime);
		paraMap.put(Constants.CURRENT_PAGENUM, currentPageNum);
		PageView pv=statService.geOrderStatPage(paraMap);
		model.addAttribute("pv", pv);
		model.addAttribute("paraMap", paraMap);
		return "server/orderStat";
	}
	
	/**
	 * 
	 * 
	 * @param areaId
	 * @param model
	 * @return
	 */
	@RequestMapping("/server/depositStat.html")
	public String depositStat(@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "currentPageNum", required = false, defaultValue = "1") Integer currentPageNum,
			Model model,HttpSession session) {
		if(StringUtil.isBlank(startTime)) {
			startTime=DateUtil.getToday();
		}
		if(StringUtil.isBlank(endTime)) {
			endTime=DateUtil.getToday();
		}
		Map paraMap=new HashMap();
		paraMap.put("startTime", startTime);
		paraMap.put("endTime", endTime);
		paraMap.put(Constants.CURRENT_PAGENUM, currentPageNum);
		PageView pv=statService.getDepositStatPage(paraMap);
		model.addAttribute("pv", pv);
		model.addAttribute("paraMap", paraMap);
		return "server/depositStat";
	}

}
