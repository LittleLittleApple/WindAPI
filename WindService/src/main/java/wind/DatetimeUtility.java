package wind;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatetimeUtility {

	public static Date tradeStartTime(Date date) {

		Calendar cal = Calendar.getInstance();
		
		cal.setTime(date);
		
		if ((cal.get(Calendar.HOUR_OF_DAY) == 9)
				&& (cal.get(Calendar.MINUTE) == 0)
				&& (cal.get(Calendar.SECOND) == 0)) {
			cal.add(Calendar.HOUR_OF_DAY, 9);
			cal.add(Calendar.MINUTE, 30);
			return cal.getTime();
		} else {
			Date date2 = new Date(date.getTime()
					- cal.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000
					- cal.get(Calendar.MINUTE) * 60 * 1000
					- cal.get(Calendar.SECOND) * 1000);
			cal.setTime(date2);
			cal.add(Calendar.HOUR_OF_DAY, 9);
			cal.add(Calendar.MINUTE, 30);
			return cal.getTime();
		}

	}

	public static Date tradeEndTime(Date date) {

		Calendar cal = Calendar.getInstance();
		Date startOfDay = tradeStartTime(date);
		cal.setTime(startOfDay);
		cal.add(Calendar.HOUR_OF_DAY, 5);
		cal.add(Calendar.MINUTE, 30);
//		Date date2 = new Date(cal.getTime().getTime() + 6 * 60 * 60 * 1000
//				- 1000);
		return cal.getTime();
	}

	public static Date firstDateOfWeek(Date qryDate) {
		Calendar cal = new GregorianCalendar();  
		cal.setTime(qryDate);
		cal.set(Calendar.DAY_OF_WEEK, 2);
		return cal.getTime();
	}
	
	public static Date lastDateOfWeek(Date qryDate) {
		Calendar cal = new GregorianCalendar();  
		cal.setTime(qryDate);
		cal.set(Calendar.DAY_OF_WEEK, 6);
		return cal.getTime();
	}
	
	public static Date firstDateOfMonth(Date qryDate) {
		Calendar cal = new GregorianCalendar();  
		cal.setTime(qryDate);
		cal.set(Calendar.DAY_OF_MONTH, 1);  
		return cal.getTime();
	}
	
	public static Date lastDateOfMonth(Date qryDate) {
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(qryDate);
		cal.add(Calendar.MONTH, 1);  
		cal.set(Calendar.DAY_OF_MONTH, 0);
		return cal.getTime();
	}

	public static boolean isToday(String date) throws ParseException {
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

		Date qryDate = null;
		try {
			qryDate = format1.parse(date);
		} catch (ParseException e) {
			qryDate = format2.parse(date);
		}
		return isSameDate(new Date(), qryDate);
	}

	public static boolean isTradeBegan(String date) throws ParseException {
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

		Date qryDate = null;
		try {
			qryDate = format1.parse(date);
		} catch (ParseException e) {
			qryDate = format2.parse(date);
		}
		Date tradeStart = tradeStartTime(new Date()); //今天交易开始时间
		return qryDate.after(tradeStart);
	}

	
	public static boolean isTradeFinished(String date) throws ParseException {
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

		Date qryDate = null;
		try {
			qryDate = format1.parse(date);
		} catch (ParseException e) {
			qryDate = format2.parse(date);
		}
		return qryDate.after(tradeEndTime(new Date()));
	}
	
	public static boolean isTradeFinished(Date qryDate) throws ParseException {
		return qryDate.after(tradeEndTime(new Date()));
	}

	

	/**
	 * 判断两个日期是否是同一天
	 * 
	 * @param date1
	 *            date1
	 * @param date2
	 *            date2
	 * @return
	 */
	public static boolean isSameDate(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
		boolean isSameMonth = isSameYear
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		boolean isSameDt = isSameMonth
				&& cal1.get(Calendar.DAY_OF_MONTH) == cal2
						.get(Calendar.DAY_OF_MONTH);

		return isSameDt;
	}
	
	public static Date tryParseDate(String sQryDate) throws ParseException {
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		
		Date qryDate = null;
		try{
			qryDate = format1.parse(sQryDate);
		}catch (ParseException e){
			qryDate = format2.parse(sQryDate);
		}
		return qryDate;
	}
	
	public static String date2WindString(Date dt) {
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		return format2.format(dt);
	}
}