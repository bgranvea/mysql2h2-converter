package com.granveaud.mysql2h2converter.parser;

import com.granveaud.mysql2h2converter.sql.Statement;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class SQLParserManager {
    public static Statement parseStatement(String str) throws ParseException {
        return parseStatement(new StringReader(str));
    }

    public static Statement parseStatement(Reader reader) throws ParseException {
        SQLParser parser = new SQLParser(reader);
        return parser.Statement();
    }

    public static List<Statement> parse(String str) throws ParseException {
        return parse(new StringReader(str));
    }

    public static List<Statement> parse(Reader reader) throws ParseException {
        SQLParser parser = new SQLParser(reader);
        return parser.Script();
    }
}
