package com.granveaud.mysql2h2converter.sql;

public class Assignment {
	private String columnName;
	private Value value;

	public Assignment(String columnName, Value value) {
		this.columnName = columnName;
		this.value = value;
	}

	@Override
    public String toString() {
        return columnName + "=" + value;
    }
}
