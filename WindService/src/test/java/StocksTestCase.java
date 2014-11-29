import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import wind.WindErrorResponse;
import wind.WindService;

public class StocksTestCase {

	@Test
	public void test() throws InterruptedException, WindErrorResponse,
			IOException {

		WindService ws = new WindService();
		// 获取全部A股的市场代码
		Map<String, String> stockMap = ws.getStocks("全部A股");
		Assert.assertEquals("平安银行", stockMap.get("000001.SZ"));
		Assert.assertEquals("贵人鸟", stockMap.get("603555.SH"));

		// 获取全部A股的市场代码
		// It seems not support for HK and TW.
		// Map<String, String> stockHKMap = ws.getStocks("H股");
		// Assert.assertEquals("平安银行", stockHKMap.get("000001.SZ"));
		// Assert.assertEquals("贵人鸟", stockHKMap.get("603555.SH"));
	}

}
