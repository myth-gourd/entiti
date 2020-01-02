package myth.gourd.entiti.util;
import java.util.Calendar;
import java.util.Date;

public class DateUtil 
{
	private static final long yearMultiple = 10000000000000L;
	private static final long monthMultiple = 100000000000L;
	private static final long dayMultiple = 1000000000L;
	private static final long hourMultiple = 10000000L;
	private static final long miniteMultiple = 100000L;
	private static final long secondMultiple = 1000L;
	
	public static Date currentDate()
	{
		return new Date();
	}
	
	public static Date CurrentTime() {
		return new Date();
	}
	
	public static int CurrentDateInt() {
		return 0;
	}

	public static long CurrentTimeLong() {
		return 0;
	}

	public static int toIntDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return year * 10000 + month * 100 + day;
	}

	public long toLongTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minite = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		int milliSECOND = cal.get(Calendar.MILLISECOND);
		return year * yearMultiple + month * monthMultiple + day * dayMultiple + hour * hourMultiple
				+ minite * miniteMultiple + second * secondMultiple + milliSECOND;
	}
}