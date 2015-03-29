package com.granveaud.mysql2h2converter.sql;

import com.google.common.base.Joiner;

import java.util.List;

/*
reference_definition:
    REFERENCES tbl_name [(index_col_name,...)]
               [MATCH FULL | MATCH PARTIAL]
               [ON DELETE reference_option]
               [ON UPDATE reference_option]

reference_option:
    RESTRICT | CASCADE | SET NULL | NO ACTION | SET DEFAULT
 */
public class ColumnReference {
    private String tableName;
    private List<String> columnNames;
    private boolean matchFull, matchPartial;
    private String onDeleteOption, onUpdateOption;

    public ColumnReference(String tableName, List<String> columnNames, boolean matchFull, boolean matchPartial, String onDeleteOption, String onUpdateOption) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.matchFull = matchFull;
        this.matchPartial = matchPartial;
        this.onDeleteOption = onDeleteOption;
        this.onUpdateOption = onUpdateOption;
    }

    @Override
    public String toString() {
        return "REFERENCES " + tableName +
                (columnNames != null ? " (" + Joiner.on(',').join(columnNames) + ")" : "") +
                (matchFull ? " MATCH FULL" : "") +
                (matchPartial ? " MATCH PARTIAL" : "") +
                (onDeleteOption != null ? " ON DELETE " + onDeleteOption : "") +
                (onUpdateOption != null ? " ON UPDATE " + onUpdateOption : "");
    }
}
