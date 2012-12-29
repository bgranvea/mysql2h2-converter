package com.granveaud.mysql2h2converter.sql;

public class SystemVariableValue implements Value {
	private String name;

	public SystemVariableValue(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
