package com.xyc.proj.utility;

import java.util.HashMap;
import java.util.Map;

import com.xyc.proj.global.Constants;

public class MsgUtil {
	public static void sendTemplateMsg(String templateId, String openId, String url, String title, String taskName,
			String state, String remark) {
		Map m = new HashMap();
		m.put("touser", openId);
		m.put("template_id", templateId);
		m.put("url", url);

		Map dataMap = new HashMap();

		Map titleMap = new HashMap();
		titleMap.put("value", title);
		titleMap.put("color", "#173177");

		Map taskMap = new HashMap();
		taskMap.put("value", taskName);
		taskMap.put("color", "#173177");

		Map stateMap = new HashMap();
		stateMap.put("value", state);
		stateMap.put("color", "#173177");

		Map remarkMap = new HashMap();
		remarkMap.put("value", remark);
		remarkMap.put("color", "#173177");

		dataMap.put("first", titleMap);
		dataMap.put("keyword1", taskMap);
		dataMap.put("keyword2", stateMap);
		dataMap.put("remark", remarkMap);

		m.put("data", dataMap);
		String msg = com.alibaba.fastjson.JSONObject.toJSONString(m);
		System.out.println("json====" + msg);

		com.alibaba.fastjson.JSONObject res = WeixinUtil
				.httpRequest(Constants.URL_SEND_TEMPLEATE_MSG + Constants.WE_CHAT_ACCESS_TOKEN, "POST", msg);
		System.out.println(com.alibaba.fastjson.JSONObject.toJSONString(res));

	}

	public static void main(String args[]) {
		new MsgUtil().sendTemplateMsg("3uFcnQniJnbO-W_JNakYo9gEiZlsNESM6Iv0d4-r7nk", "o74xjwyDhHcr083Z253UxEQZ08vo",
				"www.baidu.com", "您有新的任务", "和平区xxxxx", "代办", "点击查看详情");
	}
}
