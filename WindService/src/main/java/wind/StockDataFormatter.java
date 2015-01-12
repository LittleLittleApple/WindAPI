package wind;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;



public class StockDataFormatter {
	private DateFormat windResFormat = null;
	private DateFormat windMinuteResFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	private DateFormat windDateResFormat = new SimpleDateFormat("yyyyMMdd");
	private final String timeKey = "w_time";
	private final List<String> fields =  Arrays.asList(timeKey,"open","high","low","close", "ma5", "ma10", "ma20","amt","volume");

	
	StockDataFormatter(Integer kType){
		switch (kType) {
		case 0:
		case 1:
		case 2:
			windResFormat = windMinuteResFormat;
			break;
		case 3:
		case 4:
		case 5:
			windResFormat = windDateResFormat;
			break;
		default:
			throw new IllegalArgumentException("illegal kType: " + kType.toString());
		}
	}

	
	//params:
	//[{"w_time": "20141129 23:14:19",
	//	"open": "12.44",
	//	"amt": "5793392235.0",
	//	"volume": "484483936.0",
	//	"high": "12.44",
	//	"low": "11.18",
	//	"close": "11.18"
	//}]
	//[[timestamp, open, high, low,  close, MA5, MA10, MA20],...]
	public List<List<Double>> parseStockData(List<Map<String, String>> kData) {
		List<List<Double>> stkRes = new ArrayList<List<Double>>();
		Map<String, String> kdataMap = null;
		List<Double> kdataEleRes = null;
		for(int i=0;i< kData.size();i++) {
			//kdata map from wind.
			kdataMap = kData.get(i);
			//kdata list to result
			kdataEleRes = new ArrayList<Double>();
			for(int j=0;j<fields.size();j++) {
				try {
					String k = fields.get(j);
					if(kdataMap.containsKey(k)) {
						double v = -1.00;
						if (timeKey == k) {
							String sTime = kdataMap.get(fields.get(j));
							v = windResFormat.parse(sTime).getTime();
						}else{
							v= Double.valueOf(kdataMap.get(fields.get(j))) ;
						}
						kdataEleRes.add(v);
					}else{
//						System.out.println("key is not exists:" + k);
						kdataEleRes.add(-1.00);
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
					kdataEleRes.add(-1.00);
				}
			}
			if(stkRes.size() <= 300){
				stkRes.add(kdataEleRes);
			}
		}
		return stkRes;
	}
}
