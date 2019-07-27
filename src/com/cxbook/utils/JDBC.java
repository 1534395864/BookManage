package com.cxbook.utils;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JDBC {
	private final static ComboPooledDataSource ds = new ComboPooledDataSource("c3p0-config.xml");

	public static DataSource getc3p0() {
		return ds;
	}
}
