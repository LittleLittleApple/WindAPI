package wind;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.ScriptRunner;

//这里我们建立一个DBHelper类
public class StockImporter {
	private static StockImporter instance = null;
	private  Connection conn = null;
	private DBHelper dbHelper = null;
	private static final int CURRENT_MYSQL_VERSION=1;
	
	private static final String INSERT_STOCK_SQL="INSERT INTO ws_kdata (stockCode, ktype, w_time, "
			+ " open, high, low, close, volume, amt, ma5, ma10, ma20, adjfactor, yearMonth, yearWeek, yearDate, createdAt, updatedAt) "
			+ " SELECT ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? "
			+ " FROM dual "
			+ " WHERE NOT EXISTS (SELECT 1 FROM ws_kdata WHERE stockCode= ? AND ktype= ? AND w_time= ?) ";
	
	private static final String INSERT_STOCK_SQL_FORCE_UPDATE="REPLACE INTO ws_kdata (stockCode, ktype, w_time, "
			+ " open, high, low, close, volume, amt, ma5, ma10, ma20, adjfactor, yearMonth, yearWeek, yearDate, createdAt, updatedAt) "
			+ " SELECT ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? "
			+ " FROM dual "
			+ "  ";
	
	//make sure that the ktype is less than 3(is minute ktype)
	private static final String UPDATE_MINUTE_KDATA_ADJFACTOR_SQL="update ws_kdata t1 inner join (select stockCode, adjfactor,yearDate from ws_kdata where ktype = 3) t2"
			+ " on t1.stockCode=t2.stockCode and t1.yearDate = t2.yearDate "
			+ " set t1.adjfactor = t2.adjfactor "
			+ " where t1.stockCode = ? and t1.ktype = ? and t1.ktype < 3 ";
	
	private StockImporter(ConfigurationSQL sqlCfg) throws SQLException{ 
		dbHelper = DBHelper.getInstance(sqlCfg);
		this.conn = dbHelper.getCon();
		if (conn == null) {
			throw new SQLException("Connection has not been established!");
		}
	}
	

	public static StockImporter getInstance(ConfigurationSQL sqlCfg) throws SQLException {
		if(instance == null) {
			instance = new StockImporter(sqlCfg);
		}
		return instance;
	}
	
	
	
	public boolean checkDbVersion() throws SQLException, FileNotFoundException {
		// if the database wasn't initialized.
		if(!hasTable("db_version")){
			runSQLScript("1.sql");
			return true;
		}
		// check db version
		ResultSet rs =  dbHelper.executeQuery("select * from db_version");
		if (rs.next()) {
			int deployedSchema = rs.getInt(1);
			// return directly if the dbversion is correct.
			if (CURRENT_MYSQL_VERSION == deployedSchema) {
				return true;
			}
		}
		runSQLScript(CURRENT_MYSQL_VERSION + ".sql");
		return true;
	}
	
	public boolean hasTable(String table) throws SQLException {
		boolean res = false;
		DatabaseMetaData meta = conn.getMetaData();
		ResultSet rs;// = meta.getTables(null, null, table, null);
		rs = meta.getTables(null, null, table.toUpperCase(), null);
		if (rs.next()) {
			res = true;
		}
		return res;
	}
	
