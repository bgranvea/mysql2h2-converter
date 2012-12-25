package com.granveaud.mysql2h2converter.sql;

/*
TINYINT[(length)] [UNSIGNED] [ZEROFILL]
  | SMALLINT[(length)] [UNSIGNED] [ZEROFILL]
  | MEDIUMINT[(length)] [UNSIGNED] [ZEROFILL]
  | INT[(length)] [UNSIGNED] [ZEROFILL]
  | INTEGER[(length)] [UNSIGNED] [ZEROFILL]
  | BIGINT[(length)] [UNSIGNED] [ZEROFILL]
  | REAL[(length,decimals)] [UNSIGNED] [ZEROFILL]
  | DOUBLE[(length,decimals)] [UNSIGNED] [ZEROFILL]
  | FLOAT[(length,decimals)] [UNSIGNED] [ZEROFILL]
  | DECIMAL(length,decimals) [UNSIGNED] [ZEROFILL]
  | NUMERIC(length,decimals) [UNSIGNED] [ZEROFILL]
  | DATE
  | TIME
  | TIMESTAMP
  | DATETIME
  | CHAR(length) [BINARY | ASCII | UNICODE]
  | VARCHAR(length) [BINARY]
  | TINYBLOB
  | BLOB
  | MEDIUMBLOB
  | LONGBLOB
  | TINYTEXT
  | TEXT
  | MEDIUMTEXT
  | LONGTEXT
  | ENUM(value1,value2,value3,...)
  | SET(value1,value2,value3,...)
 */
public class ColumnType {
    private String name;
    private IntegerValue length;
    private IntegerValue decimals;
    private ValueList valueList;
    private boolean unsigned, zerofill, binary, ascii, unicode;
	private String charsetName, collationName;

	public ColumnType(String name, IntegerValue length, IntegerValue decimals, ValueList valueList, boolean unsigned, boolean zerofill, boolean binary, boolean ascii, boolean unicode, String charsetName, String collationName) {
		this.name = name;
		this.length = length;
		this.decimals = decimals;
		this.valueList = valueList;
		this.unsigned = unsigned;
		this.zerofill = zerofill;
		this.binary = binary;
		this.ascii = ascii;
		this.unicode = unicode;
		this.charsetName = charsetName;
		this.collationName = collationName;
	}

    public String getCharsetName() {
        return charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    public String getCollationName() {
        return collationName;
    }

    public void setCollationName(String collationName) {
        this.collationName = collationName;
    }

    public void setLength(IntegerValue length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return name + (length != null ? "(" + length + (decimals != null ? "," + decimals : "") + ")" : "") +
                (valueList != null ? valueList : "") +
                (unsigned ? " UNSIGNED" : "") +
                (zerofill ? " ZEROFILL" : "") +
                (binary ? " BINARY" : "") +
                (ascii ? " ASCII" : "") +
                (unicode ? " UNICODE" : "") +
				(charsetName != null ? " CHARACTER SET " + charsetName : "") +
				(collationName != null ? " COLLATION " + collationName : "");

    }
}
