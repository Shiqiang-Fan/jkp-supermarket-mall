package com.joyveb.jkpmall.util;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.springframework.util.Assert;

/**
 * @author yangqiju
 * @date 2018-03-12 上午11:30
 */
public final class DateUtil {

	private static final Integer GET_SERIAL = 100;

	private DateUtil() {
	}


	public static long getDate2StartTime(Date date) {
		Assert.notNull(date);
		return org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.DAY_OF_MONTH)
			.getTime();
	}

	/**
	 * 当天的23:59:59
	 */
	public static long getDate2EndTime(Date date) {
		Assert.notNull(date);
		Date tmpDate = org.apache.commons.lang3.time.DateUtils
			.truncate(date, Calendar.DAY_OF_MONTH);
		Date tomorrow = org.apache.commons.lang3.time.DateUtils.addDays(tmpDate, 1);
		long startDateEndTime = org.apache.commons.lang3.time.DateUtils.addSeconds(tomorrow, -1)
			.getTime();
		return startDateEndTime;
	}

}
