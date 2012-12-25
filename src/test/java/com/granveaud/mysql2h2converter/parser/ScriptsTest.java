package com.granveaud.mysql2h2converter.parser;

import com.granveaud.mysql2h2converter.sql.Statement;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ScriptsTest {

    final static private Logger LOGGER = LoggerFactory.getLogger(ScriptsTest.class);

    @Test
    public void testScripts() throws Exception {
        for (String s : new String[]{
                "/scripts/wordpress.sql", "/scripts/drupal.sql"
        }) {
            LOGGER.info("Loading script " + s);
            List<Statement> statements = SQLParserManager.parse(new InputStreamReader(getClass().getResourceAsStream(s)));
            assertTrue(!statements.isEmpty());
        }
    }
}
