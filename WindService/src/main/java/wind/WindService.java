package wind;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.ibatis.jdbc.ScriptRunner;

public class WindService {
	private static final String DATE_FORMAT_NOW = "yyyyMMdd";
	private static final String KDATA_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	// private static final Logger logger =
	// LoggerFactory.getLogger(WindService.class);

	private static final ResourceBundle configuration = ResourceBundle
			.getBundle("config");
	private static final String PYTHON_BIN;
	private static final String WIND_API_PATH;
	private static final ConfigurationMySQL mysqlConfig;

	// private final String INSERT_KDATA_SQL =
	// "INSERT INTO kdata (stock_code,ktype,w_time,open,close,high,low,volume,amt) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?) on ";
	private final String INSERT_KDATA_SQL = "INSERT INTO kdata set "
			+ " stock_code = ?, " + " ktype = ?, " + " w_time = ?, "
			+ " open = ?, " + " close = ? ," + " high = ? ," + " low = ?, "
			+ " volume = ? ," + " amt = ? " + "ON DUPLICATE KEY UPDATE"
			+ " open = VALUES(open), " + " close = VALUES(close) ,"
			+ " high = VALUES(high) ," + " low = VALUES(low), "
			+ " volume = VALUES(volume) ," + " amt = VALUES(amt) ";

	static {
		PYTHON_BIN = configuration.getString("python.bin");
		if (configuration.containsKey("wind.api.dir")) {
			WIND_API_PATH = configuration.getString("wind.api.dir");
		} else {
			WIND_API_PATH = "../";
		}
		mysqlConfig = new ConfigurationMySQL(configuration);
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

	public List<String> getStockCodes(String sector) throws IOException,
			InterruptedException, WindErrorResponse {
		Map<String, String> stockMap = getStocks(sector);
		stockMap.keySet().toArray();
		return null;
	}

	public Map<String, String> getCurrentMarket(String stockCode)
			throws IOException, InterruptedException, WindErrorResponse {
		String pythonScriptPath = getPythonFilePath("Market.py");
		List<String> cmdLst = new ArrayList<String>();
		cmdLst.add(PYTHON_BIN);
		cmdLst.add(pythonScriptPath);
		cmdLst.add(stockCode);
		WindData wd = execCommand(buildCmd(cmdLst));

//		wd.printDataList();

		List<Map<String, String>> dataLst = wd.toList();
		// Since python will return a 2D array with only on element, we should
		// get the first element.
		if (dataLst.size() > 0) {
			return dataLst.get(0);
		} else {
			return null;
		}
	}

	//get kdata from wind.
	// @params: kType: KType.ONE_MIN_KTYPE,KType.FIVE_MIN_KTYPE,KType.THIRDTY_MIN_KTYPE,KType.DAY_KTYPE,KType.WEEK_KTYPE,KType.MONTH_KTYPE
	// @return [{w_time=>"xxx",open=>"xxx",volume=>"xxx",amt=>"xxx",high=>"xxx",low=>"xxx",close=>"xxx"},...]
	public List<Map<String, String>> getKData(String stockCode, Date begin,
			Date end, String kType) throws IOException, InterruptedException,
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

		List<String> cmdLst = new ArrayList<String>();
		cmdLst.add(PYTHON_BIN);
		cmdLst.add(pythonScriptPath);
		cmdLst.add(stockCode);
		cmdLst.add(startDate);
		cmdLst.add(endDate);
		cmdLst.add(kdataPeriod);
		WindData wd = execCommand(buildCmd(cmdLst));

//		wd.printDataList();

		return fixRawKey(wd.toList());
	}
	
	//since the raw key in K data is a little bit different from minutes KData and Day KData.
	private List<Map<String, String>> fixRawKey(List<Map<String, String>> kdataLst) {
		
		for(int i=0;i<kdataLst.size();i++) {
			if(kdataLst.get(i).containsKey("amount")) {
				kdataLst.get(i).put("amt", kdataLst.get(i).get("amount"));
				kdataLst.get(i).remove("amount");
			}
		}
		return kdataLst;
	}

	// syncKData(List stockList, Date begin,Date end,String KType)
	public int[] syncKData(List<String> stockList, Date begin, Date end,
			String kType) throws IOException, InterruptedException,
			WindErrorResponse, SQLException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		int[] res;
		
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection conn = DriverManager.getConnection(mysqlConfig.url,
				mysqlConfig.username, mysqlConfig.password);
		init_wind_database(conn);
		boolean autoCommit = conn.getAutoCommit();

		int batchLimit = 1000;
		try {
			PreparedStatement insertStmt = conn
					.prepareStatement(INSERT_KDATA_SQL);
			conn.setAutoCommit(false);
			try {
				for (int i = 0; i < stockList.size(); i++) {
					String stockCode = stockList.get(i);
					List<Map<String, String>> kDataLst = getKData(stockCode,
							begin, end, kType);

					for (int j = 0; j < kDataLst.size(); j++) {
						Map<String, String> kdata = kDataLst.get(j);
						kdata.put("stock_code", stockCode);
						kdata.put("ktype", kType);
						setKDataStmt(insertStmt, kdata);
						batchLimit--;
						if (batchLimit == 0) {
							insertStmt.executeBatch();
							insertStmt.clearBatch();
							batchLimit = 1000;
						}
						insertStmt.clearParameters();
					}
				}
			} finally {
				res = insertStmt.executeBatch();
				conn.commit();
				insertStmt.close();
			}
		} finally {
			conn.setAutoCommit(autoCommit);
		}
		return res;
	}

	private void init_wind_database(Connection conn) throws SQLException,
			FileNotFoundException {
		ScriptRunner runner = new ScriptRunner(conn);
		InputStreamReader reader = new InputStreamReader(getClass()
				.getResourceAsStream("init_wind.sql"));
		runner.runScript(reader);

	}

	private void setKDataStmt(PreparedStatement updateStmt,
			Map<String, String> kData) throws SQLException {

		// INSERT_KDATA_SQL =
		// "INSERT INTO kdata (stock_code,ktype,w_time,open,close,high,low,volume,amt) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?)";

		int index = 1;
		updateStmt.setString(index++, kData.get("stock_code"));
		updateStmt.setString(index++, kData.get("ktype"));
		updateStmt.setString(index++, kData.get("w_time"));
		updateStmt.setString(index++, kData.get("open"));
		updateStmt.setString(index++, kData.get("close"));
		updateStmt.setString(index++, kData.get("high"));
		updateStmt.setString(index++, kData.get("low"));
		updateStmt.setString(index++, kData.get("volume"));
		updateStmt.setString(index++, kData.get("amt"));
		System.out.println(updateStmt.toString());
		updateStmt.addBatch();
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
		String pyPath = "";
		if (new File(WIND_API_PATH + pyName).exists()) {
			pyPath = WIND_API_PATH + pyName;
		} else {
			if (getClass().getResource(WIND_API_PATH + pyName) == null) {
				throw new IOException("");
			}
			pyPath = getClass().getResource(WIND_API_PATH + pyName).getPath()
					.replaceFirst("/", "");
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
