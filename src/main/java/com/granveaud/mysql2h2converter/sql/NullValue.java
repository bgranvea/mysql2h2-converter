package com.granveaud.mysql2h2converter.sql;

public class NullValue implements Value {
	@Override
	public String toString() {
		return "NULL";
	}
}
