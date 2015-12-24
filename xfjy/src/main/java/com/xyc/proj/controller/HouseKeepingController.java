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
import org.springframework.web.bind.annotation.RequestParam;

import com.xyc.proj.entity.Area;
import com.xyc.proj.global.Constants;
import com.xyc.proj.service.ClientService;
import com.xyc.proj.service.ServerService;
import com.xyc.proj.utility.PageView;
import com.xyc.proj.utility.StringUtil;

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

	 
	@RequestMapping("/client/houseKeepingIndex.html")
	public String houseKeepingIndex( 
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
	 

}
