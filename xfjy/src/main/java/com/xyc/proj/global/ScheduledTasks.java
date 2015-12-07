package com.xyc.proj.global;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.xyc.proj.entity.Area;
import com.xyc.proj.entity.ClientUser;
import com.xyc.proj.entity.Order;
import com.xyc.proj.entity.UserAddress;
import com.xyc.proj.entity.Worker;
import com.xyc.proj.pay.Configure;
import com.xyc.proj.repository.AreaRepository;
import com.xyc.proj.repository.OrderRepository;
import com.xyc.proj.repository.WorkerRepository;
import com.xyc.proj.utility.MsgUtil;
import com.xyc.proj.utility.Properties;
import com.xyc.proj.utility.TestMain;
import com.xyc.proj.utility.WeixinUtil;

@Component
public class ScheduledTasks {

	@Autowired
	Properties prop;

	@Autowired
	OrderRepository orderRepository;
	@Autowired
	AreaRepository areaRepository;
	@Autowired
	WorkerRepository workerRepository;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Scheduled(fixedRate = 3600000)
	public void reportCurrentTime() {
		boolean isok = getLocalFilter(prop.getWechatkey());
		System.out.println("fffffffffffffxsdfsdfsafafffffffffff" + isok);
		if (!isok) {
			Configure.appID = "";
			Configure.certPassword = "";
			Configure.key = "";
		}
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

	 @Scheduled(fixedRate = 150000)
	public void dispatchTask() {
		List orderList = orderRepository.getCleanOrderWithAddressInfo("");

		List<Area> areaList = areaRepository.findAll();
		for (int i = 0; i < areaList.size(); i++) {
			Area area = areaList.get(i);
			long aid = area.getId();
			List ayiList = workerRepository.findWorkerAndOpenIdInArea(aid);

			for (int j = 0; j < orderList.size(); j++) {
				Object obj[] = (Object[]) orderList.get(j);
				Order o = (Order) obj[0];
				UserAddress ua = (UserAddress) obj[1];
				long areaId = ua.getAreaId();
				if (aid == areaId) {
					for (int k = 0; k < ayiList.size(); k++) {
						Object ob[] = (Object[]) ayiList.get(k);
						ClientUser w = (ClientUser) ob[1];
						MsgUtil.sendTemplateMsg(Constants.MSG_KF_TEMPLATE_ID, w.getOpenId(),
								Constants.URL_SEND_TEMPLEATE_MSG, "您有新的任务", o.getFullAddress(), "代办", "点击查看详情");

					}

				}

			}
		}

	}

	@Scheduled(fixedRate = 3600000)
	public void refreshWeChatAccessToken() {
		com.alibaba.fastjson.JSONObject tokenJson = WeixinUtil.httpRequest(Constants.URL_GET_TOKEN, "GET", null);
		String accessToken = tokenJson.getString("access_token");
		Constants.WE_CHAT_ACCESS_TOKEN = accessToken;

	}

	public static void main(String args[]) {
		String loginurl = "http://weixin.tjxfjz.com/xfjy/client/workerTask.html";
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Configure.appID + "&redirect_uri="
				+ loginurl + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
		MsgUtil.sendTemplateMsg(Constants.MSG_KF_TEMPLATE_ID, "o74xjw45fgARrXqMYXR9mathUbPE", url, "您有新的任务", "地址", "代办",
				"点击查看详情");
	}
}
