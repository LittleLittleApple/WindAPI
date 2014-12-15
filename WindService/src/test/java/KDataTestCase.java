import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import wind.KType;
import wind.WindErrorResponse;
import wind.WindService;


public class KDataTestCase {

	private final WindService ws = new WindService();
	private static final String KDATA_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	@Test
	public void test() throws IOException, ParseException, InterruptedException, WindErrorResponse {
		
		
		//获取000001.SZ的K线信息， 其中KType有六种
		DateFormat format1 = new SimpleDateFormat(KDATA_DATE_FORMAT);        
		Date begin = format1.parse("2014-11-17 01:38:31");
		Date end = format1.parse("2014-11-17 15:38:31");
		
		List<Map<String, String>> res =  ws.getKData("000001.SZ", begin, end, KType.THIRDTY_MIN_KTYPE);
		Assert.assertTrue(res.size() > 0);
		//w_time=20141117 10:00:00
		Map<String, String> kdata = res.get(0);
		Assert.assertEquals("20141117 10:00:00", kdata.get("w_time"));
		Assert.assertEquals("2014-11-17 10:00:00.005000", kdata.get("time"));
		Assert.assertEquals("000001.SZ", kdata.get("windcode"));
		Assert.assertEquals("10.95", kdata.get("open"));
		Assert.assertEquals("15855947", kdata.get("volume"));
		Assert.assertEquals("172390777.36", kdata.get("amt"));
		Assert.assertEquals("10.99", kdata.get("high"));
		Assert.assertEquals("10.8", kdata.get("low"));
		Assert.assertEquals("10.85", kdata.get("close"));
		
		List<Map<String, String>> dateRes = ws.getKData("000001.SZ", begin, end, KType.DAY_KTYPE);
		Assert.assertTrue(dateRes.size() > 0);
		Map<String, String> dateKData = dateRes.get(0);
		Assert.assertEquals("20141117", dateKData.get("w_time"));
		Assert.assertEquals("10.95", dateKData.get("open"));
		Assert.assertEquals("88959448.0", dateKData.get("volume"));
		Assert.assertEquals("961843455.8", dateKData.get("amt"));
		Assert.assertEquals("10.99", dateKData.get("high"));
		Assert.assertEquals("10.71", dateKData.get("low"));
		Assert.assertEquals("10.76", dateKData.get("close"));
		
		begin = format1.parse("2014-11-17 01:38:31");
		end = format1.parse("2014-12-17 15:38:31");
		List<Map<String, String>> dateRes2 = ws.getKData("000001.SZ", begin, end, KType.MONTH_KTYPE);
		Assert.assertTrue(dateRes2.size() > 0);

	}
	
	@Test
	public void test_monthly() throws IOException, ParseException, InterruptedException, WindErrorResponse {
		DateFormat format1 = new SimpleDateFormat(KDATA_DATE_FORMAT);        
		Date begin = format1.parse("2014-11-17 01:38:31");
		Date end = format1.parse("2014-12-17 15:38:31");
		List<Map<String, String>> dateRes = ws.getKData("000001.SZ", begin, end, KType.MONTH_KTYPE);
		Assert.assertTrue(dateRes.size() > 0);
	}
	

	
	@Test
	public void testHK_monthly() throws IOException, ParseException, InterruptedException, WindErrorResponse {
		DateFormat format1 = new SimpleDateFormat(KDATA_DATE_FORMAT);        
		Date begin = format1.parse("2014-11-17 01:38:31");
		Date end = format1.parse("2014-12-17 15:38:31");
		List<Map<String, String>> dateRes = ws.getKData("00872.HK", begin, end, KType.MONTH_KTYPE);
		Assert.assertTrue(dateRes.size() == 0);
	}
	
	
	@Test
	public void testNASDAQ() throws IOException, ParseException, InterruptedException, WindErrorResponse {
		
		
		//获取AAL.O的K线信息， 其中KType有六种
		DateFormat format1 = new SimpleDateFormat(KDATA_DATE_FORMAT);        
		Date begin = format1.parse("2014-10-17 01:38:31");
		Date end = format1.parse("2014-11-16 15:38:31");
		
		List<Map<String, String>> res =  ws.getKData("AAL.O", begin, end, KType.DAY_KTYPE);
		Assert.assertTrue(res.size() > 0);
		Map<String, String> kdata = res.get(0);
		Assert.assertEquals("20141017", kdata.get("w_time"));
		Assert.assertEquals("33.88", kdata.get("open"));
		Assert.assertEquals("15985070.0", kdata.get("volume"));
		Assert.assertEquals("nan", kdata.get("amt"));
		Assert.assertEquals("34.34", kdata.get("high"));
		Assert.assertEquals("32.81", kdata.get("low"));
		Assert.assertEquals("33.38", kdata.get("close"));

	}
	
	@Test
	public void testHK() throws IOException, ParseException, InterruptedException, WindErrorResponse {
		
		
		//获取0001.HK的K线信息， 其中KType有六种
		DateFormat format1 = new SimpleDateFormat(KDATA_DATE_FORMAT);        
		Date begin = format1.parse("2014-11-16 01:38:31");
		Date end = format1.parse("2014-12-16 15:38:31");
		
		List<Map<String, String>> res =  ws.getKData("0001.HK", begin, end, KType.DAY_KTYPE);
		Assert.assertTrue(res.size() > 0);
		Map<String, String> kdata = res.get(0);
		Assert.assertEquals("20141117", kdata.get("w_time"));
		Assert.assertEquals("142.0", kdata.get("open"));
		Assert.assertEquals("2206421.0", kdata.get("volume"));
		Assert.assertEquals("309412240.0", kdata.get("amt"));
		Assert.assertEquals("142.2", kdata.get("high"));
		Assert.assertEquals("139.3", kdata.get("low"));
		Assert.assertEquals("139.5", kdata.get("close"));

	}

}
