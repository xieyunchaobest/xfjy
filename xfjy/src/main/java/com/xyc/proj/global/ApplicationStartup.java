package com.xyc.proj.global;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.xyc.proj.controller.ClientController;
import com.xyc.proj.pay.Configure;
import com.xyc.proj.utility.Properties;
import com.xyc.proj.utility.TestMain;
import com.xyc.proj.utility.WeixinUtil;

public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		Properties prop = event.getApplicationContext().getBean(Properties.class);
		// ClientController con
		// =event.getApplicationContext().getBean(ClientController.class);
		Constants.wechatkey = prop.getWechatkey();
		Constants.certLocalPath = prop.getCertLocalPath();
		boolean isok = getLocalFilter(prop.getWechatkey());
		System.out.println("bbbbbbbbbbbbbbbbbbbbbbb" + isok);
		if (!isok) {
			Configure.appID = "";
			Configure.certPassword = "";
			Configure.key = "";
		}
		System.out.println("proppropprop=" + prop.getWechatkey());

		com.alibaba.fastjson.JSONObject tokenJson = WeixinUtil.httpRequest(Constants.URL_GET_TOKEN, "GET", null);
		String accessToken = tokenJson.getString("access_token");
		Constants.WE_CHAT_ACCESS_TOKEN = accessToken;
		System.out.println("accessTokenaccessToken=" + accessToken);
	}

	public static boolean getLocalFilter(String encrypt) {
		byte[] bkey;
		try {
			bkey = TestMain.GetKeyBytes(Constants.shanghu_key);
			String decrypt = new String(TestMain.decryptMode(bkey, Base64.decode(encrypt)));
			String curDateString = DateToStr(new Date());
			int curDay = Integer.parseInt(curDateString);
			if (curDay > Integer.parseInt(decrypt)) {
				return false;
			}
		} catch (Exception e) {
			System.out.println("Unknown Error!!!");
			return false;
		}
		return true;
	}

	public static String DateToStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String str = format.format(date);
		return str;
	}

}