package com.xyc.proj.global;

/**
 * 一个全局的字符串资源映射入口
 * @author tongshulian
 * @since 2015.7.14
 *
 */
public interface Constants {
	
	// baseDao中获取mybatis配置文件中mapper的查询语句id的前缀
	public static final String MYBATIS_MAPPER_PRIX = "com.xyc.proj.";
	
	// mybatis配置文件路径
	String MYBATIS_CONFIG_FILE_PATH = "classpath:/mappers/*.xml";
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
	
	public static final String EDUCATIONAL_LEVEL_MIDDLE="M";//中学以及以下
	public static final String EDUCATIONAL_LEVEL_ZK="Z";//专科
	public static final String EDUCATIONAL_LEVEL_BK="B";//本科
	public static final String EDUCATIONAL_LEVEL_SS="S";//硕士
	
	
	public static final String WORK_SERVICE_TYPE_CLEAN="C";//宝洁
	public static final String WORK_SERVICE_YS="YS";//YUE SHAO
	public static final String WORK_SERVICE_YY_YE="YY";//孕婴&育儿嫂
	public static final String WORK_SERVICE_YL="YY";//养老护工
	public static final String WORK_SERVICE_JZ="JZ";//家政&小时工
	
	public static final String WORK_ROLE_ROLE_AY="A";//阿姨
	public static final String WORK_ROLE_ROLE_TEACHER="T";//老师
	
	public static final String WORK_TIME_DAY="D";//白班
	public static final String WORK_TIME_24H="H";//24XIAOSHI
	
}
