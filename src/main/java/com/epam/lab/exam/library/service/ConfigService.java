package com.epam.lab.exam.library.service;

import java.time.ZoneId;

public class ConfigService {

	private static final ConfigService INSTANCE = new ConfigService();
	
	private String defaultLocale;
	private Float dailyFee;
	private long expirationDays;
	private Integer defaultPageSize;
	private Integer defaultOffset;
	private ZoneId libraryTimezone;
	private int libraryClosingHour;
	private String checksumAlgorithm;


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

	public String getChecksumAlgorithm() {
		return checksumAlgorithm;
	}

	public void setChecksumAlgorithm(String checksumAlgorithm) {
		this.checksumAlgorithm = checksumAlgorithm;
	}

	public long getExpirationDays() {
		return expirationDays;
	}

	public void setExpirationDays(long expirationDays) {
		this.expirationDays = expirationDays;
	}
	
	

}
