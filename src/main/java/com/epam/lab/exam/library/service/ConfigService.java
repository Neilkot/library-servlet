package com.epam.lab.exam.library.service;

import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

public class ConfigService {

	private static final ConfigService INSTANCE = new ConfigService();

	private ConfigService() {
	}

	public static ConfigService getInstance() {
		return INSTANCE;
	}

	public String getDefaultLocale() {
		return "ua";
	}

	public String getPropertyValue(String locale, String messageCode) {
		return messageCode;
	}

	public Float getDailyFee() {
		return 1.0f;
	}

	public long getExpirationMillis() {
		return TimeUnit.DAYS.toMillis(7);
	}

	public Integer getDefaultPageSize() {
		return 2;
	}

	public Integer getDefaultOffset() {
		return 0;
	}

	public ZoneId getLibraryTimezone() {
		return ZoneId.of("Europe/Kiev");
	}

	public int getLibraryClosingHour() {
		return 22;
	}

}
