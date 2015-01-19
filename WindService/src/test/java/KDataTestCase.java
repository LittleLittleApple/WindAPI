import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import wind.KType;
import wind.PriceAdjust;
import wind.WindErrorResponse;
import wind.WindService;


public class KDataTestCase {

	private final WindService ws = new WindService();
	private static final String KDATA_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	@Test
	public void testStockData() throws ParseException, IOException, InterruptedException, WindErrorResponse, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<List<Double>> stockRes = null;
		List<Double> kdata = null;
		
//		stockRes =  ws.getStockData("000001.SZ", "2014-06-11 01:38:31", 3, 0);
//		kdata = stockRes.get(0);
//		System.out.println(kdata.toString());
//		Assert.assertTrue(kdata.size() == 10);
		

		stockRes =  ws.getStockData("000001.SZ", "2014-06-11 01:38:31", 3, 0);
		kdata = stockRes.get(0);
		System.out.println(kdata.toString());
		Assert.assertTrue(kdata.size() == 10);
		
//		stockRes =  ws.getStockData("000001.SZ", "2014-11-16 01:38:31", 4, 0);
//		kdata = stockRes.get(0);
//		Assert.assertTrue(kdata.size() == 10);
//		
//		stockRes =  ws.getStockData("000001.SZ", "2014-11-16 01:38:31", 5, 0);
//		kdata = stockRes.get(0);
//		Assert.assertTrue(kdata.size() == 10);
		
		
		
//		Assert.assertTrue(kdata.size() == 10);
//		Assert.assertEquals(1416153600000.00, kdata.get(0), 0.000);
//		Assert.assertEquals(10.95, kdata.get(1), 0.000);
//		Assert.assertEquals(10.99, kdata.get(2), 0.000);
//		Assert.assertEquals(10.71, kdata.get(3), 0.000);
//		Assert.assertEquals(10.76, kdata.get(4), 0.000);
//		Assert.assertEquals(11.042, kdata.get(5), 0.000);
//		Assert.assertEquals(10.977, kdata.get(6), 0.000);
//		Assert.assertEquals(10.672, kdata.get(7), 0.000);
//		Assert.assertEquals(961843455.8, kdata.get(8), 0.000);
//		Assert.assertEquals(88959448.0, kdata.get(9), 0.00);
	}
	
	@Test
	public void testStockDataIntegrate() throws ParseException, IOException, InterruptedException, WindErrorResponse, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<List<Double>> stockRes = null;
		List<Double> kdata = null;		
		String sQryDate="2015-01-19 16:01:53";
		
		stockRes =  ws.getStockData("000001.SZ", sQryDate, 0, 1);
		kdata = stockRes.get(0);
		Assert.assertTrue(kdata.size() == 10);
		

		sQryDate="2015-01-20 16:01:53";
		
		stockRes =  ws.getStockData("000001.SZ", sQryDate, 0, 1);
		kdata = stockRes.get(0);
		Assert.assertTrue(kdata.size() == 10);
	}
		
	
	
	@Test
	public void testStockDataMinutes() throws ParseException, IOException, InterruptedException, WindErrorResponse, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<List<Double>> stockRes = null;
		List<Double> kdata = null;		
		String sQryDate="2015-01-16 13:38:31";
		
		
		stockRes =  ws.getStockData("000001.SZ", sQryDate, 0, 0);
		kdata = stockRes.get(0);
		System.out.println(kdata.toString());
		Assert.assertTrue(kdata.size() == 10);
		
		stockRes =  ws.getStockData("000001.SZ", sQryDate, 1, 0);
		kdata = stockRes.get(0);
		Assert.assertTrue(kdata.size() == 10);
		
		stockRes =  ws.getStockData("000001.SZ", sQryDate, 2, 0);
		kdata = stockRes.get(0);
		Assert.assertTrue(kdata.size() == 10);
		

		sQryDate="2015-01-15 13:38:31";
		stockRes =  ws.getStockData("000001.SZ", sQryDate, 0, 0);
		kdata = stockRes.get(0);
		System.out.println(kdata.toString());
		Assert.assertTrue(kdata.size() == 10);
		
		stockRes =  ws.getStockData("000001.SZ", sQryDate, 1, 0);
		kdata = stockRes.get(0);
		Assert.assertTrue(kdata.size() == 10);
		
		stockRes =  ws.getStockData("000001.SZ", sQryDate, 2, 0);
		kdata = stockRes.get(0);
		Assert.assertTrue(kdata.size() == 10);

//		Assert.assertEquals(1420766700000.00, kdata.get(0), 0.000);
//		Assert.assertEquals(14.9, kdata.get(1), 0.000);
//		Assert.assertEquals(14.9, kdata.get(2), 0.000);
//		Assert.assertEquals(14.9, kdata.get(3), 0.000);
//		Assert.assertEquals(14.9, kdata.get(4), 0.000);
//		Assert.assertEquals(14.948, kdata.get(5), 0.000);
//		Assert.assertEquals(14.948, kdata.get(6), 0.000);
//		Assert.assertEquals(14.948, kdata.get(7), 0.000);
//		Assert.assertEquals(8796960.0, kdata.get(8), 0.000);
//		Assert.assertEquals(590400.0, kdata.get(9), 0.00);
	}

}
