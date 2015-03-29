package com.granveaud.mysql2h2converter.sql;

import com.google.common.base.Joiner;

import java.util.List;

/*
CREATE
   [ UNIQUE ] [ index_type ] INDEX [ [ IF NOT EXISTS ] index_name ]
 | PRIMARY KEY [ HASH ]
ON tbl_name (index_col_name,...)

index_type:
  HASH | SPATIAL

index_col_name:
    col_name [ASC | DESC] [ NULLS

 */
public class CreateIndexStatement implements Statement {
    private boolean unique, ifNotExists;
    private String tableName;
    private String indexType;
    private String indexName;
    private List<ColumnName> columnNames;

    public CreateIndexStatement(boolean unique, String indexType, boolean ifNotExists, String indexName, String tableName, List<ColumnName> columnNames) {
        this.unique = unique;
        this.indexName = indexName;
        this.indexType = indexType;
        this.ifNotExists = ifNotExists;
        this.tableName = tableName;
        this.columnNames = columnNames;
    }

    @Override
    public String toString() {
        return "CREATE" + (unique ? " UNIQUE": "") + ( indexType != null ? " " + indexType : "" ) + " INDEX" + (ifNotExists ? " IF NOT EXISTS" : "") +
                ( indexName != null ? " " + indexName : "" ) +
                " ON " + tableName + " (" + Joiner.on(',').join(columnNames) + ")"
                ;
    }
}
