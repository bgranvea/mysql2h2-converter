package com.granveaud.mysql2h2converter.parser;

import com.google.common.collect.Lists;
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
                "wordpress.sql", "drupal.sql", "xwiki.sql", "xwiki-no-foreign-key-checks.sql", "xwiki-sqlyog.sql"
        }) {
            LOGGER.info("Loading script " + s);
            List<Statement> statements = Lists.newArrayList(SQLParserManager.parseScript(new InputStreamReader(getClass().getResourceAsStream("/scripts/" + s))));
            assertTrue(!statements.isEmpty());
        }
    }
}
