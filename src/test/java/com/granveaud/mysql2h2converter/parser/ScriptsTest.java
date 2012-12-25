package com.granveaud.mysql2h2converter.parser;

import com.granveaud.mysql2h2converter.sql.Statement;
import org.junit.Test;

import java.io.InputStreamReader;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ScriptsTest {

    @Test
    public void testScripts() throws Exception {
		for (String s : new String[] {
				"/scripts/wordpress.sql", "/scripts/drupal.sql"
		}) {
			System.out.println("FILE " + s);
			List<Statement> statements = SQLParserManager.parse(new InputStreamReader(getClass().getResourceAsStream(s)));
            assertTrue(!statements.isEmpty());
		}
    }
}
