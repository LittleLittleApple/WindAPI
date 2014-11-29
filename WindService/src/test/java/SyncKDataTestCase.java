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
import wind.WindErrorResponse;
import wind.WindService;


public class SyncKDataTestCase {

	@Test
	public void test() throws IOException, ParseException, InterruptedException, WindErrorResponse, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		String KDATA_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		
		WindService ws = new WindService();

		DateFormat format1 = new SimpleDateFormat(KDATA_DATE_FORMAT);        
		Date begin = format1.parse("2014-11-17 01:38:31");
		Date end = format1.parse("2014-11-17 15:38:31");
		
		List<String> stockCodes = new ArrayList<String>();
		stockCodes.add("000001.SZ");
		stockCodes.add("000002.SZ");
		ws.syncKData(stockCodes, begin, end, KType.FIVE_MIN_KTYPE);
		ws.syncKData(stockCodes, begin, end, KType.THIRDTY_MIN_KTYPE);
		ws.syncKData(stockCodes, begin, end, KType.DAY_KTYPE);
	}

}
