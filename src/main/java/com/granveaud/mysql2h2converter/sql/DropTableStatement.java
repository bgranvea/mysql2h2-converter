package com.granveaud.mysql2h2converter.sql;

import com.google.common.base.Joiner;

import java.util.List;

/*
DROP [TEMPORARY] TABLE [IF EXISTS]
tbl_name [, tbl_name] ...
 */

public class DropTableStatement implements Statement {
    private boolean temporary, ifExists;
    private List<String> tableNames;

    public DropTableStatement(boolean temporary, boolean ifExists, List<String> tableNames) {
        this.temporary = temporary;
        this.ifExists = ifExists;
        this.tableNames = tableNames;
    }

    @Override
    public String toString() {
        return "DROP "+ (temporary ? "TEMPORARY " : "") + "TABLE " + (ifExists ? "IF EXISTS " : "") +
                Joiner.on(',').join(tableNames);
    }
}
