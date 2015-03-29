package com.granveaud.mysql2h2converter.converter;

import com.google.common.collect.Iterators;
import com.granveaud.mysql2h2converter.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class H2Converter {
    final static private Logger LOGGER = LoggerFactory.getLogger(H2Converter.class);

    static class ConverterIterator implements Iterator<Statement> {
        private Map<String, Integer> indexNameOccurrences = new HashMap<String, Integer>();
        private List<Statement> delayedStatements = new ArrayList<Statement>();

        private Iterator<Statement> sourceIterator;
        private List<Statement> nextStatements = new ArrayList<Statement>();
        private Iterator<Statement> nextStatementIterator, delayedStatementsIterator;

        ConverterIterator(Iterator<Statement> sourceIterator) {
            this.sourceIterator = sourceIterator;
            loadNextStatements();
        }

        private void loadNextStatements() {
            while (true) {
                Statement sourceNextStatement = sourceIterator.next();
                if (sourceNextStatement == null) {
                    // finished source statements
                    nextStatementIterator = null;
                    if (!delayedStatements.isEmpty()) {
                        // now iterate on delayed statements
                        delayedStatementsIterator = delayedStatements.iterator();
                    }
                    return;
                }

                nextStatements.clear();
                convertStatement(sourceNextStatement, nextStatements);

                if (!nextStatements.isEmpty()) {
                    nextStatementIterator = nextStatements.iterator();
                    return;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return (nextStatementIterator != null) || (delayedStatementsIterator != null && delayedStatementsIterator.hasNext());
        }

        @Override
        public Statement next() {
            // iterator is on delayed statements
            if (delayedStatementsIterator != null) {
                return delayedStatementsIterator.next();
            }

            // iterator is on conversion of source iterator
            Statement next = nextStatementIterator.next();
            if (!nextStatementIterator.hasNext()) {
                loadNextStatements();
            }

            return next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void convertStatement(Statement sourceStatement, List<Statement> result) {
            if (sourceStatement instanceof EmptyStatement) {
                // ignore empty statements
            } else if (sourceStatement instanceof LockTablesStatement || sourceStatement instanceof UnlockTablesStatement) {
                // do not copy, MySQL specific
            } else if (sourceStatement instanceof SetVariableStatement) {
                // do not copy, SET statement are usually MySQL specific
            } else if (sourceStatement instanceof StartTransactionStatement) {
                // replace with H2 equivalent
                result.add(new SetStatement("AUTOCOMMIT", Arrays.<Value>asList(new ExpressionValue("OFF"))));
            } else if (sourceStatement instanceof CommitTransactionStatement) {
                // replace with H2 equivalent
                result.add(new CommitTransactionStatement());
                result.add(new SetStatement("AUTOCOMMIT", Arrays.<Value>asList(new ExpressionValue("ON"))));
            } else if (sourceStatement instanceof UseStatement) {
                // USE dbName => SET SCHEMA dbName
                UseStatement useStatement = (UseStatement) sourceStatement;
                result.add(new SetStatement("SCHEMA", Arrays.<Value>asList(new StringValue(useStatement.getDbName()))));

            } else if (sourceStatement instanceof CreateDatabaseStatement) {
                // CREATE DATABASE => CREATE SCHEMA
                CreateDatabaseStatement createStatement = (CreateDatabaseStatement) sourceStatement;
                result.add(new CreateSchemaStatement(createStatement.getDbName(), createStatement.isIfNotExists()));

            } else if (sourceStatement instanceof CreateTableStatement) {
                CreateTableStatement createStatement = (CreateTableStatement) sourceStatement;

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

                result.add(sourceStatement);

            } else if (sourceStatement instanceof InsertStatement) {
                InsertStatement insertStatement = (InsertStatement) sourceStatement;

                // handle invalid '0000-00-00 00:00:00' datetime and binary values
                if (insertStatement.getValues() != null) {
                    handleInsertValues(insertStatement);
                }

                result.add(sourceStatement);

            } else {
                // general case: add statement unchanged
                result.add(sourceStatement);
            }
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
                if (constraint.getType().equals("KEY")) {
                    // TODO Translate index types
                    delayedStatements.add(new CreateIndexStatement(false, null, false, constraint.getIndexName(), createStatement.getTableName(), constraint.getIndexColumnNames()));
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
                    if (value instanceof StringValue) {
                        StringValue strValue = (StringValue) value;
                        if (strValue.getValue().equals("'0000-00-00 00:00:00'")) {
                            // replace '0000-00-00 00:00:00' datetime value
                            // TODO: this is not correct because '0000-00-00 00:00:00' could be a real string value
                            LOGGER.warn("Replace '0000-00-00 00:00:00' with valid H2 datetime (unsafe replacement)");
                            value = new StringValue("'0001-01-01 00:00:00'");
                        } else if (strValue.getValue().contains("\\")) {
                            // handle \n, \' ...
                            value = DbUtils.transformStringValue(strValue.getValue());
                        }

                        valueList.getValues().set(i, value);

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

    public static Iterator<Statement> convertScript(Iterator<Statement> sourceIterator) {
        return new ConverterIterator(sourceIterator);
    }
}
