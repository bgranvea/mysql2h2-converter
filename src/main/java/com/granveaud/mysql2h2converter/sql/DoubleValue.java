package com.granveaud.mysql2h2converter.sql;

public class DoubleValue implements Value {
	private Double value;

	public DoubleValue(String str) {
		value = Double.parseDouble(str);
	}

	public Double getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