	private void runSQLScript(String sqlFile) throws SQLException, FileNotFoundException {
		ScriptRunner runner = new ScriptRunner(conn);
		InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("sqlscripts/" + sqlFile));
		runner.runScript(reader);
	}
	
	// check if the stock has been imported
	public boolean isImported(String stockCode, Date begin, Date end, Integer ktype) throws SQLException, FileNotFoundException {

		//由于一天可能在不同的时间点同步多次股票数据，所以要求分钟的K线数据可以重复导入并更新
		if(ktype < 3) {
			return false;
		}
		//日K线，周K线，月K线
		String sSQL = "select  1  from ws_kdata where stockCode = '"+ stockCode +"' and ktype = " + ktype + " and w_time >= " + begin.getTime() + " and w_time <  " + end.getTime();
		System.out.println(sSQL);
		ResultSet rs =  dbHelper.executeQuery(sSQL);
		if (rs.next()) {
			System.out.println("Existing. Skipped import.");
			return true;
		}
		return false;
	}
	
	//Arrays.asList(timeKey,"open","high","low","close", "ma5", "ma10", "ma20","amt","volume");
	public void importStocks(List<Map<String, String>> kdataLst, boolean forceUpdate) throws SQLException, ParseException {
		int total = kdataLst.size();
		int insert = 0;
		
		Iterator<Map<String, String>> it = kdataLst.iterator();
		boolean autoCommit = conn.getAutoCommit();

		int batchLimit = 1000;
		String sSql = null;
		
		if(forceUpdate) {
			sSql = INSERT_STOCK_SQL_FORCE_UPDATE;
		}else {
			sSql = INSERT_STOCK_SQL;
		}

		try {
			PreparedStatement insertStmt = conn
					.prepareStatement(sSql);
			conn.setAutoCommit(false);
			try {
				
				while(it.hasNext()){
					Map<String, String> kdata = it.next();
					SingleStockSQLData stockData = new SingleStockSQLData(kdata);
					setInsertStockStmt(insertStmt, stockData,forceUpdate);batchLimit--;
					if (batchLimit == 0) {
						insertStmt.executeBatch();
						insertStmt.clearBatch();
						batchLimit = 1000;
					}
					insertStmt.clearParameters();
					insert++;
				}
			} finally {
				insertStmt.executeBatch();
				conn.commit();
				insertStmt.close();
			}
		} finally {
			conn.setAutoCommit(autoCommit);
		}
		System.out.println("totla: " + total + "  insert: " + insert);
	}
	//stockCode, ktype, w_time, open, high, low, close, volume, amt, createdAt, updatedAt
	private void setInsertStockStmt(PreparedStatement insertStmt, SingleStockSQLData stockData, boolean forceUpdate) throws SQLException {
		int index = 1;
		insertStmt.setString(index++, stockData.getStockCode());
		insertStmt.setInt(index++, stockData.getKtype());
		insertStmt.setTimestamp(index++, new java.sql.Timestamp(stockData.getWTime().getTime()));
		insertStmt.setLong(index++, stockData.getOpen());
		insertStmt.setLong(index++, stockData.getHigh());
		insertStmt.setLong(index++, stockData.getLow());
		insertStmt.setLong(index++, stockData.getClose());
		insertStmt.setLong(index++, stockData.getVolume());
		insertStmt.setLong(index++, stockData.getAmt());
		insertStmt.setLong(index++, stockData.getMa5());
		insertStmt.setLong(index++, stockData.getMa10());
		insertStmt.setLong(index++, stockData.getMa20());
		insertStmt.setLong(index++, stockData.getAdjfactor());
		insertStmt.setInt(index++, stockData.getYearMonth());
		insertStmt.setInt(index++, stockData.getYearWeek());
		insertStmt.setInt(index++, stockData.getYearDate());
		insertStmt.setTimestamp(index++, new java.sql.Timestamp(stockData.getCreatedAt().getTime()));
		insertStmt.setTimestamp(index++, new java.sql.Timestamp(stockData.getUpdatedAt().getTime()));
		
		if(!forceUpdate) {
			//duplicate checking.
			insertStmt.setString(index++, stockData.getStockCode());
			insertStmt.setInt(index++, stockData.getKtype());
			insertStmt.setTimestamp(index++, new java.sql.Timestamp(stockData.getWTime().getTime()));
		}
		
//		System.out.println(insertStmt.toString());
		insertStmt.addBatch();
	}
	
	
	
	public void updateStockMinuteAdjFactor(String stockCode, Integer ktype) throws SQLException {
		if(ktype >= 3) {
//			System.out.println("ktype: " + ktype + " Only minute Kdata need to update adjfactor. since wind doesn't return adjfactor.");
			return;
		}
		PreparedStatement pstmt = conn.prepareStatement(UPDATE_MINUTE_KDATA_ADJFACTOR_SQL);
		int index = 1;
		pstmt.setString(index++, stockCode);
		pstmt.setInt(index++, ktype);
		
		System.out.println(pstmt.toString());
		pstmt.executeUpdate();
	}

	

}