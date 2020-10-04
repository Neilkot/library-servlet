package com.epam.lab.exam.library.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.concurrent.TimeUnit;

public class ConfigService {

	private static final ConfigService INSTANCE = new ConfigService();

	private ConfigService() {
	}

	public static ConfigService getInstance() {
		return INSTANCE;
	}

	public String getDefaultLocale() {
		return "";
	}

	public String getPropertyValue(String locale, String messageCode) {
		return "";
	}

	public Float getDailyFee() {
		return 1.0f;
	}

	public long getExpirationMillis() {
		return TimeUnit.DAYS.toMillis(7);
	}
	
	
	
	
}
