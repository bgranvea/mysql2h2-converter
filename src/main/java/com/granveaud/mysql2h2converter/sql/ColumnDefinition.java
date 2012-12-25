package com.granveaud.mysql2h2converter.sql;
/*
column_definition:
        col_name type [NOT NULL | NULL] [DEFAULT default_value]
        [AUTO_INCREMENT] [[PRIMARY] KEY] [COMMENT 'string']
        [reference_definition]
  */
public class ColumnDefinition {
    private ColumnName columnName;
    private ColumnType columnType;
    private boolean nullOption, notNullOption, autoIncrement, primary, key;
    private Value defaultValue;
    private Value updateValue;
    private StringValue comment;
    private ColumnReference columnReference;

    public ColumnDefinition(ColumnName columnName, ColumnType columnType, boolean nullOption, boolean notNullOption, boolean autoIncrement, boolean primary, boolean key, Value defaultValue, Value updateValue, StringValue comment, ColumnReference columnReference) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.nullOption = nullOption;
        this.notNullOption = notNullOption;
        this.autoIncrement = autoIncrement;
        this.primary = primary;
        this.key = key;
        this.defaultValue = defaultValue;
        this.updateValue = updateValue;
        this.comment = comment;
        this.columnReference = columnReference;
    }

    @Override
    public String toString() {
        return columnName.toString() + " " + columnType +
                (notNullOption ? " NOT NULL" : "") +
                (nullOption ? " NULL" : "") +
                (defaultValue != null ? " DEFAULT " + defaultValue: "") +
                (updateValue != null ? " ON UPDATE " + updateValue: "") +
                (autoIncrement ? " AUTO_INCREMENT" : "") +
                (primary ? " PRIMARY" : "") +
                (key ? " KEY" : "") +
                (comment != null ? " COMMENT " + comment : "") +
                (columnReference != null ? columnReference : "");
    }
}
