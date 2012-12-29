package com.granveaud.mysql2h2converter.sql;

import com.google.common.base.Joiner;

import java.util.List;

public class UnlockTablesStatement implements Statement {
	@Override
	public String toString() {
		return "UNLOCK TABLES";
	}
}
