package com.bright.apollo.tool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Liujj
 * @date 2017年6月21日 下午3:51:41
 * 
 */
public class DateHelper {
	public final static String FORMATALL="yyyy-MM-dd HH:mm:ss";
	public final static String FORMATHOUR="HH:mm:ss";
	public final static String FORMAT="yyyy-MM-dd";
	public final static String FORMATMONTH="yyyy_MM";
	public final static String FORMATNOSIGN="yyyyMMdd";
	public final static String FORMATNWITHPOINT="yyyy.MM.dd";
	public final static String FORMATNALLWITHPOINT="yyyy,MM,dd,HH,mm";
	public static long startOfTodDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date=calendar.getTime();
		return date.getTime();
	}
	public static long endOfTodDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date=calendar.getTime();
		return date.getTime();
	}
	public static long getYesterday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date=calendar.getTime();
		return date.getTime()-24*60*60*1000 ;
	}
	public static long getWeekdayStart() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date=calendar.getTime();
		return date.getTime()-24*60*60*1000*6 ;
	}
	public static long getYesterdayStart() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date=calendar.getTime();
		return date.getTime()-24*60*60*1000 ;
	}
	public static long getYesterdayEnd() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date=calendar.getTime();
		return date.getTime()-24*60*60*1000 ;
	}
	public static long getMonthEnd() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date=calendar.getTime();
		return date.getTime()-30*24*60*60*1000l ;
	}
	public static long getTomorrow() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date=calendar.getTime();
		return date.getTime()+24*60*60*1000 ;
	}
	public static String formatDate(long time,String format){
		SimpleDateFormat dateFormater = new SimpleDateFormat(format);
		return dateFormater.format(new Date(time));
	}
	public static void main(String[] args) {
		System.out.println(formatDate(1546069552695l, DateHelper.FORMATALL));
	}
}
