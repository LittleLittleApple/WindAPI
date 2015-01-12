package wind;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WindStockQuery {
	private Date startDate = null;
	private Date endDate = null;
	private DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Integer ktMillionSec = null;
	
	private Integer kLineCount = 300;
	
	WindStockQuery(Integer kType) {
		ktMillionSec = getMillionSeconds(kType);
	}
	
	
	WindStockQuery(String minDate, Integer kType) throws ParseException {
		this(kType);
		Date qryDate = format1.parse(minDate);
		if (kType < Integer.parseInt(KType.DAY_KTYPE)) {
			startDate = new Date(qryDate.getTime() - 770 * ktMillionSec);
			endDate = new Date(qryDate.getTime() + 770 * ktMillionSec);
		}else {
			startDate = new Date(qryDate.getTime() - 25 * ktMillionSec);
			endDate = new Date(qryDate.getTime() + 25 * ktMillionSec);
		}
	
		System.out.println((endDate.getTime() - startDate.getTime())/ktMillionSec);
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	
	
	
	
	//get million seconds of ktype.
	private Integer getMillionSeconds(Integer kType) {
		Integer sec = 1;
		switch (kType) {
		case 0:
			sec = 1;
			break;
		case 1:
			sec = 5;
			break;
		case 2:
			sec = 30;
			break;
		case 3:
			sec = 60;
			break;
		case 4:
			sec = 1 * 24 * 60;
			break;
		case 5:
			sec = 1 * 7 * 24 * 60;
			break;
		case 6:
			sec = 1 * 30 * 24 * 60;
			break;

		default:
			break;
		}
		//convert to second
		sec = sec * 60;
		//convert to million second.
		sec = sec * 1000;
		
		return sec;
	}
}
