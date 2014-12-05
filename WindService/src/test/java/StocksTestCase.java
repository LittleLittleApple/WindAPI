import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import wind.WindErrorResponse;
import wind.WindService;

public class StocksTestCase {
	
	private final WindService ws = new WindService();

	@Test
	public void test() throws InterruptedException, WindErrorResponse,
			IOException {

		
		// 获取全部A股的市场代码
		Map<String, String> stockMap = ws.getStocks("全部A股");
		Assert.assertEquals("平安银行", stockMap.get("000001.SZ"));
		Assert.assertEquals("贵人鸟", stockMap.get("603555.SH"));
		

		Map<String, String> stockMap2 = ws.getStocks("上证A股");
		Assert.assertTrue(stockMap2.keySet().size() > 0);
		Assert.assertEquals("浦发银行", stockMap2.get("600000.SH"));
		

		Map<String, String> stockMap3 = ws.getStocks("全部上市公司");
		Assert.assertTrue(stockMap3.keySet().size() > 0);
		Assert.assertEquals("浦发银行", stockMap3.get("600000.SH"));
		

		Map<String, String> stockMap4 = ws.getStocks("全部B股");
		Assert.assertEquals("深物业B", stockMap4.get("200011.SZ"));

		
	}
	
	@Test
	public void testH()  throws InterruptedException, WindErrorResponse,
	IOException {
		// It seems that wind does not support for HK.
		 Map<String, String> stockHKMap = ws.getStocks("全部港股");
		 Assert.assertTrue(stockHKMap.keySet().size() == 0);
	}
	
	@Test
	public void testNASDAQ()  throws InterruptedException, WindErrorResponse,
	IOException {
		// It seems that wind does not support for NASDAQ.
		 Map<String, String> stockHKMap = ws.getStocks("NASDAQ全部股票");
		 Assert.assertTrue(stockHKMap.keySet().size() == 0);
	}

}
