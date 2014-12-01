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
		Assert.assertTrue(market.keySet().contains("w_time"));
		Assert.assertTrue(market.keySet().contains("rt_open"));
		Assert.assertTrue(market.keySet().contains("rt_last"));
		Assert.assertTrue(market.keySet().contains("rt_vol"));
		Assert.assertTrue(market.keySet().contains("rt_pct_chg"));
		Assert.assertTrue(market.keySet().contains("rt_amt"));
		Assert.assertTrue(market.keySet().contains("rt_high"));
		Assert.assertTrue(market.keySet().contains("rt_low"));
	}

}
