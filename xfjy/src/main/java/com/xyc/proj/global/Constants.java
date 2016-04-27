package com.xyc.proj.global;

import com.xyc.proj.pay.Configure;

/**
 * 一个全局的字符串资源映射入口
 * @author tongshulian
 * @since 2015.7.14
 *
 */
public class Constants {
	public static final String URL_PRE_PAY="https://api.mch.weixin.qq.com/pay/unifiedorder";
	//public static final String certLocalPath="/home/diuser/resource/apiclient_cert.p12";
	public static  String certLocalPath="";	 
	
	public static   String URL_GET_TOKEN="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+Configure.appID+"&secret="+Configure.WE_CHAT_APPSECRET;
	public static   String URL_CREATE_MENU=" https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";
	public static   String URL_SEND_TEMPLEATE_MSG="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
	public static   String MSG_KF_TEMPLATE_ID="3uFcnQniJnbO-W_JNakYo9gEiZlsNESM6Iv0d4-r7nk";
	public static   String WE_CHAT_ACCESS_TOKEN="vkb4LUxANNpSFQdQ4wU0oQgf9pGTGbShyU0wO9iZeSNgT0NxHZ6PGAZuRdk07DfOZ2krnhsBWvICTMDq7Ky0LaAQZXGQH-02SnfXjSSx5yIDZVcAHAGCY";
	
	
	
	
	
	// baseDao中获取mybatis配置文件中mapper的查询语句id的前缀
	public static final String MYBATIS_MAPPER_PRIX = "com.xyc.proj.";
	
	// mybatis配置文件路径
	public static String MYBATIS_CONFIG_FILE_PATH = "classpath:/mappers/*.xml";
	public static final String MENU_MANAGE = "manage";
	
	
	public static final String MENU_QUERY = "query";
	
	//UPDATE OR ADD
	public static final String OPT_FLAG_ADD="A";
	public static final String OPT_FLAG_UPDATE="U";
	
	public static final String YES="Y";
	public static final String NO="N";
	
	public static final int DEFAULT_PAGE_SIZE=15;
	
	public static final String ORDERBY="orderBys";
	public static final String CURRENT_PAGENUM="currentPageNum";
	
	public static final String STATE_P="P";
	public static final String STATE_A="A";
	
	public static final String SERVICE_TYPE_CC="CC";//普通宝洁
	public static final String SERVICE_TYPE_DBJ="DBJ";//大宝洁
	public static final String SERVICE_TYPE_KH="KH";//开荒
	public static final String SERVICE_TYPE_CBL="CBL";//插玻璃
	
	public static final String CYCLE_TYPE_SG="SG";//散工
	public static final String CYCLE_TYPE_BY="BY";//包月
	
	public static final String CONFIG_PTBJDJ="PTBJDJ";//普通宝洁单价
	public static final String CONFIG_DBJDJ="DBJDJ";//大宝洁单价
	public static final String CONFIG_CNCDJ="CNCDJ";//插内窗单价
	public static final String CONFIG_CYTDJ="CYTDJ";//插阳台单价
	public static final String CONFIG_KHDJ="KHDJ";//开荒单价
	
	public static final String CONFIG_CLEAN_TOOLS_FEE4PTBJ="QJGJFY4PTBJ";//普通清洁工具费用
	public static final String CONFIG_CLEAN_TOOLS_FEE4DBJ="QJGJFY4DBJ";//大宝洁工具费用
	public static final String CONFIG_CLEAN_TOOLS_FEE4CBL="QJGJFY4CBL";//插播里工具费用
	public static final String CONFIG_CLEAN_TOOLS_FEE4KH="QJGJFY4KH";//开荒里工具费用
	
	
	public static final String ORDER_STATE_UNPAY="U";//未支付
	public static final String ORDER_STATE_PAYED="P";//已支付
	public static final String ORDER_STATE_CONFIRMED="C";//已经确认
	public static final String ORDER_STATE_FINISH="F";//完成
	public static final String ORDER_STATE_PROBATION="S";//试用
	
	public static final String ORDER_PAY_MODE_ONLY_WECHAT="OW";//支付方式，只有微信
	public static final String ORDER_PAY_MODE_ONLY_YUE="OY";//只有余额
	public static final String ORDER_PAY_MODE_ONLY_COUPON="OC";//只有优惠券
	public static final String ORDER_PAY_MODE_WECHAT_YUE="WY";//威信+余额
	public static final String ORDER_PAY_MODE_WECHAT_COUPON="WC";//威信+y优惠券
	
	
	public static final String EDUCATIONAL_LEVEL_MIDDLE="M";//中学以及以下
	public static final String EDUCATIONAL_LEVEL_ZK="Z";//专科
	public static final String EDUCATIONAL_LEVEL_BK="B";//本科
	public static final String EDUCATIONAL_LEVEL_SS="S";//硕士
	
	
	public static final String WORK_SERVICE_TYPE_CLEAN="C";//宝洁
	public static final String WORK_SERVICE_TYPE_JZ="J";//家政
	
	public static final String WORK_SERVICE_JZY="JZY";//家政员
	public static final String WORK_SERVICE_YY_YYS="YYS";//育婴师
	public static final String WORK_SERVICE_YY_YS="YS";//月嫂
	public static final String WORK_SERVICE_YL="YL";//养老
	public static final String WORK_SERVICE_XSG="XSG";//小时工
	public static final String WORK_SERVICE_YYHG="YYHG";//医院护工
	
	public static final String WORK_ROLE_ROLE_AY="A";//阿姨
	public static final String WORK_ROLE_ROLE_TEACHER="T";//老师
	public static final String WORK_ROLE_ROLE_DIANZHANG="D";//店长
	public static final String WORK_ROLE_ROLE_XINGZHENG="X";//行政
	public static final String WORK_ROLE_ROLE_CAIWU="C";//财务
	public static final String WORK_ROLE_ROLE_GUANLIYUAN="M";//管理员
	
	public static final String WORK_TIME_DAY="D";//白班
	public static final String WORK_TIME_24H="H";//24XIAOSHI
	
	public static final String COUPON_TYPE_CASH="C";//
	public static final String COUPON_TYPE_DISCOUNT="D";
	
	public static final String BLOOD_TYPE_A="A";
	public static final String BLOOD_TYPE_B="B";
	public static final String BLOOD_TYPE_AB="AB";
	public static final String BLOOD_TYPE_O="O";
	
	
	public static String key = "";
	public static String wechatkey="";
	public static String shanghu_key = "!QAZ@WSX#EDC$RFV%TGB^YHN&UJM";  
}
