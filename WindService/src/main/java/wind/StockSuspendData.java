package wind;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


//单个股票K线数据(采样点)
public class StockSuspendData {
	
	private String stockCode = null;
	private String secName = null;
//	private String suspendType = null;
	private String suspendReason = null;
	private boolean hasSuspended = false;
	
//	private String stockCodeField = "wind_code";
//	private String secNameField = "sec_name";
//	private String suspendTypeField = "suspend_type";
//	private String suspendReasonField = "suspend_reason";


	
	public StockSuspendData(String stockCode, WindData wd) {
		this.stockCode = stockCode;
		
		List<Map<String, String>> suspendMapLst = wd.toList();
		Map<String, String> stockData = suspendMapLst.get(0);
		//注意stockData不应该为空。 wind的K线数据，只要合法就应该返回有结果.
		secName = stockData.get("sec_name");
		String st = stockData.get("trade_status");
		if (st.contains("停牌")) {
			hasSuspended = true;
			suspendReason = stockData.get("susp_reason");
		}

	}
	
//	public StockSuspendData(String stockCode, WindData wd) {
//		this.stockCode = stockCode;
//		
//		Map<String, Map<String, String>> suspendMap = wd.getStocksSuspendFromData();
//		if(suspendMap.containsKey(stockCode)) {
//			hasSuspended = true;
//			Map<String, String> suspendedData = suspendMap.get(stockCode);
//			this.secName = suspendedData.get(secNameField);
//			this.suspendType = suspendedData.get(suspendTypeField);
//			this.suspendReason = suspendedData.get(suspendReasonField);
//		}
//	}
	
	public boolean isSuspended() {
		return hasSuspended;
	}
	
	
	// 股票代码
	public String getStockCode() {
		return this.stockCode;
	} 

	// 证券名称
	public String getSecName() {
		return this.secName;
	} 

//	// 停牌类型
//	public String getSuspendType() {
//		return this.suspendType;
//	} 

	// 停牌原因
	public String getSuspendReason() {
		return this.suspendReason;
	}
}
