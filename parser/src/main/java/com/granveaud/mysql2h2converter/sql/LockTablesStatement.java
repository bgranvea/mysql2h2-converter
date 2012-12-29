package com.granveaud.mysql2h2converter.sql;

import com.google.common.base.Joiner;

import java.util.List;

/*
LOCK TABLES
    tbl_name [AS alias] {READ [LOCAL] | [LOW_PRIORITY] WRITE}
    [, tbl_name [AS alias] {READ [LOCAL] | [LOW_PRIORITY] WRITE}] ...
 */
public class LockTablesStatement implements Statement {
	private List<LockTablesSpecification> specifications;

	public LockTablesStatement(List<LockTablesSpecification> specifications) {
		this.specifications = specifications;
	}

	@Override
	public String toString() {
		return "LOCK TABLES " + Joiner.on(',').join(specifications);
	}
}
