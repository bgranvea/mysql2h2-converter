package com.granveaud.mysql2h2converter.sql;

public class StringValue implements Value {
	private String value;

	public StringValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}
}
