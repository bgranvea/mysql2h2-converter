package com.granveaud.mysql2h2converter.sql;

public class SetVariableStatement implements Statement {
    private Assignment assignment;

	public SetVariableStatement(Assignment assignment) {
		this.assignment = assignment;
	}

	@Override
    public String toString() {
        return "SET " + assignment;
    }
}
