package wind;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

//单个股票K线数据(采样点)
public class SingleStockSQLData {

	private String stockCode = null;
	private int ktype = -1;
	private Date w_time = null; 
	private long open = -1; 
	private long high = -1; 
	private long low = -1;
	private long close = -1; 
	private long volume = -1; 
	private long amt = -1; 
	private long ma5 = -1;
	private long ma10 = -1;
	private long ma20 = -1;
	private long adjfactor = -1;
	private Date createdAt = null; 
	private Date updatedAt = null;
	
	private static final int MULTIPLIEDFORSQL = 1000000;
	private static final String KDATA_DATE_FORMAT = "yyyyMMdd HH:mm:ss";
	private static final String KDATA_DATE_FORMAT2 = "yyyy-MM-dd HH:mm:ss";
	
	private DateFormat kdateDateFormat = new SimpleDateFormat(KDATA_DATE_FORMAT);
	private DateFormat kdateDateFormat2 = new SimpleDateFormat(KDATA_DATE_FORMAT2);

	public SingleStockSQLData(Map<String, String> stockData) throws ParseException {
		Date today = new Date();
		this.stockCode = stockData.get("stockCode");
		this.ktype = Integer.parseInt(stockData.get("ktype"));
		try {
			this.w_time = kdateDateFormat.parse(stockData.get("w_time"));
		} catch (ParseException ex) {
			this.w_time = kdateDateFormat2.parse(stockData.get("w_time"));
		}
		this.open =  parseStockValue(stockData.get("open"));
		this.high = parseStockValue(stockData.get("high"));
		this.low = parseStockValue(stockData.get("low"));
		this.close = parseStockValue(stockData.get("close"));
		this.volume = parseStockValue(stockData.get("volume"));
		this.amt = parseStockValue(stockData.get("amt"));
		this.ma5 = parseStockValue(stockData.get("ma5"));
		this.ma10 = parseStockValue(stockData.get("ma10"));
		this.ma20 = parseStockValue(stockData.get("ma20"));
		this.adjfactor = parseStockValue(stockData.get("adjfactor"));
		this.createdAt =  new java.sql.Date(today.getTime());
		this.updatedAt =  new java.sql.Date(today.getTime());
	}
	
	
	
	private long parseStockValue(String stockValue) {
		long iRes = -1l;
		if( stockValue == null || stockValue.isEmpty()) {
			return iRes;
		}
		try {
			Double dValue = Double.parseDouble(stockValue);
			iRes = (long) (dValue * MULTIPLIEDFORSQL);
		}catch(NumberFormatException e){
			System.err.println(e.getMessage());
		}
		return iRes;
	}
		
	//long value for SQL database
	
	public String getStockCode() {
		return stockCode;
	}

	
	public Integer getKtype() {
		return ktype;
	}	

	
	public Date getWTime() {
		return w_time;
	}

	
	public long getWTimestamp() {
		return w_time.getTime();
	}
	

	public Integer getYearMonth() {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(w_time);
		//Jan is 0
		return cal1.get(Calendar.YEAR) * 100 + cal1.get(Calendar.MONTH) + 1;
	}

	public Integer getYearWeek() {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(w_time);
		return cal1.get(Calendar.YEAR) * 100 + cal1.get(Calendar.WEEK_OF_YEAR);
	}
	

	public Integer getYearDate() {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(w_time);
		return cal1.get(Calendar.YEAR) * 10000 + cal1.get(Calendar.MONTH) * 100 + cal1.get(Calendar.DATE);
	}
	
	public long getOpen() {
		return open;
	}
	
	public long getHigh() {
		return high;
	}
	
	public long getLow() {
		return low;
	}
	
	public long getClose() {
		return close;
	}
	
	public long getVolume() {
		return volume;
	}
	
	public long getAmt() {
		return amt;
	}
	
	public long getMa5() {
		return ma5;
	}
	
	public long getMa10() {
		return ma10;
	}
	
	public long getMa20() {
		return ma20;
	}
	
	public long getAdjfactor() {
		return adjfactor;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public Date getUpdatedAt() {
		return updatedAt;
	}

}
