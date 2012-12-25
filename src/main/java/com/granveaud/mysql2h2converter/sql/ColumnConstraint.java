package com.granveaud.mysql2h2converter.sql;

import com.google.common.base.Joiner;

import java.util.List;

/*
  | [CONSTRAINT [symbol]] PRIMARY KEY [index_type] (index_col_name,...)
  | KEY [index_name] [index_type] (index_col_name,...)
  | INDEX [index_name] [index_type] (index_col_name,...)
  | [CONSTRAINT [symbol]] UNIQUE [INDEX]
        [index_name] [index_type] (index_col_name,...)
  | [FULLTEXT|SPATIAL] [INDEX] [index_name] (index_col_name,...)
  | [CONSTRAINT [symbol]] FOREIGN KEY
        [index_name] (index_col_name,...) [reference_definition]
  | CHECK (expr)

 */
public class ColumnConstraint {
    private boolean constraint;
    private String constraintName;
    private String type; // PRIMARY KEY, KEY, INDEX, UNIQUE [INDEX], [FULLTEXT|SPATIAL] INDEX, FOREIGN KEY, CHECK
    private List<ColumnName> indexColumnNames;
    private String indexType;
    private String indexName;
    private ColumnReference columnReference;
    private ExpressionValue checkExpr;

    public ColumnConstraint(boolean constraint, String constraintName, String type, List<ColumnName> indexColumnNames, String indexType, String indexName, ColumnReference columnReference, ExpressionValue checkExpr) {
        this.constraint = constraint;
        this.constraintName = constraintName;
        this.type = type;
        this.indexColumnNames = indexColumnNames;
        this.indexType = indexType;
        this.indexName = indexName;
        this.columnReference = columnReference;
        this.checkExpr = checkExpr;
    }

    public String getType() {
        return type;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public List<ColumnName> getIndexColumnNames() {
        return indexColumnNames;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    @Override
    public String toString() {
        return (constraint ? "CONSTRAINT " : "") +
                (constraintName != null ? constraintName + " " : "") +
                type +
                (indexName != null ? " " + indexName : "") +
                (indexType != null ? " USING " + indexType : "") +
                (indexColumnNames != null ? " (" + Joiner.on(',').join(indexColumnNames) + ")" : "") +
                (columnReference != null ? " " + columnReference : "") +
                (checkExpr != null ? " " + checkExpr : "");

    }
}
