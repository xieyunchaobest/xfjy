package com.xyc.proj.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	public static String format1="yyyyMMddHHmmss";
	
	public static String getNow() {
		Calendar cal = Calendar.getInstance();
		String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal
				.getTime());
		return now;
	}
	
	public static String getYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal
				.getTime());
		return yesterday;
	}
	
	public static String getToday() {
		Calendar cal = Calendar.getInstance();
		String today = new SimpleDateFormat("yyyy-MM-dd").format(cal
				.getTime());
		return today;
	}
	
	public static String getTomorrow() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		String today = new SimpleDateFormat("yyyy-MM-dd").format(cal
				.getTime());
		return today;
	}
	
	
	/**
	 * 按指定的格式将日期对象转换为字符串
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String to_char(Date date, String format) {
		if (date == null)
			return null;
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	

	/**
	 * 日期（精确到日）转字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String date2Str(Date date) {
		String str = "";
		if (null != date) {
			str = DateUtil.to_char(date, "yyyy-MM-dd");
		}
		return str;
	}
	
	/**
	 * 日期（精确到日）转字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String Time2Str(Date date) {
		if(date==null)return "";
		String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return str;
	}

	public static Date strToDate(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		   Date date = null;
		   try {
		    date = format.parse(str);
		   } catch (ParseException e) {
			   e.printStackTrace();
		   }
		   return date;
	}
	
	public static String Time2Str(Date date,String format) {
		if(date==null)return "";
		String str = new SimpleDateFormat(format).format(date);
		return str;
	}
	
	public static String getDiffDate(int hour) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, hour);
		String day = new SimpleDateFormat(format1).format(cal
				.getTime());
		return day;
	}
	
	
	public static String getTimeStamp() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(date);
		return str;
	}
	
	/**
	 * 获取指定日期前后的某天
	 * @param date
	 * @return
	 */
	public static String getDiffDate(Date date,int diff) {
	    Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    c.add(Calendar.DATE, diff);
	    Date myDate = c.getTime();
	    return date2Str(myDate);
	}
	
	public static Date getNextMonday(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DAY_OF_MONTH,-(week-2)+7);
        
        return cal.getTime();
    }
	
	public static void  main(String[] argv){
            String strDate = "2015-12-11";;
            Date date = strToDate(strDate);
          //  Date dateSunDay = getSunday(date);
            Date dateMonday = getNextMonday(date);
            System.out.println(dateMonday);
	}
	
}
