package com.epam.lab.exam.library;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.epam.lab.exam.library.db.DBManagerContainer;
import com.epam.lab.exam.library.db.WebAppDBManager;
import com.epam.lab.exam.library.service.ConfigService;

@WebListener
public class StartupListener implements ServletContextListener {

	private final DBManagerContainer container = DBManagerContainer.getInstance();
	private final ConfigService configService = ConfigService.getInstance();

	public void contextInitialized(ServletContextEvent sce) {
		try {
			container.setdBManager(new WebAppDBManager());
			Properties props = new Properties();
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream input = classLoader.getResourceAsStream("libraryConfig.properties");
			props.load(input);
			initializeConfigService(props);
		} catch (NamingException | IOException e) {
			throw new IllegalStateException("Couldn't initialized DbContainer. Cause: " + e.getMessage(), e);
		}
	}

	private void initializeConfigService(Properties props) {
		configService.setDefaultLocale(props.getProperty("defaultLocale"));
		configService.setDailyFee(Float.valueOf(props.getProperty("dailyFee")));
		configService.setExpirationMillis(TimeUnit.DAYS.toMillis(Long.parseLong(props.getProperty("expirationMillis"))));
		configService.setDefaultPageSize(Integer.parseInt(props.getProperty("defaultPageSize")));
		configService.setDefaultOffset(Integer.parseInt(props.getProperty("defaultOffset")));
		configService.setLibraryClosingHour(Integer.parseInt(props.getProperty("libraryClosingHour")));
		configService.setLibraryTimezone(ZoneId.of(props.getProperty("libraryTimezone")));
	}

	public void contextDestroyed(ServletContextEvent sce) {
	
	}

}
