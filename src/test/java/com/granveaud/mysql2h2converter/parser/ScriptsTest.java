package com.granveaud.mysql2h2converter.parser;

import com.granveaud.mysql2h2converter.converter.H2Converter;
import com.granveaud.mysql2h2converter.sql.Statement;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ScriptsTest {

    final static private Logger LOGGER = LoggerFactory.getLogger(ScriptsTest.class);

    @Test
    public void testScripts() throws Exception {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test;MODE=MySQL");

		for (String s : new String[] {
				"/scripts/wordpress.sql", "/scripts/drupal.sql"
		}) {
            LOGGER.info("Loading script " + s);
			List<Statement> statements = SQLParserManager.parse(new InputStreamReader(getClass().getResourceAsStream(s)));
            assertTrue(!statements.isEmpty());

            LOGGER.info("Conversion");
            List<Statement> h2Statements = new H2Converter().convertScript(statements);

            LOGGER.info("Load into H2");
            for (Statement stat : h2Statements) {
                java.sql.Statement sqlStat = conn.createStatement();
                LOGGER.info("EXECUTE " + stat.toString());

                sqlStat.execute(stat.toString());
            }
		}

        conn.close();
    }
}
