package com.neo.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};
	
	public static String format(Date date) {
		if (date == null)
			return null;

		DateFormat dateFormat = df.get();
		return dateFormat.format(date);
	}

}
