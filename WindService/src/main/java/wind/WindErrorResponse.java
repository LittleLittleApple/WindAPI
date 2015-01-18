package wind;

import java.util.HashMap;
import java.util.Map;

public class WindErrorResponse extends Exception {

	private static final Map<Integer, String> errorCodeMessage;

	static {
		errorCodeMessage = new HashMap<Integer, String>();
		errorCodeMessage.put(0, "操作成功");
		errorCodeMessage.put(-40520008, "超时错误");
		errorCodeMessage.put(-40521010, "超时错误");
		errorCodeMessage.put(-40520013, "其他地方登录错误 ");
		errorCodeMessage.put(-40522010, "日期与时间语法错误");
		errorCodeMessage.put(-40522008, "指标参数语法错误");
		errorCodeMessage.put(-40522017, "数据提取量超限");
		errorCodeMessage.put(-40522015, "请求无相应权限");
		errorCodeMessage.put(-40521003, "网络连接失败");
		errorCodeMessage.put(-40521004, "操请求发送失败");
		errorCodeMessage.put(-40520002, "系统错误");
		errorCodeMessage.put(-40521008, "错误的应答");
		errorCodeMessage.put(-40520007, "无数据");
		errorCodeMessage.put(-40520005, "无权限");
		errorCodeMessage.put(-40522003, "非法请求");
		errorCodeMessage.put(-40522017, "数据提取量超限");
		errorCodeMessage.put(-40521006, "网络错误");

	}

	public WindErrorResponse() {
	}

	public WindErrorResponse(String defaultMessage) {
		super(defaultMessage);
	}

	public WindErrorResponse(Integer errorCode, String defaultMessage) {
		this("Unable to get data from wind. "
				+ (errorCodeMessage.keySet().contains(errorCode) ? "Cause: "
						+ errorCodeMessage.get(errorCode) : "")
				+ defaultMessage);
	}

}
