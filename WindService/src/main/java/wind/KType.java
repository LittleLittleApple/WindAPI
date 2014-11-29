package wind;

import java.util.HashMap;
import java.util.Map;

public class KType {
	public final static String ONE_MIN_KTYPE="1m";
	public final static String FIVE_MIN_KTYPE="5m";
	public final static String THIRDTY_MIN_KTYPE="30m";
	public final static String DAY_KTYPE="1d";
	public final static String WEEK_KTYPE="1w";
	public final static String MONTH_KTYPE="1m";
	
	private final static Map<String, String> ktypeMapping;
	static {
		ktypeMapping = new HashMap<String, String>();
		ktypeMapping.put(ONE_MIN_KTYPE, "BarSize=1");
		ktypeMapping.put(FIVE_MIN_KTYPE, "BarSize=5");
		ktypeMapping.put("30m", "BarSize=30");
		ktypeMapping.put("1d", "Period=D;");
		ktypeMapping.put("1w", "Period=W;");
		ktypeMapping.put("1m", "Period=M;");
	}
	
	public static String parsePyKType(String parameter) {
		String value = ktypeMapping.get(parameter);
		if (value == null || value.isEmpty()) {
			throw new IllegalArgumentException("illegal parameter: "
					+ parameter);
		}
		return value;
	}
}