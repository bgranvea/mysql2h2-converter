package com.granveaud.mysql2h2converter.sql;

public class IntegerValue implements Value {
	private Integer value;

	public IntegerValue(String str) {
		value = Integer.parseInt(str);
	}

	public Integer getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
