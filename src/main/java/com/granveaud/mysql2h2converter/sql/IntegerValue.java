package com.granveaud.mysql2h2converter.sql;

import java.math.BigInteger;

public class IntegerValue implements Value {
	private BigInteger value;

	public IntegerValue(String str) {
		value = new BigInteger(str);
	}

	public BigInteger getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
