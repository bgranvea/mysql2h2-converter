package com.granveaud.mysql2h2converter.sql;

public class CommitTransactionStatement implements Statement {
    @Override
    public String toString() {
        return "COMMIT";
    }
}
