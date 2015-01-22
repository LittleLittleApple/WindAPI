package wind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WindData {
	private Integer errorCode;
	private String requestID;
	private String[] codes;
	private String[] fields;
	private String[] times;
	private List<String[]> data;
	private String strResult = "";
//	private static final Logger logger = LoggerFactory.getLogger(WindService.class);

	public WindData(List<String> lines) throws WindErrorResponse {
		for (int i = 0; i < lines.size(); i++) {
			readline(lines.get(i));
		}
		if (!isSuccessed()) {
			throw new WindErrorResponse(this.errorCode, this.strResult);
		}
	}

	// 命令错误代码
	public Integer getErrorCode() {
		return this.errorCode;
	}

	// 命令返回的代码
	public String getRequestID() {
		return this.requestID;
	}

	// 命令返回的代码
	public String[] getCodes() {
		return this.codes;
	}

	// 命令返回的指标
	public String[] getFields() {
		return this.fields;
	}

	// 命令返回的时间
	public String[] getTimes() {
		return this.times;
	}

	// 命令返回的数据
	// @return:
	// [["2014-11-17 10:00:00.005000","2014-11-17 10:30:00.005000"],["000001.SZ","000001.SZ"],["10.95","10.84"],["10.85","10.74"]]
	public List<String[]> getData() {
		return this.data;
	}

	public Map<String, String> getStocksFromData() {
		Map<String, String> stockMap = new HashMap<String, String>();
		String stockCodeField = "wind_code";
		String secNameField = "sec_name";
		// Normally wind_code is the second element.
		if (this.data.size() > 1 && this.fields.length > 0) {
			int stockCodeIndex = Arrays.asList(this.fields).indexOf(
					stockCodeField);
			int secNameIndex = Arrays.asList(this.fields).indexOf(
					secNameField);
			String[] stockCodes = this.data.get(stockCodeIndex);
			String[] secNames = this.data.get(secNameIndex);
			stockMap = new HashMap<String, String>(stockCodes.length);
			for(int i=0;i<stockCodes.length;i++) {
				stockMap.put(stockCodes[i], secNames[i]);
			}
			
		}
		return stockMap;
	}

	//TODO 要加上nan的处理
	// parse result to List
	// @return: [{"w_time=> ""2014-11-17 10:00:00.005000", "open"=>"10.95"...},
	// {"w_time=> ""2014-11-17 10:30:00.005000", "open"=>"10.74"...}]
	public List<Map<String, String>> toList() {
		List<Map<String, String>> resList = new ArrayList<Map<String, String>>();
		if (isSuccessed()) {
			Map<String, String> rowMap;
			for (int i = 0; i < this.fields.length; i++) {
				String rawKey = this.fields[i].toLowerCase();
				String[] values = this.data.get(i);
				rowMap = new HashMap<String, String>();
				if(times.length != values.length) {
					System.out.println("time fields is not match the values fields.");
					break;
				}
				for (int j = 0; j < values.length; j++) {
					if (resList.size() > j) {
						rowMap = resList.get(j);
					} else {
						rowMap = new HashMap<String, String>();
						// Added time field to result set.
						rowMap.put("w_time", processWindValue(times[j]));
						resList.add(rowMap);
					}
					rowMap.put(rawKey, processWindValue(values[j]));
				}
			}
		}
		return resList;
	}
	
	/*
	 *  这个方法是专门为获取股票是否停牌用的方法
	 */
	public Map<String, Map<String, String>> getStocksSuspendFromData() {
		Map<String, Map<String, String>> stockMap = new HashMap<String, Map<String, String>>();
		String stockCodeField = "wind_code";
		String secNameField = "sec_name";
		String suspendType = "suspend_type";
		String suspendReason = "suspend_reason";
		

		String isSuspended = "isSuspended";
		// Normally wind_code is the second element.
		if (this.data.size() > 1 && this.fields.length > 0) {
			String[] stockCodes = getFieldValues(stockCodeField);
			String[] secNames = getFieldValues(secNameField);
			String[] suspendTypes = getFieldValues(suspendType);
			String[] suspendReasons = getFieldValues(suspendReason);
			stockMap = new HashMap<String, Map<String, String>>(stockCodes.length);
			Map<String, String> suspendedData = null;
			
			for(int i=0;i<stockCodes.length;i++) {
				suspendedData = new HashMap<String, String>();
				suspendedData.put(secNameField, secNames[i]);
				suspendedData.put(isSuspended, "1");
				suspendedData.put(suspendType, suspendTypes[i]);
				suspendedData.put(suspendReason, suspendReasons[i]);
				stockMap.put(stockCodes[i], suspendedData);
			}
			
		}
		return stockMap;
	}
	
	private String[] getFieldValues(String fieldName) {
		int fieldIdx = getFieldIndex(fieldName);
		if(fieldIdx != -1) {
			return this.data.get(fieldIdx);
		}else
		{
			return null;
		}
		
	}
	
	private int getFieldIndex(String fieldName){
		int resIdx = Arrays.asList(this.fields).indexOf(
				fieldName);
		return resIdx;
	}
	
	private String processWindValue(String sValue) {
		if(sValue == null || sValue.isEmpty()) {
			return "";
		}else if (sValue.equalsIgnoreCase("nan")) {
			return "";
		}else {
			return sValue;
		}
	}

	// Success only when errorCode is 0
	public boolean isSuccessed() {
		return this.errorCode != null && this.errorCode == 0;
	}
	
	
	private String[] parse1DArray(String arrStr) {
		String[] res = arrStr.replaceAll("\\[", "").replaceAll("\\]", "")
				.replaceAll("None", "").split(",");
		return res;
	}

	private List<String[]> parse2DArray(String arrStr) {
		List<String[]> resLst = new ArrayList<String[]>();

		String[] strArr = arrStr.split("\\],");
		for (int i = 0; i < strArr.length; i++) {
			String[] arrLine = parse1DArray(strArr[i]);
			resLst.add(arrLine);
		}
		return resLst;
	}

	private void readline(String line) {
		int seperatorIndex = line.indexOf("=");
		if (seperatorIndex < 0) {
			return;
		}

		String rawKey = line.substring(0, seperatorIndex);
		String rawValue = line.substring(seperatorIndex + 1);
		if(rawKey.equalsIgnoreCase(".ErrorCode")) {
			this.errorCode = Integer.parseInt(rawValue);
		}else if(rawKey.equalsIgnoreCase(".RequestID")) {
			this.requestID = rawValue;
		}else if(rawKey.equalsIgnoreCase(".Codes")) {
			this.codes = parse1DArray(rawValue);
		}else if(rawKey.equalsIgnoreCase(".Fields")) {
			this.fields = parse1DArray(rawValue);
		}else if(rawKey.equalsIgnoreCase(".Times")) {
			this.times = parse1DArray(rawValue);
		}else if(rawKey.equalsIgnoreCase(".Data")) {
			this.data = parse2DArray(rawValue);
		}else {
			System.out.println("Skipped unknown wind data: " + line);
			return;
		}
		strResult += line;
	}

	// debug needed method. print json data.
	public void printDataList() {
		String res = "";
		for (int i = 0; i < toList().size(); i++) {
			res += toList().get(i).toString()+",";
		}
		System.out.println("");
		System.out.println("Reuslt List:" + "[" + res + "]");
	}

	// return plain text result from Wind python API.
	public String toString() {
		return this.strResult;
	}

}
