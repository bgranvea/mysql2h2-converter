package com.granveaud.mysql2h2converter.converter;

import com.granveaud.mysql2h2converter.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class H2Converter {
    final static private Logger LOGGER = LoggerFactory.getLogger(H2Converter.class);


    public List<Statement> convertScript(List<Statement> statements) {
        Map<String, Integer> indexNameOccurrences = new HashMap<String, Integer>();

        List<Statement> result = new ArrayList<Statement>();
        for (Statement st : statements) {
            if (st instanceof EmptyStatement) {
                // ignore empty statements
            } else if (st instanceof SetVariableStatement) {
                // do not copy, SET statement are usually MySQL specific
            } else if (st instanceof UseStatement) {
                 // USE dbName => SET SCHEMA dbName
                UseStatement useStatement = (UseStatement) st;
                result.add(new SetStatement("SCHEMA", Arrays.<Value>asList(new StringValue(useStatement.getDbName()))));

            } else if (st instanceof CreateDatabaseStatement) {
                // CREATE DATABASE => CREATE SCHEMA
                CreateDatabaseStatement createStatement = (CreateDatabaseStatement) st;
                result.add(new CreateSchemaStatement(createStatement.getDbName(), createStatement.isIfNotExists()));

            } else if (st instanceof CreateTableStatement) {
                CreateTableStatement createStatement = (CreateTableStatement) st;

                // ignore MySQL create table specific options
                createStatement.setOptions(null);

                // handle duplicate index names in KEY
                handleCreateTableDuplicateIndexNames(createStatement, indexNameOccurrences);

                // handle CHARACTER SET and COLLATION column definition
                handleCreateTableColumnDefinitions(createStatement);

                // handle KEY (colName(length))
                handleCreateTableKeyColumnNameLength(createStatement);

                result.add(st);

            } else if (st instanceof InsertStatement) {
                InsertStatement insertStatement = (InsertStatement) st;

                // handle invalid '0000-00-00 00:00:00' datetime and binary values
                if (insertStatement.getValues() != null) {
                    handleInsertValues(insertStatement);
                }

                result.add(st);

            } else {
                // general case: add statement unchanged
                result.add(st);
            }
        }

        return result;
    }

    private void handleCreateTableDuplicateIndexNames(CreateTableStatement createStatement, Map<String, Integer> indexNameOccurrences) {
        for (ColumnConstraint constraint : createStatement.getDefinition().getConstraints()) {
            if (constraint.getIndexName() != null) {
                String indexName = DbUtils.unescapeDbObjectName(constraint.getIndexName());

                int occurrence = indexNameOccurrences.containsKey(indexName) ? indexNameOccurrences.get(indexName) : 0;

                // increment occurrence for next time
                indexNameOccurrences.put(indexName, occurrence + 1);

                // replace with unique name
                constraint.setIndexName(indexName + "_" + occurrence);
            }
        }
    }

    private void handleCreateTableColumnDefinitions(CreateTableStatement createTableStatement) {
        for (ColumnDefinition def : createTableStatement.getDefinition().getColumnDefinitions()) {
            def.getColumnType().setCharsetName(null);
            def.getColumnType().setCollationName(null);
        }
    }

    private void handleCreateTableKeyColumnNameLength(CreateTableStatement createTableStatement) {
        for (ColumnConstraint constraint : createTableStatement.getDefinition().getConstraints()) {
            for (ColumnName columnName : constraint.getIndexColumnNames()) {
                columnName.setLength(null);
            }
        }
    }

    private void handleInsertValues(InsertStatement insertStatement) {
        for (ValueList valueList : insertStatement.getValues()) {
            for (int i = 0; i < valueList.getValues().size(); i++) {
                Value value = valueList.getValues().get(i);
                if (value instanceof StringValue && "'0000-00-00 00:00:00'".equals(value.toString())) {
                    // replace '0000-00-00 00:00:00' datetime value
                    // TODO: this is not correct because '0000-00-00 00:00:00' could be a real string value
                    LOGGER.warn("Replace '0000-00-00 00:00:00' with valid H2 datetime");
                    valueList.getValues().set(i, new StringValue("'0001-01-01 00:00:00'"));
                } else if (value instanceof BinaryValue) {
                    // be sure to use X'hex' format
                    ((BinaryValue) value).setFormat(BinaryValue.Format.FORMAT1);
                }
            }
        }
    }
}
