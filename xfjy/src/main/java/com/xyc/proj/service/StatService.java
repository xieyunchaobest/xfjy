package com.xyc.proj.service;

import java.util.Map;

import com.xyc.proj.utility.PageView;

public interface StatService {
	public PageView geOrderStatPage(Map m);
	
	public PageView getDepositStatPage(Map m);
}
