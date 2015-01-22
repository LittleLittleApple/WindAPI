import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import wind.StockSuspendData;
import wind.WindErrorResponse;
import wind.WindService;


public class TradeSuspendTestCase {

	private final WindService ws = new WindService();
	
	@Test
	public void testStockData() throws ParseException, IOException, InterruptedException, WindErrorResponse, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		StockSuspendData ssd =  null;

		//测试000001.SZ不为停牌状态
		ssd =  ws.getSuspendData("000001.SZ", "2014-06-12 01:38:31");
		Assert.assertFalse(ssd.isSuspended());
		
		
		//测试000798的停牌和复牌头盔
		//TODO: 这个000798.SZ明明已经在2014-11-26日停牌， 但python的接口查不出来.
		ssd =  ws.getSuspendData("000798.SZ", "2014-11-27 01:38:31");
		Assert.assertNotNull(ssd);
		Assert.assertTrue(ssd.isSuspended());

		ssd =  ws.getSuspendData("000798.SZ", "2014-12-23 01:38:31");
		Assert.assertFalse(ssd.isSuspended());
		
		ssd =  ws.getSuspendData("000005.SZ", "2015-01-23 01:38:31");
		Assert.assertFalse(ssd.isSuspended());
		
		ssd =  ws.getSuspendData("000005.SZ", "2016-01-23 01:38:31");
		Assert.assertFalse(ssd.isSuspended());
	}
	
}
