package com.xyc.proj.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.xyc.proj.global.Constants;
import com.xyc.proj.mapper.DepositStatMapper;
import com.xyc.proj.mapper.OrderStatMapper;
import com.xyc.proj.utility.PageView;


public class StatServiceImpl implements StatService{
	
	@Lazy
	@Autowired
	OrderStatMapper orderStatMapper;
	@Lazy
	@Autowired
	DepositStatMapper depositStatMapper;
	
	public PageView geOrderStatPage(Map m) {
		PageView pv = new PageView((Integer) m.get("currentPageNum"));
		m.put("start", pv.getStart());
		m.put("end", pv.getCurrentMaxCnt());
		pv.setResultList(findOrderStatList(m));
		pv.setTotalRecord(orderStatMapper.getOrderPageCount(m));
		return pv;
	}
	
	public PageView geDepositStatPage(Map m) {
		PageView pv = new PageView((Integer) m.get("currentPageNum"));
		m.put("start", pv.getStart());
		m.put("end", pv.getCurrentMaxCnt());
		pv.setResultList(depositStatMapper.getDepositStatPage(m));
		pv.setTotalRecord(orderStatMapper.getOrderPageCount(m));
		return pv;
	}
	
	List findOrderStatList(Map m) {
		List oList= orderStatMapper.getOrderPage(m);
		if(oList!=null) {
			for(int i=0;i<oList.size();i++) {
				Map mm=(Map)oList.get(i);
				String serviceType=(String)mm.get("serviceType");
				if (Constants.SERVICE_TYPE_CC.equals(serviceType)) {
					mm.put("serviceTypeText","日常保洁");
				} else if (Constants.SERVICE_TYPE_DBJ.equals(serviceType)) {
					mm.put("serviceTypeText","大保洁");
				} else if (Constants.SERVICE_TYPE_CBL.equals(serviceType)) {
					mm.put("serviceTypeText","擦玻璃");
				} else if (Constants.SERVICE_TYPE_KH.equals(serviceType)) {
					mm.put("serviceTypeText","开荒");
				}
			}
		}
		return oList;
	}

}
