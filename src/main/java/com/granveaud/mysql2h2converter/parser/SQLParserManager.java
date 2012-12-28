package com.granveaud.mysql2h2converter.parser;

import com.google.common.collect.Lists;
import com.granveaud.mysql2h2converter.sql.Statement;

import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

public class SQLParserManager {
    static class ScriptIterator implements Iterator<Statement> {
        private SQLParser parser;
        private Statement nextStatement;

        ScriptIterator(SQLParser parser) {
            this.parser = parser;
            loadNextStatement();
        }

        private void loadNextStatement() {
            try {
                nextStatement = parser.ScriptStatement();
            } catch (ParseException e) {
                throw new IllegalArgumentException("Cannot load next statement", e);
            }
        }

        @Override
        public boolean hasNext() {
            return (nextStatement != null);
        }

        @Override
        public Statement next() {
            Statement result = nextStatement;
            loadNextStatement();

            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static Statement parseStatement(String str) throws ParseException {
        return parseStatement(new StringReader(str));
    }

    public static Statement parseStatement(Reader reader) throws ParseException {
        SQLParser parser = new SQLParser(reader);
        return parser.Statement();
    }

    public static Iterator<Statement> parseScript(Reader reader) throws ParseException {
        SQLParser parser = new SQLParser(reader);
        return new ScriptIterator(parser);
    }
}
