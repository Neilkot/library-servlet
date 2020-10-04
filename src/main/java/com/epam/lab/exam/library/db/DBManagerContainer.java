package com.epam.lab.exam.library.db;

public class DBManagerContainer {

	private static final DBManagerContainer INSTANCE = new DBManagerContainer();
	private DBManager dBManager;

	private DBManagerContainer() {
	}

	public static DBManagerContainer getInstance() {
		return INSTANCE;
	}

	public DBManager getdBManager() {
		if (dBManager == null) {
			throw new IllegalStateException("DBManager was not instantiated");
		}
		return dBManager;
	}

	public void setdBManager(DBManager dBManager) {
		this.dBManager = dBManager;
	}

}
