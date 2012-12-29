package com.granveaud.mysql2h2converter.sql;

public class ExpressionValue implements Value {
	private String expr;

	public ExpressionValue(String expr) {
		this.expr = expr;
	}

	public String getValue() {
		return expr;
	}

	@Override
	public String toString() {
		return expr;
	}
}
