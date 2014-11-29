import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import wind.WindErrorResponse;
import wind.WindService;


public class MarketTestCase {

	@Test
	public void test() throws IOException, InterruptedException, WindErrorResponse {
		
		WindService ws = new WindService();
		
		//获取000001.SZ的当前行情
		Map<String, String> market = ws.getCurrentMarket("000001.SZ");
		Assert.assertTrue(market.keySet().size() > 1);
//		Assert.assertEquals("11.27", market.get("rt_open"));
//		Assert.assertEquals("12.44", market.get("rt_last"));
//		Assert.assertEquals("484483936.0", market.get("rt_vol"));
//		Assert.assertEquals("0.0999", market.get("rt_pct_chg"));
//		Assert.assertEquals("5793392235.0", market.get("rt_amt"));
//		Assert.assertEquals("12.44", market.get("rt_high"));
//		Assert.assertEquals("11.18", market.get("rt_low"));
//		Assert.assertEquals("20141129 23:14:19", market.get("w_time"));

	}

}
