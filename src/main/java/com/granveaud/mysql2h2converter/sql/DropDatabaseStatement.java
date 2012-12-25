package com.granveaud.mysql2h2converter.sql;

public class DropDatabaseStatement implements Statement {
    private String dbName;
    private boolean ifExists;

    public DropDatabaseStatement(String dbName, boolean ifExists) {
        this.dbName = dbName;
        this.ifExists = ifExists;
    }

    @Override
    public String toString() {
        return "DROP DATABASE " + (ifExists ? "IF EXISTS " : "") + dbName;
    }
}
