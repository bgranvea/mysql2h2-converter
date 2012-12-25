package com.granveaud.mysql2h2converter.sql;

public class UseStatement implements Statement {
    private String dbName;

    public UseStatement(String dbName) {
        this.dbName = dbName;
    }

    public String getDbName() {
        return dbName;
    }

    @Override
    public String toString() {
        return "USE " + dbName;
    }
}
