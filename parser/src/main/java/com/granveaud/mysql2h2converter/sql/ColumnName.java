package com.granveaud.mysql2h2converter.sql;

/*
    col_name [(length)] [ASC | DESC]
 */
public class ColumnName {
    private String name;
    private IntegerValue length;
    private boolean asc, desc;

    public ColumnName(String name, IntegerValue length, boolean asc, boolean desc) {
        this.name = name;
        this.length = length;
        this.asc = asc;
        this.desc = desc;
    }

	public IntegerValue getLength() {
		return length;
	}

	public void setLength(IntegerValue length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return name + (length != null ? "(" + length + ")" : "") + (asc ? " ASC" : "") + (desc ? " DESC" : "");
    }
}
