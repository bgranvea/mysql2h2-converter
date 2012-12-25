package com.granveaud.mysql2h2converter.converter;

import com.granveaud.mysql2h2converter.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class H2Converter {
    final static private Logger LOGGER = LoggerFactory.getLogger(H2Converter.class);

    public List<Statement> convertScript(List<Statement> statements) {
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
                // ignore MySQL create table specific options
                CreateTableStatement createStatement = (CreateTableStatement) st;
                createStatement.setOptions(null);

                result.add(st);
            } else {
                // general case: add statement unchanged
                result.add(st);
            }
        }

        return result;
    }

    public void resetState() {
    }
}
