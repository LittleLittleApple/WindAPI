package wind;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//单个股票K线数据(采样点)
public class StockDataLoader {


	
	private static StockDataLoader instance = null;
	private  Connection conn = null;
	private DBHelper dbHelper = null;
	
	private static final String SELECT_STOCK_SQL=" select * from (select t1.stockCode, t1.ktype, t1.w_time, t1.open, t1.high, t1.low, t1.close, t1.volume, t1.amt, t1.ma5, t1.ma10, t1.ma20, t1.adjfactor,mx1.adjfactor as max_adjfactor,createdAt,updatedAt "
			+ " from ws_kdata t1  "
			+ " join (select stockCode, adjfactor, max(yeardate) from ws_kdata where ktype = 3 group by stockCode) mx1 "
			+ " on t1.stockCode = mx1.stockCode "
			+ " where mx1.stockCode = ? and t1.ktype = ? and t1.w_time >= ? order by w_time limit 150) as un1 "
			+ " union "
			+ " select t2.stockCode, t2.ktype, t2.w_time, t2.open, t2.high, t2.low, t2.close, t2.volume, t2.amt, t2.ma5, t2.ma10, t2.ma20, t2.adjfactor,mx2.adjfactor as max_adjfactor,createdAt,updatedAt "
			+ " from ws_kdata t2  "
			+ " join (select stockCode, adjfactor, max(yeardate) from ws_kdata where ktype = 3 group by stockCode) mx2 "
			+ " on t2.stockCode = mx2.stockCode "
			+ " where t2.stockCode = ? and t2.ktype = ? and t2.w_time < ? order by w_time limit ? ";
	
	private static final String SELECT_STOCK_SQL_COUNT="(select count(1) "
			+ " from ws_kdata t1  "
			+ " join (select stockCode, adjfactor, max(yeardate) from ws_kdata where ktype = 3 group by stockCode) mx1 "
			+ " on t1.stockCode = mx1.stockCode "
			+ " where mx1.stockCode = ? and t1.ktype = ? and t1.w_time >= ? limit 150) ";

	
	private StockDataLoader(ConfigurationSQL sqlCfg) throws SQLException{ 
		dbHelper = DBHelper.getInstance(sqlCfg);
		this.conn = dbHelper.getCon();
		if (conn == null) {
			throw new SQLException("Connection has not been established!");
		}
	}

	

	public static StockDataLoader getInstance(ConfigurationSQL sqlCfg) throws SQLException {
		if(instance == null) {
			instance = new StockDataLoader(sqlCfg);
		}
		return instance;
	}
	
	public List<List<Double>> getStockData(String stockCode,
			Integer ktype, Integer priceAdj, String sQryDate)
			throws SQLException, ParseException {
		
		int afterStockCount = getStockDataCount(stockCode, ktype, priceAdj, sQryDate);
		int previousStockCount = 300 - afterStockCount;
		PreparedStatement pstmt = conn.prepareStatement(SELECT_STOCK_SQL);

		Date qryDate = StockDataLoader.tryParseDate(sQryDate);
		int index = 1;
		pstmt.setString(index++, stockCode);
		pstmt.setInt(index++, ktype);
		pstmt.setTimestamp(index++, new java.sql.Timestamp(qryDate.getTime()));
		

		pstmt.setString(index++, stockCode);
		pstmt.setInt(index++, ktype);
		pstmt.setTimestamp(index++, new java.sql.Timestamp(qryDate.getTime()));
		pstmt.setInt(index++, previousStockCount);
		
		System.out.println(pstmt.toString());
		ResultSet rs =  pstmt.executeQuery();
		return getResultMap(rs,priceAdj);
	}
	
	public int getStockDataCount(String stockCode, Integer ktype,
			Integer priceAdj, String sQryDate) throws SQLException,
			ParseException {
		int stockCount = 0;
		PreparedStatement pstmt = conn.prepareStatement(SELECT_STOCK_SQL_COUNT);

		Date qryDate = StockDataLoader.tryParseDate(sQryDate);
		int index = 1;
		pstmt.setString(index++, stockCode);
		pstmt.setInt(index++, ktype);
		pstmt.setTimestamp(index++, new java.sql.Timestamp(qryDate.getTime()));
		
		System.out.println(pstmt.toString());
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			stockCount = rs.getInt(1);
		}
		return stockCount;
	}
	
	private static List<List<Double>> getResultMap(ResultSet rs, Integer priceAdj)
			throws SQLException, ParseException {
		List<List<Double>> resLst = new ArrayList<List<Double>>();
		
		Map<String, String> hm = new HashMap<String, String>();
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		while(rs.next()) {
			for (int i = 1; i <= count; i++) {
				String key = rsmd.getColumnLabel(i);
				String value = rs.getString(i);
				hm.put(key, value);
			}
			SingleStockData ssData= new SingleStockData(hm,priceAdj);
			resLst.add(ssData.toRawList());
		}

		return resLst;
	}  
	
	public static Date tryParseDate(String sQryDate) throws ParseException {
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		
		Date qryDate = null;
		try{
			qryDate = format1.parse(sQryDate);
		}catch (ParseException e){
			qryDate = format2.parse(sQryDate);
		}
		return qryDate;
	}
	
}
