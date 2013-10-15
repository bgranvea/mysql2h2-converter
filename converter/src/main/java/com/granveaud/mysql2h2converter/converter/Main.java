package com.granveaud.mysql2h2converter.converter;

import com.granveaud.mysql2h2converter.parser.ParseException;
import com.granveaud.mysql2h2converter.parser.SQLParserManager;
import com.granveaud.mysql2h2converter.sql.Statement;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;

public class Main {
    final static private Charset INPUT_CHARSET = Charset.forName("UTF-8");

    public static void main(String[] args) throws IOException, ParseException {
        if (args.length < 1) {
            System.err.println("Usage: input_file1 input_file2...");
            System.exit(1);
        }

        for (String str : args) {
            Iterator<Statement> sourceIterator = SQLParserManager.parseScript(new InputStreamReader(new FileInputStream(str), INPUT_CHARSET));

            // conversion and execution
            Iterator<Statement> it = H2Converter.convertScript(sourceIterator);
            while (it.hasNext()) {
                Statement st = it.next();

                // output to stdout
                System.out.println(st.toString() + ";");
            }
        }
    }
}
