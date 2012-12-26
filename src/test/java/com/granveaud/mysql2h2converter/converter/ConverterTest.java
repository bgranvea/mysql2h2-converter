package com.granveaud.mysql2h2converter.converter;

import com.granveaud.mysql2h2converter.parser.SQLParserManager;
import com.granveaud.mysql2h2converter.sql.Statement;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ConverterTest {

    final static private Logger LOGGER = LoggerFactory.getLogger(ConverterTest.class);

    @Test
    public void testConverter() throws Exception {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test;MODE=MySQL");

		for (String s : new String[] {
				"wordpress.sql", "drupal.sql", "xwiki.sql", "xwiki-no-foreign-key-checks.sql"
		}) {
            LOGGER.info("Loading script " + s);
			List<Statement> statements = SQLParserManager.parse(new InputStreamReader(getClass().getResourceAsStream("/scripts/" + s)));

            LOGGER.info("Conversion");
            List<Statement> h2Statements = new H2Converter().convertScript(statements);

            LOGGER.info("Load into H2");
            for (Statement stat : h2Statements) {
                java.sql.Statement sqlStat = conn.createStatement();
                sqlStat.execute(stat.toString());
				sqlStat.close();
            }
            LOGGER.info("Done");
		}

        conn.close();
    }
}
