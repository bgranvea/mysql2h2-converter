package com.granveaud.mysql2h2converter.sql;

public class BinaryValue implements Value {
    public enum Format {
        FORMAT1, FORMAT2
    }

    private Format format;
    private String hex;

    public BinaryValue(Format format, String hex) {
        this.format = format;
		this.hex = hex;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

	public String getHex() {
		return hex;
	}

	@Override

    public String toString() {
        return (format == Format.FORMAT1 ? "X'" : "0x") +
                hex +
                (format == Format.FORMAT1 ? "'" : "");
    }
}
