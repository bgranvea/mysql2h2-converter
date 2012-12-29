package com.granveaud.mysql2h2converter.sql;

public class BitFieldValue implements Value {
    public enum Format {
        FORMAT1, FORMAT2
    }

    private Format format;
    private String bits;

    public BitFieldValue(Format format, String bits) {
        this.format = format;
		this.bits = bits;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

	public String getBits() {
		return bits;
	}

	@Override
    public String toString() {
        return (format == Format.FORMAT1 ? "b'" : "0b") +
                bits +
                (format == Format.FORMAT1 ? "'" : "");
    }
}
