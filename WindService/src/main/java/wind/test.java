package wind;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class test {

	private static final String KDATA_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws ParseException 
	 * @throws WindErrorResponse 
	 */
	public static void main(String[] args) throws IOException, InterruptedException, ParseException, WindErrorResponse {
		WindService ws = new WindService();
		//获取全部A股的市场代码
		ws.getStocks("全部A股");		
//		System.out.println(Arrays.toString(stocks.toArray()));
		
		//获取000001.SZ的当前行情
		ws.getCurrentMarket("000001.SZ");
		

		//获取000001.SZ的K线信息， 其中KType有六种
		DateFormat format1 = new SimpleDateFormat(KDATA_DATE_FORMAT);        
		Date begin = format1.parse("2014-11-17 01:38:31");
		Date end = format1.parse("2014-11-17 15:38:31");
		
		List<Map<String, String>> res =  ws.getKData("000001.SZ", begin, end, KType.THIRDTY_MIN_KTYPE);
		
		ws.getKData("000001.SZ", begin, end, KType.DAY_KTYPE);
		
//		PythonInterpreter interpreter = new PythonInterpreter();
//		interpreter.exec("import sys sys.path.append('pathToModiles if they're not there by default') import yourModule");
//		// execute a function that takes a string and returns a string
//		PyObject someFunc = interpreter.get("funcName");
//		PyObject result = someFunc.__call__(new PyString("Test!"));
//		String realResult = (String) result.__tojava__(String.class);
		
		

	}

}
