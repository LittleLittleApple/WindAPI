package wind;

import java.util.HashMap;
import java.util.Map;

public class PriceAdjust {
	//不得权
	public final static Integer NONE=0;
	//向前复权
	public final static Integer FORWARD=1;
	//向后复权
	public final static Integer BACKWARD=2;
	
	private final static Map<Integer, String> priceAdjMapping;
	static {
		priceAdjMapping = new HashMap<Integer, String>();
		priceAdjMapping.put(NONE, "N"); 
		priceAdjMapping.put(FORWARD, "PriceAdj=F;"); 
		priceAdjMapping.put(BACKWARD, "PriceAdj=B;");
	}
	
	public static String parsePriceAdj(Integer parameter) {
		String value = priceAdjMapping.get(parameter);
		if (value == null) {
			throw new IllegalArgumentException("illegal parameter: "
					+ parameter);
		}
		return value;
	}
}