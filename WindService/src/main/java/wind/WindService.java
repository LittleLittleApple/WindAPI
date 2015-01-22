package wind;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class WindService {
	private static final String DATE_FORMAT_NOW = "yyyyMMdd";
	private static final String KDATA_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	// private static final Logger logger =
	// LoggerFactory.getLogger(WindService.class);

	private static final ResourceBundle configuration = ResourceBundle
			.getBundle("config");
	private static final String PYTHON_BIN;
	private static final String WIND_API_PATH;
	private static final ConfigurationSQL sqlCfg;
	
	static {
		PYTHON_BIN = configuration.getString("python.bin");
		if (configuration.containsKey("wind.api.dir")) {
			WIND_API_PATH = configuration.getString("wind.api.dir");
		} else {
			WIND_API_PATH = "../";
		}
		sqlCfg = new ConfigurationSQL(configuration);
	}

	public WindService() {

	}

	public Map<String, String> getStocks(String sector) throws IOException,
			InterruptedException, WindErrorResponse {
		String pythonScriptPath = getPythonFilePath("Stocks.py");

		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		DateFormat df = new SimpleDateFormat(DATE_FORMAT_NOW);
		String startDate = df.format(today);

		List<String> cmdLst = new ArrayList<String>();
		cmdLst.add(PYTHON_BIN);
		cmdLst.add(pythonScriptPath);
		cmdLst.add(startDate);
		cmdLst.add(sector);
		WindData wd = execCommand(buildCmd(cmdLst));

		return wd.getStocksFromData();
	}

	public Map<String, String> getCurrentMarket(String stockCode)
			throws IOException, InterruptedException, WindErrorResponse {
		String pythonScriptPath = getPythonFilePath("Market.py");
		List<String> cmdLst = new ArrayList<String>();
		cmdLst.add(PYTHON_BIN);
		cmdLst.add(pythonScriptPath);
		cmdLst.add(stockCode);
		WindData wd = execCommand(buildCmd(cmdLst));

		// wd.printDataList();

		List<Map<String, String>> dataLst = wd.toList();
		// Since python will return a 2D array with only on element, we should
		// get the first element.
		if (dataLst.size() > 0) {
			return dataLst.get(0);
		} else {
			return null;
		}
	}
	
	/*
	 * 获取股票停牌数据
	 * @params: 
	 * stockCode: 股票代码。 必须从wind中获得. i.e:"000001.SZ"
	 * sQryDate:  "yyyy-MM-dd". i.e "2014-11-17"
	 * @return:  如果查询的股票在查询时间内停牌， 则会返回StockSuspendData类型的数据， 否则则返回null
	 * 
	 */
	public StockSuspendData getSuspendData(String stockCode, String sQryDate)
			throws IOException, InterruptedException, WindErrorResponse, ParseException {
		
		StockSuspendData ssd = null;
		Date qryDate = DatetimeUtility.tryParseDate(sQryDate);
		Date today = DatetimeUtility.dayEndOfDate(new Date());
		
		String startDate = null;
		if(qryDate.after(today)) {
			startDate = DatetimeUtility.date2WindString(today);
		}else {
			startDate = DatetimeUtility.date2WindString(qryDate);
		}
		
		String endDate = startDate;
		
		
		
		String pythonScriptPath = getPythonFilePath("TradeSuspend.py");
		List<String> cmdLst = new ArrayList<String>();
		cmdLst.add(PYTHON_BIN);
		cmdLst.add(pythonScriptPath);
		cmdLst.add(stockCode);
		cmdLst.add(startDate);
		cmdLst.add(endDate);
		WindData wd = execCommand(buildCmd(cmdLst));
		
		ssd = new StockSuspendData(stockCode, wd);;
//		if (!ssd.isSuspended()) {
//			ssd = null;
//		}
		return ssd;
	}

	// get kdata from wind.
	// @params: kType:
	// KType.ONE_MIN_KTYPE,KType.FIVE_MIN_KTYPE,KType.THIRDTY_MIN_KTYPE,KType.DAY_KTYPE,KType.WEEK_KTYPE,KType.MONTH_KTYPE
	// @return
	// [{w_time=>"xxx",open=>"xxx",volume=>"xxx",amt=>"xxx",high=>"xxx",low=>"xxx",close=>"xxx", trade_status=>"xxx"},...]
	private List<Map<String, String>> getKData(String stockCode, Date begin,
			Date end, String kType, Integer priceAdj) throws IOException, InterruptedException,
			WindErrorResponse {

		Map<String, String> m = new HashMap<String, String>();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		list.add(m);

		DateFormat df = new SimpleDateFormat(KDATA_DATE_FORMAT);
		String startDate = df.format(begin);
		String endDate = df.format(end);
		String pythonScriptPath;
		String kdataPeriod = KType.parsePyKType(kType);
		if (kdataPeriod.startsWith("BarSize")) {
			pythonScriptPath = getPythonFilePath("KDataMinutes.py");
		} else {
			pythonScriptPath = getPythonFilePath("KData.py");
		}

		String kdataPriceAdj = PriceAdjust.parsePriceAdj(priceAdj);

		List<String> cmdLst = new ArrayList<String>();
		cmdLst.add(PYTHON_BIN);
		cmdLst.add(pythonScriptPath);
		cmdLst.add(stockCode);
		cmdLst.add(startDate);
		cmdLst.add(endDate);
		cmdLst.add(kdataPeriod);
		cmdLst.add(kdataPriceAdj);
		
		WindData wd = execCommand(buildCmd(cmdLst));

		// wd.printDataList();

		return fixRawKey(stockCode, kType, wd.toList());
	}

	// since the raw key in K data is a little bit different from minutes KData
	// and Day KData.
	private List<Map<String, String>> fixRawKey(String stockCode, String ktype,
			List<Map<String, String>> kdataLst) {

		for (int i = 0; i < kdataLst.size(); i++) {
			Map<String, String > kdataMap = kdataLst.get(i);
			if(kdataMap == null) {
				continue;
			}
			
			kdataMap.put("stockCode", stockCode);
			kdataMap.put("ktype", ktype);
			
			if (kdataMap.containsKey("amount")) {
				kdataMap.put("amt", kdataMap.get("amount"));
				kdataMap.remove("amount");
			}
		}
		return kdataLst;
	}
	
	/*根据K线类型和复权类型查询股票行情数据. 
		由于 Wind对查旧的分钟数据有很大的限制,分钟K线只会同步查询当天(today)的数据.
		parameters: 
		stockCode: stockcode from wind, such as: 000001.SZ
		kType: 0 =>1 minute, 1 =>5 minutes, 2 => 30 minutes, 3 => 1 day, 4 => 1 week, 5 => 1 month
		priceAdj: 0 => None adjustment, 1 => forward adjustment, 2 => backward adjustment
		query date: "yyyy-MM-dd HH:mm:ss". i.e "2014-11-17 01:38:31"
		@return:  股票的列表数据
    */
	public List<List<Double>> getStockData(String stockCode, String queryDate, Integer ktype, Integer priceAdj) throws ParseException, IOException, InterruptedException, WindErrorResponse, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<List<Double>> res = null;//new ArrayList<List<Double>>();

		List<String> stockList = Arrays.asList(stockCode);
		
		StockDataLoader sdl = StockDataLoader.getInstance(sqlCfg);
		//同步查询日期的K线数据, 如果当天没有结束，则不会同步。
		if(ktype >= 3 && DatetimeUtility.isToday(queryDate)) {
			return res;
		}
		//分钟K线只会同步查询当天(today)的数据. Wind对查旧的分钟数据有很大的限制
		syncStockData(stockList, queryDate, ktype);
		res = sdl.getStockData(stockCode, ktype, priceAdj, queryDate);
		return res;
	}
	
    /**
     * 自动同步wind的stock数据。注意查询区间， 最好只是同步当天的数据. 该接口同步顺序:月线 -> 周线 -> 日线 -> 1分钟K线 -> 5分钟K线 -> 30分钟K线
     * 其中月线，周线和日线会检查数据库是否存在。如果已经重复，那就不会再向Wind查询
     * @param  stockList: 从Wind获取回来的股票代码列表
     * @param  sQryDate:  查询日期.支持两种格式:yyyy-MM-dd HH:mm:ss, yyyy-MM-dd. 注意：同步的过程中，系统会自动重置查询日期为00:00:00 ~ 23:59:59
     * @param  ktype:  K线数据类型.0 =>1 minute, 1 =>5 minutes, 2 => 30 minutes, 3 => 1 day, 4 => 1 week, 5 => 1 month
     * 
     * @throws ParseException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws SQLException 
     */
	public void syncStockData(List<String> stockList, String sQryDate) throws IOException, InterruptedException, WindErrorResponse, ParseException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Date qryDate = DatetimeUtility.tryParseDate(sQryDate);
		syncStockData(stockList, qryDate, 5);  //先同步月线
		syncStockData(stockList, qryDate, 4);  //先同步周线
		syncStockData(stockList, qryDate, 3);  //先同步日线
		
		syncStockData(stockList, qryDate, 0);  //同步1分钟K线
		syncStockData(stockList, qryDate, 1);  //同步5分钟K线
		syncStockData(stockList, qryDate, 2);  //同步30分钟K线
	}
	
    /**
     * 同步wind的stock数据。注意查询区间， 最好只是同步当天的数据
     * @param  stockList: 从Wind获取回来的股票代码列表
     * @param  sQryDate:  查询日期.支持两种格式:yyyy-MM-dd HH:mm:ss, yyyy-MM-dd. 注意：同步的过程中，系统会自动重置查询日期为00:00:00 ~ 23:59:59
     * @param  ktype:  K线数据类型.0 =>1 minute, 1 =>5 minutes, 2 => 30 minutes, 3 => 1 day, 4 => 1 week, 5 => 1 month
     * 
     * @throws ParseException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws SQLException 
     */
	public void syncStockData(List<String> stockList, String sQryDate, Integer ktype) throws IOException, InterruptedException, WindErrorResponse, ParseException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Date qryDate = DatetimeUtility.tryParseDate(sQryDate);
		syncStockData(stockList, qryDate, ktype);
	}
	

	
    /**
     * 同步wind的stock数据。注意查询区间， 最好只是同步当天的数据
     * @param  stockList: 从Wind获取回来的股票代码列表
     * @param  qryDate:  查询日期.  注意：同步的过程中，系统会自动重置查询日期为00:00:00 ~ 23:59:59
     * @param  ktype:  K线数据类型.0 =>1 minute, 1 =>5 minutes, 2 => 30 minutes, 3 => 1 day, 4 => 1 week, 5 => 1 month
     * 
     * @throws ParseException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws SQLException 
     */
	public void syncStockData(List<String> stockList, Date qryDate, Integer ktype) throws IOException, InterruptedException, WindErrorResponse, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, ParseException {
		
		Date startDate = KType.getDefaultStart(qryDate, ktype);
		Date endDate = KType.getDefaultEnd(qryDate, ktype);
		syncKData(stockList, startDate, endDate, ktype.toString(), PriceAdjust.NONE);
	}
	
	/*
	 * 按指定参数，同步Wind的数据到数据库。 注意这是旧的同步接口， 已经新加了一个更简单的公共接口: syncStockData
	 * 
	 */
	private void syncKData(List<String> stockList, Date begin, Date end,
			String ktype, Integer priceAdj) throws IOException,
			InterruptedException, WindErrorResponse, SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException, ParseException {

		StockImporter importer = StockImporter.getInstance(sqlCfg);
		importer.checkDbVersion();
		for (int i = 0; i < stockList.size(); i++) {
			String stockCode = stockList.get(i);
			if(importer.isImported(stockCode, Integer.parseInt(ktype))) {
				continue;
			}
			List<Map<String, String>> kDataLst = getKData(stockCode, begin,
					end, ktype, priceAdj);
			importer.importStocks(kDataLst);
//			importer.updateStockMinuteAdjFactor(stockCode, Integer.parseInt(ktype));

		}
	}

	private WindData execCommand(String[] cmd) throws IOException,
			InterruptedException, WindErrorResponse {
		// print the using cmd.
		printCmd(cmd);

		Process pr = Runtime.getRuntime().exec(cmd);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				getInputStream(pr)));
		String line;
		List<String> lines = new ArrayList<String>();

		while ((line = in.readLine()) != null) {
			lines.add(line);
		}
		in.close();
		pr.waitFor();

		System.out.println(lines.toString());
		WindData wd = new WindData(lines);
		if (!wd.isSuccessed()) {
			throw new WindErrorResponse("Unable to get data from wind: "
					+ wd.toString());
		}

		return wd;
	}

	private void printCmd(String[] cmd) {
		String strCmd = PYTHON_BIN + " ";
		// start from 1 because the first argument is python command
		for (int i = 1; i < cmd.length; i++) {
			strCmd += ("\"" + cmd[i] + "\"" + " ");
		}
		System.out.println(strCmd);
	}

	private String getPythonFilePath(String pyName) throws IOException {
		String pyPath = WIND_API_PATH + "/" + pyName;
		;
		if (!(new File(pyPath).exists())) {
			throw new IOException(pyPath + " not found");
		} 

		System.out.println("Python script: " + pyPath);
		return pyPath;
	}

	private InputStream getInputStream(Process pr) {
		// return WindService.class.getResourceAsStream("kdata.txt");
		return pr.getInputStream();
	}

	private String[] buildCmd(List<String> cmdLst) {
		return Arrays.copyOf(cmdLst.toArray(), cmdLst.toArray().length,
				String[].class);
	}
}
