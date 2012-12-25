package com.granveaud.mysql2h2converter.sql;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class BinaryValue implements Value {
    public enum Format {
        FORMAT1, FORMAT2
    }

    private Format format;
    private byte[] value;

    public BinaryValue(Format format, String hex) {
        this.format = format;
        try {
            value = Hex.decodeHex(hex.toCharArray());
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Cannot decode hex string '" + hex + "'", e);
        }
    }

    public byte[] getValue() {
        return value;
    }

    @Override
    public String toString() {
        return (format == Format.FORMAT1 ? "X'" : "0x") +
                new String(Hex.encodeHex(value)) +
                (format == Format.FORMAT1 ? "'" : "");
    }
}
