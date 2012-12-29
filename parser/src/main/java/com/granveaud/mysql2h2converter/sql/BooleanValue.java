package com.granveaud.mysql2h2converter.sql;

public class BooleanValue implements Value {
    private boolean value;

	public BooleanValue(boolean value) {
		this.value = value;
	}

	public boolean isValue() {
		return value;
	}

	@Override
    public String toString() {
        return (value ? "TRUE" : "FALSE");
    }
}
