package wind;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Case;

//单个股票K线数据(采样点)
public class SingleStockData {

	private String stockCode = null;
	private int ktype = -1;
	private int priceAdj = -1;
	private Date w_time = null; 
	private double open = -1d; 
	private double high = -1d; 
	private double low = -1d;
	private double close = -1d; 
	private double volume = -1d; 
	private double amt = -1d; 
	private double ma5 = -1d;
	private double ma10 = -1d;
	private double ma20 = -1d;
	private double adjfactor = -1d;
	private double max_adjfactor = -1d;
	private Date createdAt = null; 
	private Date updatedAt = null;
	
	private static final int MULTIPLIEDFORSQL = 1000000;
	private static final String KDATA_DATE_FORMAT1 = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final String KDATA_DATE_FORMAT2 = "yyyy-MM-dd HH:mm:ss";
	private static final String KDATA_DATE_FORMAT3 = "yyyyMMdd HH:mm:ss";
	
	private DateFormat kdateDateFormat1 = new SimpleDateFormat(KDATA_DATE_FORMAT1);
	private DateFormat kdateDateFormat2 = new SimpleDateFormat(KDATA_DATE_FORMAT2);
	private DateFormat kdateDateFormat3 = new SimpleDateFormat(KDATA_DATE_FORMAT3);

	public SingleStockData(Map<String, String> stockData,Integer priceAdj) throws ParseException {
		this.stockCode = stockData.get("stockCode");
		this.ktype = Integer.parseInt(stockData.get("ktype"));
		this.priceAdj = priceAdj;
		this.w_time =  tryParse(stockData.get("w_time"));
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
		this.max_adjfactor = parseStockValue(stockData.get("max_adjfactor"));
		this.createdAt =  tryParse(stockData.get("createdAt"));
		this.updatedAt =  tryParse(stockData.get("updatedAt"));
	}
	
	public Date tryParse(String v) throws ParseException {
		try {
			return kdateDateFormat1.parse(v);

		} catch (ParseException ex) {
			try {
				return kdateDateFormat2.parse(v);
			} catch (ParseException exx) {
				return kdateDateFormat3.parse(v);
			}
		}
	}
	
	private double parseStockValue(String stockValue) {
		double iRes = -1d;
		if( stockValue == null || stockValue.isEmpty()) {
			return iRes;
		}
		try {
			Double dValue = Double.parseDouble(stockValue);
			iRes = (double) dValue / MULTIPLIEDFORSQL;
		}catch(NumberFormatException e){
			System.err.println(e.getMessage());
		}
		return iRes;
	}
	
	public Double[] toRawArray(){
		List<Double> resLst = toRawList();
		Double[] res = new Double[resLst.size()];
		return resLst.toArray(res);
	}
	
	public List<Double> toRawList(){
		List<Double> resLst = new ArrayList<Double>();

		resLst.add(getWTimestamp());
		resLst.add(getOpen());
		resLst.add(getHigh());
		resLst.add(getLow());
		resLst.add(getClose());
		resLst.add(getMa5());
		resLst.add(getMa10());
		resLst.add(getMa20());
		resLst.add(getAmt());
		resLst.add(getVolume());
		return resLst;
	}
	
	private double parseAdj(double v) {
		double resV= v;
		if(v == -1d) {
			return resV;
		}else{
			
			switch (priceAdj) {
			case 1:
				resV = v * (this.adjfactor / this.max_adjfactor);
				break;
			case 2:
				resV = v * this.adjfactor;
				break;

			default:
				resV = v;
				break;
			}
			return resV;
		}
	}
	
	public String getStockCode() {
		return stockCode;
	}
	
	public String getKtype() {
		return Integer.toString(ktype);
	}

	public double getWTimestamp() {
		long v = w_time.getTime();
		if(v == -1l) {
			return -1d;
		}else{
			return (double) v ;
		}
	}
	
	public double getOpen() {
		return parseAdj(open);
	}
	
	public double getHigh() {
		return  parseAdj(high);
	}
	
	public double getLow() {
		return  parseAdj(low);
	}
	
	public double getClose() {
		return  parseAdj(close);
	}
	
	public double getVolume() {
		return  parseAdj(volume);
	}
	
	public double getAmt() {
		return  parseAdj(amt);
	}
	
	public double getMa5() {
		return  parseAdj(ma5);
	}
	
	public double getMa10() {
		return  parseAdj(ma10);
	}
	
	public double getMa20() {
		return ma20;
	}
	
	public Date getWTime() {
		return w_time;
	}
	
	public double getAdjfactor() {
		return adjfactor;
	}

	public double getMaxAdjfactor() {
		return max_adjfactor;
	}

	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public Date getUpdatedAt() {
		return updatedAt;
	}

}
