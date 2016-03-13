package com.xyc.proj.pay;

import java.util.Date;
import java.util.Map;

import com.xyc.proj.utility.DateUtil;

public class PayMain {
	public static void main(String args[]) {


		String outTradeNo = RandomStringGenerator.getRandomStringByLength(32);
		int totalFee = 1;
		String spBillCreateIP = "192.18.0.2";
		String timeStart = DateUtil.Time2Str(new Date(),DateUtil.format1);
		String timeExpire = DateUtil.getDiffDate(1);
		String openId="o74xjw9vCvp6Wxa5zPEPu4ghP8gA";

		ScanPayReqData scanPayReqData = new ScanPayReqData(outTradeNo, totalFee, spBillCreateIP,
				timeStart, timeExpire,openId);
		try {
			String res=new ScanPayService().request(scanPayReqData);
			Map rm=XMLParser.getMapFromXML(res);
			
			System.out.println("res======"+res);
			System.out.println("Map======"+rm);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		};
	}

}
