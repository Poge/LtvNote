package com.ltv.note.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@SuppressLint("SimpleDateFormat")
public class NoteTimeUtil {

	@SuppressLint("SimpleDateFormat")
	public static String getCurrentTimeText() {
		SimpleDateFormat timeStampFormat = new SimpleDateFormat(
				"yyyy_MM_dd_HH_mm_ss");
		return timeStampFormat.format(new Date(System.currentTimeMillis()));
	}

	public static String getTimeTextOnHour(long time) {
		return getTimeTextOnHour(new Date(time));
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getTimeTextOnHour(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(date);
	}
	

	public static String getTimeTextOnDay(long time) {

		return getTimeTextOnDay(new Date(time));
	}
	
	public static String getTimeTextOnDay(Date time) {

		SimpleDateFormat timeStampFormat = new SimpleDateFormat("MM/dd");
		return timeStampFormat.format(time);
	}
	
	

	public static String getTimeTextOnYear(long time) {

		return getTimeTextOnYear(new Date(time));
	}

	public static String getTimeTextOnYear(Date date) {

		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy/MM/dd");
		return timeStampFormat.format(date);
	}

	@SuppressLint("SimpleDateFormat")
	public static String getCurrentTimeMillis() {

		return String.valueOf(System.currentTimeMillis());
	}

	public static String getWeek(long time) {
		return getWeek(new Date(time));

	}
	
	public static String getWeek(Date time) {
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("EE");
		return timeStampFormat.format(time);
		
	}
	
	public static String getNoteTitle() {
		long time = System.currentTimeMillis();
		String date = getTimeTextOnYear(time);
		String week = getWeek(time);
		return date + " " + week;
	}


	public static String getNoteTitle(long time) {
		return getNoteTitle(new Date(time));
	}

	public static String getNoteTitle(Date time) {
		String date = getTimeTextOnYear(time);
		String week = getWeek(time);
		return date + " " + week;
	}



	public static String getCustomStyleTime(long time) {
		String timeText = "";
		Calendar calendar = Calendar.getInstance();
		int year_Today = calendar.get(Calendar.YEAR);
		int month_Today = calendar.get(Calendar.MONTH);
		int day_Today = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.clear();
		calendar.setTimeInMillis(time);
		int year_Notes = calendar.get(Calendar.YEAR);
		int month_Notes = calendar.get(Calendar.MONTH);
		int day_Notes = calendar.get(Calendar.DAY_OF_MONTH);

		if (year_Today == year_Notes && month_Today == month_Notes
				&& day_Today == day_Notes) {
			timeText = getTimeTextOnHour(time);
		} else if (year_Today == year_Notes) {
			timeText = getTimeTextOnDay(time);
		} else {
			timeText = getTimeTextOnYear(time);
		}

		return timeText;
	}

	public static String getCustomStyleTime(Date date){
		return getCustomStyleTime(date.getTime());
	}



}
