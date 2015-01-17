import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import wind.KType;
import wind.PriceAdjust;
import wind.WindErrorResponse;
import wind.WindService;


public class SyncKDataTestCase {
	
	private final WindService ws = new WindService();
	private static final String KDATA_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	@Test
	public void testImportStock() throws IOException, ParseException, InterruptedException, WindErrorResponse, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		DateFormat format1 = new SimpleDateFormat(KDATA_DATE_FORMAT);  
		Date begin = null;
		Date end = null;
		
		begin = format1.parse("2015-01-15 01:20:31");
		end = format1.parse("2015-01-15 15:38:31");		
		List<String> stockCodes = new ArrayList<String>();
		stockCodes.add("000001.SZ");
//		stockCodes.add("000002.SZ");
//		ws.syncKData(stockCodes, begin, end, KType.ONE_MIN_KTYPE, PriceAdjust.NONE);
		ws.syncKData(stockCodes, begin, end, KType.ONE_MIN_KTYPE, PriceAdjust.NONE);
		
//		"000001.SZ" "2014-11-15 00:38:31" "2014-11-17 02:38:31" "Period=D;" "PriceAdj=F;" 
		begin = format1.parse("2015-01-12 00:38:31");
		end = format1.parse("2015-01-15 02:38:31");
//		ws.syncKData(stockCodes, begin, end, KType.DAY_KTYPE, PriceAdjust.NONE);
		
	}
	@Test
	public void test1() throws IOException, ParseException, InterruptedException, WindErrorResponse, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		DateFormat format1 = new SimpleDateFormat(KDATA_DATE_FORMAT);        
		Date begin = format1.parse("2014-12-03 01:20:31");
		Date end = format1.parse("2014-12-03 15:38:31");
		
		List<String> stockCodes = new ArrayList<String>();
		stockCodes.add("000001.SZ");
		stockCodes.add("000002.SZ");
		ws.syncKData(stockCodes, begin, end, KType.ONE_MIN_KTYPE, PriceAdjust.NONE);
		
	}
	
	@Test
	public void test5() throws IOException, ParseException, InterruptedException, WindErrorResponse, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		DateFormat format1 = new SimpleDateFormat(KDATA_DATE_FORMAT);        
		Date begin = format1.parse("2014-12-03 15:18:31");
		Date end = format1.parse("2014-12-03 15:38:31");
		
		List<String> stockCodes = new ArrayList<String>();
		stockCodes.add("000001.SZ");
		stockCodes.add("000002.SZ");
		ws.syncKData(stockCodes, begin, end, KType.FIVE_MIN_KTYPE, PriceAdjust.NONE);
	}
	
	@Test
	public void test30() throws IOException, ParseException, InterruptedException, WindErrorResponse, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		DateFormat format1 = new SimpleDateFormat(KDATA_DATE_FORMAT);        
		Date begin = format1.parse("2014-12-01 15:08:31");
		Date end = format1.parse("2014-12-01 15:38:31");
		
		List<String> stockCodes = new ArrayList<String>();
		stockCodes.add("000001.SZ");
		stockCodes.add("000002.SZ");
		ws.syncKData(stockCodes, begin, end, KType.THIRDTY_MIN_KTYPE, PriceAdjust.NONE);
		
		begin = format1.parse("2014-09-17 01:38:31");
		end = format1.parse("2014-11-17 15:38:31");
		ws.syncKData(stockCodes, begin, end, KType.MONTH_KTYPE, PriceAdjust.NONE);
	}
	
	@Test
	public void test1d() throws IOException, ParseException, InterruptedException, WindErrorResponse, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		DateFormat format1 = new SimpleDateFormat(KDATA_DATE_FORMAT);        
		Date begin = format1.parse("2014-11-10 15:08:31");
		Date end = format1.parse("2014-11-17 15:38:31");
		
		List<String> stockCodes = new ArrayList<String>();
		stockCodes.add("000001.SZ");
		stockCodes.add("000002.SZ");
		ws.syncKData(stockCodes, begin, end, KType.DAY_KTYPE, PriceAdjust.NONE);
		
	}
	
	@Test
	public void test1w() throws IOException, ParseException, InterruptedException, WindErrorResponse, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		DateFormat format1 = new SimpleDateFormat(KDATA_DATE_FORMAT);        
		Date begin = format1.parse("2014-11-10 15:08:31");
		Date end = format1.parse("2014-11-28 15:38:31");
		
		List<String> stockCodes = new ArrayList<String>();
		stockCodes.add("000001.SZ");
		stockCodes.add("000002.SZ");
		ws.syncKData(stockCodes, begin, end, KType.WEEK_KTYPE, PriceAdjust.NONE);
	}
	
	@Test
	public void test1m() throws IOException, ParseException, InterruptedException, WindErrorResponse, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		DateFormat format1 = new SimpleDateFormat(KDATA_DATE_FORMAT);        
		Date begin = format1.parse("2014-08-10 15:08:31");
		Date end = format1.parse("2014-11-28 15:38:31");
		
		List<String> stockCodes = new ArrayList<String>();
		stockCodes.add("000001.SZ");
		stockCodes.add("000002.SZ");
		ws.syncKData(stockCodes, begin, end, KType.MONTH_KTYPE, PriceAdjust.NONE);
	}

}
