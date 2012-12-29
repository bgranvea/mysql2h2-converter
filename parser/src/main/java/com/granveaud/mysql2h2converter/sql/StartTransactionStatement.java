package com.granveaud.mysql2h2converter.sql;

public class StartTransactionStatement implements Statement {
    @Override
    public String toString() {
        return "START TRANSACTION";
    }
}
