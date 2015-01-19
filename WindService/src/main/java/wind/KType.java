package wind;

import java.util.HashMap;
import java.util.Map;

public class KType {
	public final static String ONE_MIN_KTYPE="0";
	public final static String FIVE_MIN_KTYPE="1";
	public final static String THIRDTY_MIN_KTYPE="2";
	public final static String DAY_KTYPE="3";
	public final static String WEEK_KTYPE="4";
	public final static String MONTH_KTYPE="5";
	
	private final static Map<String, String> ktypeMapping;
	static {
		ktypeMapping = new HashMap<String, String>();
		ktypeMapping.put(ONE_MIN_KTYPE, "BarSize=1;");
		ktypeMapping.put(FIVE_MIN_KTYPE, "BarSize=5;");
		ktypeMapping.put(THIRDTY_MIN_KTYPE, "BarSize=30;");
		ktypeMapping.put(DAY_KTYPE, "Period=D;");
		ktypeMapping.put(WEEK_KTYPE, "Period=W;");
		ktypeMapping.put(MONTH_KTYPE, "Period=M;");
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