package com.granveaud.mysql2h2converter.sql;

public class SetStatement implements Statement {
    private Assignment assignment;

	public SetStatement(Assignment assignment) {
		this.assignment = assignment;
	}

	@Override
    public String toString() {
        return "SET " + assignment;
    }
}
