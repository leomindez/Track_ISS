package com.spaceapps.trackiss;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;

public class IssPassTimes {

	private String message;
	private double latitude;
	private double longitude;
	private int altitude;
	private int passes;
	private long datetime;
	private int duration;
	private long risetime;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public int getPasses() {
		return passes;
	}

	public void setPasses(int passes) {
		this.passes = passes;
	}

	public long getDatetime() {
		return datetime;
	}

	public void setDatetime(long datetime) {
		this.datetime = datetime;
	}

	public int getDuration() {
		return duration / 60;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public long getRisetime() {
		return risetime;
	}

	public void setRisetime(long risetime) {
		this.risetime = risetime;
	}

	private Date getCalendarRiseTime() {
		return new Date(getRisetime() * 1000);
	}

	public GregorianCalendar getCalendarWithDate() {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTimeInMillis(getCalendarRiseTime().getTime());
			
		return calendar;

	}

	@SuppressLint("SimpleDateFormat")
	public String getStringRiseTimes() {
		String hourRise;
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"dd-MM-yyyy HH:mm:ss");
		hourRise = dateFormat.format(getCalendarRiseTime().getTime());
		
		return hourRise;

	}
}
