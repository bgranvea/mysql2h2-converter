package com.granveaud.mysql2h2converter.sql;

public class JdbcPlaceholderValue implements Value {
	@Override
	public String toString() {
		return "?";
	}
}
