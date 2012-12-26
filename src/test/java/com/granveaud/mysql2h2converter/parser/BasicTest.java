package com.granveaud.mysql2h2converter.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BasicTest {

    private void assertStatementEquals(String str) throws ParseException {
        assertStatementEquals(str, str);
    }

    private void assertStatementEquals(String str, String compareStr) throws ParseException {
        assertEquals(compareStr, SQLParserManager.parseStatement(str).toString());
    }

    @Test
    public void testIgnoreCase() throws Exception {
        String str = "use DB";
        assertStatementEquals(str, str.toUpperCase());
    }

    @Test
    public void testUse() throws Exception {
        String str = "USE db";
        assertStatementEquals(str);
    }

    @Test
    public void testCreateDatabase() throws Exception {
        String str = "CREATE DATABASE db";
        assertStatementEquals(str);

        str = "CREATE DATABASE IF NOT EXISTS db";
        assertStatementEquals(str);
    }

    @Test
    public void testDropDatabase() throws Exception {
        String str = "DROP DATABASE db";
        assertStatementEquals(str);

        str = "DROP DATABASE IF EXISTS db";
        assertStatementEquals(str);
    }

    @Test
    public void testDropTable() throws Exception {
        String str = "DROP TABLE t1,t2";
        assertStatementEquals(str);

        str = "DROP TEMPORARY TABLE IF EXISTS t1";
        assertStatementEquals(str);
    }

    @Test
    public void testCreateTable() throws Exception {
        String str = "CREATE TABLE test (" +
                "t1 int(10) NOT NULL AUTO_INCREMENT," +
                "t2 int(10) NOT NULL," +
                "t3 varchar(55) DEFAULT ''," +
                "PRIMARY KEY (t1)," +
                "UNIQUE KEY u1 (t1,t2)," +
                "KEY k1 (t2)," +
                "CONSTRAINT c1 FOREIGN KEY (t2) REFERENCES test2 (t2) ON DELETE CASCADE" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8";
        assertStatementEquals(str);
    }

	@Test
	public void testAlterTable() throws Exception {
		String str = "ALTER TABLE test ADD CONSTRAINT c1 FOREIGN KEY (f1) REFERENCES test2 (t2)";
		assertStatementEquals(str);
	}

	@Test
	public void testLockUnlockTables() throws Exception {
		String str = "LOCK TABLES t1 AS t1 READ LOCAL";
		assertStatementEquals(str);

		str = "LOCK TABLES t1 AS t1 LOW_PRIORITY WRITE";
		assertStatementEquals(str);

		str = "UNLOCK TABLES";
		assertStatementEquals(str);
	}

    @Test
    public void testInsert() throws Exception {
        String str = "INSERT INTO test VALUES (1,'test',5.0,b'101010'),(2,'test',6.0,b'101010'),(0x036072ff,'test',X'036072ff',0b101010)";
        assertStatementEquals(str);
    }

    @Test
	public void testCharLiteralEscaping() throws Exception {
		/*
        String str = "SET SQL_MODE='this is a test '' test2 \\' '' \\' test3'";
        assertStatementEquals(str);

        str = "SET SQL_MODE='this is a test '' test2 \\' '' \\' test3 \\'";
        assertStatementEquals(str);

        str = "SET SQL_MODE=\"this is a test \\\" ''' test2 \\' test3\"";
        assertStatementEquals(str);

        str = "SET SQL_MODE=\"this is a test \\\" ''' test2 \\' test3 \\\"";
        assertStatementEquals(str);
                             */
		// from xwiki
		String str = "INSERT INTO test VALUES ('test \\'','test')";
		assertStatementEquals(str);
	}
}
