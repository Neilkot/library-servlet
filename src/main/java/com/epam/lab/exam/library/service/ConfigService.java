package com.epam.lab.exam.library.service;

import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

public class ConfigService {

	private static final ConfigService INSTANCE = new ConfigService();
	
	private String defaultLocale;
	private Float dailyFee;
	private long expirationMillis;
	private Integer defaultPageSize;
	private Integer defaultOffset;
	private ZoneId libraryTimezone;
	private int libraryClosingHour;


	private ConfigService() {
	}

	public static ConfigService getInstance() {
		return INSTANCE;
	}
	
	public void setDefaultLocale(String defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	public void setDailyFee(Float dailyFee) {
		this.dailyFee = dailyFee;
	}

	public void setExpirationMillis(long expirationMillis) {
		this.expirationMillis = expirationMillis;
	}

	public void setDefaultPageSize(Integer defaultPageSize) {
		this.defaultPageSize = defaultPageSize;
	}

	public void setDefaultOffset(Integer defaultOffset) {
		this.defaultOffset = defaultOffset;
	}

	public void setLibraryTimezone(ZoneId libraryTimezone) {
		this.libraryTimezone = libraryTimezone;
	}

	public void setLibraryClosingHour(int libraryClosingHour) {
		this.libraryClosingHour = libraryClosingHour;
	}
	
	public String getPropertyValue(String locale, String messageCode) {
		return messageCode;
	}

	public String getDefaultLocale() {
		return defaultLocale;
	}

	public Float getDailyFee() {
		return dailyFee;
	}

	public long getExpirationMillis() {
		return expirationMillis;
	}

	public Integer getDefaultPageSize() {
		return defaultPageSize;
	}

	public Integer getDefaultOffset() {
		return defaultOffset;
	}

	public ZoneId getLibraryTimezone() {
		return libraryTimezone;
	}

	public int getLibraryClosingHour() {
		return libraryClosingHour;
	}
	
	

//	public String getDefaultLocale() {
//		return "ua";
//	}

	

//	public Float getDailyFee() {
//		return 50.0f;
//	}
//
//	public long getExpirationMillis() {
//		return TimeUnit.DAYS.toMillis(7);
//	}
//
//	public Integer getDefaultPageSize() {
//		return 2;
//	}
//
//	public Integer getDefaultOffset() {
//		return 0;
//	}
//
//	public ZoneId getLibraryTimezone() {
//		return ZoneId.of("Europe/Kiev");
//	}
//
//	public int getLibraryClosingHour() {
//		return 22;
//	}

}
