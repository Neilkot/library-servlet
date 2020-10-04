package com.epam.lab.exam.library;

import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.epam.lab.exam.library.db.DBManagerContainer;
import com.epam.lab.exam.library.db.WebAppDBManager;

@WebListener
public class StartupListener implements ServletContextListener {

	private final DBManagerContainer container = DBManagerContainer.getInstance();

	public void contextInitialized(ServletContextEvent sce) {
		try {
			container.setdBManager(new WebAppDBManager());
		} catch (NamingException e) {
			throw new IllegalStateException("Couldn't initialized DbContainer. Cause: " + e.getMessage(), e);
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

}
