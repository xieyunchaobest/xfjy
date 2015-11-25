package com.xyc.proj.pay;

import java.util.Date;

import com.xyc.proj.utility.DateUtil;

/**
 * User: rizenguo
 * Date: 2014/10/29
 * Time: 14:40
 * 这里放置各种配置数据
 */
public class Configure {
//这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）
	// 每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证
	// 收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改

	public static String key = "1qaz2wsx3edc4rfv5tgb5tghb6yuj78u";
	public static final String WE_CHAT_APPSECRET="032c3861e23d03907d6eda5a2a79ead0";

	//微信分配的公众号ID（开通公众号之后可以获取到）
	public static String appID = "wxce20c658c1eb222f";

	//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
	public static String mchID = "1286681401";
	
	public static String DEVICE_INFO="WEB";
	
	public static String nonce_str=RandomStringGenerator.getRandomStringByLength(32);
	
	public static String body="幸福家缘家政宝洁服务";
	
	public static String detail="幸福家缘家政宝洁服务";
	public static String attach="天津";
	public static String out_trade_no=RandomStringGenerator.getRandomStringByLength(32);
	public static String fee_type="CNY";
	public static String total_fee="1";
	public static String spbill_create_ip="192.18.0.2";
	public static String time_start=DateUtil.Time2Str(new Date());
	public static String time_expire=DateUtil.getDiffDate(1);
	public static String notify_url="http://weixin.tjxfjz.com/xfjy/client/paytest/";
	
	public static String url_loot="http://weixin.tjxfjz.com/xfjy/client/loot/";
	
	//受理模式下给子商户分配的子商户号
	private static String subMchID = "";

	//HTTPS证书的本地路径
	private static String certLocalPath = "";

	//HTTPS证书密码，默认密码等于商户号MCHID
	public  static String certPassword = "1286681401";

	//是否使用异步线程的方式来上报API测速，默认为异步模式
	private static boolean useThreadToDoReport = true;

	//机器IP
	private static String ip = "";

	//以下是几个API的路径：
	//1）被扫支付API
	public static String PAY_API = "https://api.mch.weixin.qq.com/pay/micropay";
	
	public static final String URL_PRE_PAY="https://api.mch.weixin.qq.com/pay/unifiedorder";

	//2）被扫支付查询API
	public static String PAY_QUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";

	//3）退款API
	public static String REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	//4）退款查询API
	public static String REFUND_QUERY_API = "https://api.mch.weixin.qq.com/pay/refundquery";

	//5）撤销API
	public static String REVERSE_API = "https://api.mch.weixin.qq.com/secapi/pay/reverse";

	//6）下载对账单API
	public static String DOWNLOAD_BILL_API = "https://api.mch.weixin.qq.com/pay/downloadbill";

	//7) 统计上报API
	public static String REPORT_API = "https://api.mch.weixin.qq.com/payitil/report";

	public static boolean isUseThreadToDoReport() {
		return useThreadToDoReport;
	}

	public static void setUseThreadToDoReport(boolean useThreadToDoReport) {
		Configure.useThreadToDoReport = useThreadToDoReport;
	}

	public static String HttpsRequestClassName = "com.tencent.common.HttpsRequest";

	public static void setKey(String key) {
		Configure.key = key;
	}

	public static void setAppID(String appID) {
		Configure.appID = appID;
	}

	public static void setMchID(String mchID) {
		Configure.mchID = mchID;
	}

	public static void setSubMchID(String subMchID) {
		Configure.subMchID = subMchID;
	}

	public static void setCertLocalPath(String certLocalPath) {
		Configure.certLocalPath = certLocalPath;
	}

	public static void setCertPassword(String certPassword) {
		Configure.certPassword = certPassword;
	}

	public static void setIp(String ip) {
		Configure.ip = ip;
	}

	public static String getKey(){
		return key;
	}
	
	public static String getAppid(){
		return appID;
	}
	
	public static String getMchid(){
		return mchID;
	}

	public static String getSubMchid(){
		return subMchID;
	}
	
	public static String getCertLocalPath(){
		return certLocalPath;
	}
	
	public static String getCertPassword(){
		return certPassword;
	}

	public static String getIP(){
		return ip;
	}

	public static void setHttpsRequestClassName(String name){
		HttpsRequestClassName = name;
	}

}
