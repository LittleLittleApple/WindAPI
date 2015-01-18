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

		String sQryDate = "2015-01-15 15:38:31";
		
		begin = format1.parse("2015-01-15 01:20:31");
		end = format1.parse("2015-01-15 15:38:31");	
		
		List<String> stockCodes = new ArrayList<String>();
		stockCodes.add("000001.SZ");
		stockCodes.add("000002.SZ");



		sQryDate = "2015-01-14 15:38:31";
		ws.syncStockData(stockCodes, sQryDate);
		
		sQryDate = "2015-01-15 15:38:31";
		ws.syncStockData(stockCodes, sQryDate);
		
	}

}
