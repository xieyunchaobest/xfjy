package com.xyc.proj.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.xyc.proj.entity.Order;
import com.xyc.proj.global.Constants;


public class Tools {
	
	 
	public static com.alibaba.fastjson.JSONObject getJSON(HttpServletRequest request)  { 
		StringBuffer sb = new StringBuffer("");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(ServletInputStream) request.getInputStream(), "UTF-8"));
			
			String temp;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
			br.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		com.alibaba.fastjson.JSONObject json=com.alibaba.fastjson.JSONObject.parseObject(sb.toString());
		return json;
	}
	
	
	public static void forwardPage(Model model, HttpServletRequest request) {
		boolean exp = filters();
		if (exp == true)
			return;
		String openId = request.getParameter("openId");
		openId = StringUtil.isBlank(openId) ? "" : openId;

		String userAddressId = request.getParameter("userAddressId");
		userAddressId = StringUtil.isBlank(userAddressId) ? "0" : userAddressId;

		String fullAddress = request.getParameter("fullAddress");
		fullAddress = StringUtil.isBlank(fullAddress) ? "" : fullAddress;

		String mobileNo = request.getParameter("mobileNo");
		mobileNo = StringUtil.isBlank(mobileNo) ? "" : mobileNo;

		String serviceDate = request.getParameter("serviceDate");
		serviceDate = StringUtil.isBlank(serviceDate) ? "" : serviceDate;

		String duration = request.getParameter("duration");
		duration = StringUtil.isBlank(duration) ? "" : duration;

		String startTime = request.getParameter("startTime");
		startTime = StringUtil.isBlank(startTime) ? "" : startTime;

		String endTime = request.getParameter("endTime");
		endTime = StringUtil.isBlank(endTime) ? "" : endTime;

		String servicetype = request.getParameter("serviceType");
		servicetype = StringUtil.isBlank(servicetype) ? "" : servicetype;

		String cycleType = request.getParameter("cycleType");
		cycleType = StringUtil.isBlank(cycleType) ? "" : cycleType;

		String durationMonth = request.getParameter("durationMonth");
		durationMonth = StringUtil.isBlank(cycleType) ? "" : durationMonth;

		String repeatInWeek = request.getParameter("repeatInWeek");
		repeatInWeek = StringUtil.isBlank(repeatInWeek) ? "" : repeatInWeek;

		String repeatInWeekText = request.getParameter("repeatInWeekText");
		repeatInWeekText = StringUtil.isBlank(repeatInWeekText) ? "" : repeatInWeekText;

		String durationMonthText = request.getParameter("durationMonthText");
		durationMonthText = StringUtil.isBlank(durationMonthText) ? "" : durationMonthText;

		String durationText = request.getParameter("durationText");
		durationText = StringUtil.isBlank(durationText) ? "" : durationText;

		String isProviceCleanTools = request.getParameter("isProviceCleanTools");
		isProviceCleanTools = StringUtil.isBlank(isProviceCleanTools) ? "" : isProviceCleanTools;

		String area = request.getParameter("area");
		area = StringUtil.isBlank(area) ? "" : area;

		String balconyCount = request.getParameter("balconyCount");
		balconyCount = StringUtil.isBlank(balconyCount) ? "" : balconyCount;

		String windowCount = request.getParameter("windowCount");
		windowCount = StringUtil.isBlank(windowCount) ? "" : windowCount;

		String name = request.getParameter("name");
		name = StringUtil.isBlank(name) ? "" : name;

		Order o = new Order();
		o.setOpenId(openId);
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
		o.setName(name);
		model.addAttribute("order", o);
	}
	

	public static boolean filters() {
//		Date d = new Date();
//		TestMain.getLocalFilter();
		return false;
	}

	public static boolean isjz(String busi) {
		if(busi.equals(Constants.WORK_SERVICE_JZY) || 
			busi.equals(Constants.WORK_SERVICE_YY_YYS) ||
			busi.equals(Constants.WORK_SERVICE_YY_YS) ||
			busi.equals(Constants.WORK_SERVICE_YL) ||
			busi.equals(Constants.WORK_SERVICE_XSG)  ||
			busi.equals(Constants.WORK_SERVICE_YYHG)  
		) {
			return true;
		}
		
		return false;
	}
}
