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
        List<Statement> delayedStatements = new ArrayList<Statement>();
        for (Statement st : statements) {
            if (st instanceof EmptyStatement) {
                // ignore empty statements
			} else if (st instanceof LockTablesStatement || st instanceof UnlockTablesStatement) {
				// do not copy, MySQL specific
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

                // handle duplicate index names in KEY and index names conflicting with reserved keywords
                handleCreateTableIndexNames(createStatement, indexNameOccurrences);

                // handle CHARACTER SET and COLLATION column definition, remove ON UPDATE
                handleCreateTableColumnDefinitions(createStatement);

                // handle KEY (colName(length)): length cannot be specified with H2
                handleCreateTableKeyColumnNameLength(createStatement);

                // 1) handle when foreign key check is disabled: add foreign key constraints at the end
                // 2) remove USING indexType
                handleCreateTableConstraints(createStatement, delayedStatements);

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

        result.addAll(delayedStatements);

        return result;
    }

    private void handleCreateTableIndexNames(CreateTableStatement createStatement, Map<String, Integer> indexNameOccurrences) {
        for (ColumnConstraint constraint : createStatement.getDefinition().getConstraints()) {
            if (constraint.getIndexName() != null) {
                String indexName = DbUtils.unescapeDbObjectName(constraint.getIndexName()).toUpperCase();

				// replace with unique name
                Integer occurrence = indexNameOccurrences.containsKey(indexName) ? indexNameOccurrences.get(indexName) : 0;
                constraint.setIndexName(indexName + "_" + occurrence);

				// increment occurrence for next time
				indexNameOccurrences.put(indexName, occurrence + 1);
            }
        }
    }

    private void handleCreateTableConstraints(CreateTableStatement createStatement, List<Statement> delayedStatements) {
        Iterator<ColumnConstraint> it = createStatement.getDefinition().getConstraints().iterator();
        while (it.hasNext()) {
            ColumnConstraint constraint = it.next();

            if (constraint.getType().equals("FOREIGN KEY")) {
                delayedStatements.add(new AlterTableStatement(false, createStatement.getTableName(), Arrays.asList(new AlterTableSpecification("ADD", constraint))));
                it.remove();
            }

            constraint.setIndexType(null);
        }
    }

    private void handleCreateTableColumnDefinitions(CreateTableStatement createTableStatement) {
        for (ColumnDefinition def : createTableStatement.getDefinition().getColumnDefinitions()) {
            def.getColumnType().setCharsetName(null);
            def.getColumnType().setCollationName(null);
            def.setUpdateValue(null);
        }
    }

    private void handleCreateTableKeyColumnNameLength(CreateTableStatement createTableStatement) {
        for (ColumnConstraint constraint : createTableStatement.getDefinition().getConstraints()) {
            for (ColumnName columnName : constraint.getIndexColumnNames()) {
				if (columnName.getLength() != null) {
					LOGGER.warn("Remove length value in key/index column name");
                	columnName.setLength(null);
				}
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
                    LOGGER.warn("Replace '0000-00-00 00:00:00' with valid H2 datetime (unsafe replacement)");
                    valueList.getValues().set(i, new StringValue("'0001-01-01 00:00:00'"));
                } else if (value instanceof BinaryValue) {
                    // be sure to use X'hex' format
                    ((BinaryValue) value).setFormat(BinaryValue.Format.FORMAT1);
                } else if (value instanceof BitFieldValue) {
					BitFieldValue bitFieldValue = (BitFieldValue) value;
					if (bitFieldValue.getBits().equals("1")) {
						valueList.getValues().set(i, new BooleanValue(true));
					} else if (bitFieldValue.getBits().equals("0")) {
						valueList.getValues().set(i, new BooleanValue(false));
					} else {
						LOGGER.warn("Don't know how to convert BitFieldValue " + bitFieldValue.getBits() + " for H2");
					}
				}
            }
        }
    }
}
