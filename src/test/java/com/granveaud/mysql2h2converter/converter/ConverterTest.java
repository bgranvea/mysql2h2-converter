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
    public void testScripts() throws Exception {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test;MODE=MySQL");

		String sql = "CREATE TABLE IF NOT EXISTS `file_managed` (`fid` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'File ID.',`uid` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT 'The users.uid of the user who is associated with the file.',`filename` varchar(255) NOT NULL DEFAULT '' COMMENT 'Name of the file with no path components. This may differ from the basename of the URI if the file is renamed to avoid overwriting an existing file.',`uri` varchar(255) NOT NULL DEFAULT '' COMMENT 'The URI to access the file (either local or remote).',`filemime` varchar(255) NOT NULL DEFAULT '' COMMENT 'The file’s MIME type.',`filesize` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT 'The size of the file in bytes.',`status` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'A field indicating the status of the file. Two status are defined in core: temporary (0) and permanent (1). Temporary files older than DRUPAL_MAXIMUM_TEMP_FILE_AGE will be removed during a cron run.',`timestamp` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT 'UNIX timestamp for when the file was added.',PRIMARY KEY (`fid`),UNIQUE KEY `uri` (`uri`),KEY `uid` (`uid`),KEY `status` (`status`),KEY \"timestamp\" (`timestamp`))";
		java.sql.Statement sqlStat2 = conn.createStatement();
		sqlStat2.execute(sql);
		sqlStat2.close();

		for (String s : new String[] {
				"wordpress.sql", "drupal.sql"
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
