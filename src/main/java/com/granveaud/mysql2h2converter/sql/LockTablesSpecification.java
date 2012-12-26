package com.granveaud.mysql2h2converter.sql;

/*
    tbl_name [AS alias] {READ [LOCAL] | [LOW_PRIORITY] WRITE}
    [, tbl_name [AS alias] {READ [LOCAL] | [LOW_PRIORITY] WRITE}] ...
 */
public class LockTablesSpecification {
	private String tableName;
	private String alias;
	private boolean read, local, lowPriority, write;

	public LockTablesSpecification(String tableName, String alias, boolean read, boolean local, boolean lowPriority, boolean write) {
		this.tableName = tableName;
		this.alias = alias;
		this.read = read;
		this.local = local;
		this.lowPriority = lowPriority;
		this.write = write;
	}

	@Override
	public String toString() {
		return tableName +
				(alias != null ? " AS " + alias : "") +
				(read ? " READ" : "") +
				(local ? " LOCAL" : "") +
				(lowPriority ? " LOW_PRIORITY" : "") +
				(write ? " WRITE" : "");
	}
}
