package com.epam.lab.exam.library.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.lab.exam.library.dto.BookRequestDTO;
import com.epam.lab.exam.library.exceptins.ClientRequestException;
import com.epam.lab.exam.library.exceptins.ErrorType;
import com.epam.lab.exam.library.model.RequestType;

public class RequestExpirationService {

	private static final RequestExpirationService INSTANCE = new RequestExpirationService();
	private final ConfigService configService = ConfigService.getInstance();

	private final Logger logger = LogManager.getLogger(this.getClass());

	private RequestExpirationService() {
	}

	public static RequestExpirationService getInstance() {
		return INSTANCE;
	}

	public void applyFee(List<BookRequestDTO> requests) {
		requests.stream().filter(r -> r.getReturnDate() == null).filter(this::isExpired)
				.forEach(r -> r.setFee(calculateFee(r.getExpirationDate())));
	}

	public Instant calculateExpirationDate(RequestType requestType) throws ClientRequestException {
		Instant now = Instant.now();
		switch (requestType) {
		case ABONEMENT:
			return now.plus(configService.getExpirationDays(), ChronoUnit.DAYS);
		case READING_AREA:
			if (!isLibraryOpen()) {
				throw new ClientRequestException(ErrorType.LIBRARY_IS_CLOSED);
			}
			Instant expirationDate = now.plus(getHoursTillEndOfDay(), ChronoUnit.HOURS);
			ZonedDateTime exp = ZonedDateTime.ofInstant(expirationDate, configService.getLibraryTimezone());
			exp = exp.withMinute(0).withSecond(0);
			return exp.toInstant();
		default:
			logger.warn("RequestType not supported={}", requestType);
			throw new IllegalArgumentException("request type not supported: " + requestType);
		}
	}

	private boolean isExpired(BookRequestDTO request) {
		Instant now = Instant.now();
		Instant expirationDate = request.getExpirationDate();
		return expirationDate.isBefore(now);
	}

	private Float calculateFee(Instant expirationDate) {
		Float dailyFee = configService.getDailyFee();
		Instant now = Instant.now();
		ZonedDateTime current = ZonedDateTime.ofInstant(now, ZoneId.of("UTC"));
		ZonedDateTime expire = ZonedDateTime.ofInstant(expirationDate, ZoneId.of("UTC"));
		long daysExpired = ChronoUnit.DAYS.between(current, expire) + 1;
		BigDecimal bigDecimal = new BigDecimal(daysExpired * dailyFee);
		bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		return bigDecimal.floatValue();
	}

	private int getHoursTillEndOfDay() throws ClientRequestException {
		int endHour = configService.getLibraryClosingHour();
		int hourNow = ZonedDateTime.now(configService.getLibraryTimezone()).getHour();
		return endHour - hourNow;
	}

	private boolean isLibraryOpen() {
		return ZonedDateTime.now(configService.getLibraryTimezone()).getHour() < configService.getLibraryClosingHour();
	}
}
