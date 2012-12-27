package com.granveaud.mysql2h2converter.converter;

import com.granveaud.mysql2h2converter.parser.ParseException;
import com.granveaud.mysql2h2converter.parser.SQLParserManager;
import com.granveaud.mysql2h2converter.sql.Statement;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConverterTest {

    final static private Logger LOGGER = LoggerFactory.getLogger(ConverterTest.class);

	@BeforeClass
	public static void initDriver() {
		org.h2.Driver.load();
	}

	@AfterClass
	public static void cleanupDriver() {
		org.h2.Driver.unload();
	}

	private Connection connection;

	@Before
	public void initConnection() throws SQLException {
		connection = DriverManager.getConnection("jdbc:h2:mem:test;MODE=MySQL");
	}

	@After
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			LOGGER.warn("Exception while closing connection", e);
		}
	}

	private void executeUpdate(String sql) throws SQLException {
		try {
			java.sql.Statement sqlStat = connection.createStatement();
			sqlStat.executeUpdate(sql.toString());
			sqlStat.close();
		} catch (SQLException e) {
			LOGGER.warn("Error sql=" + sql, e);
			throw e;
		}
	}

	private List<Map<String, Object>> executeSelect(String sql) throws SQLException {
		List<Map<String, Object>> result = null;

		java.sql.Statement sqlStat = connection.createStatement();
		if (sqlStat.execute(sql.toString())) {
			ResultSet rs = sqlStat.getResultSet();
			ResultSetMetaData metaData = rs.getMetaData();

			result = new ArrayList<Map<String, Object>>();
			while (rs.next()) {
				Map<String, Object> record = new HashMap<String, Object>();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					record.put(metaData.getColumnName(i), rs.getObject(i));
				}
				result.add(record);
			}
		}
		sqlStat.close();

		return result;
	}

	private void executeScript(Reader reader) throws SQLException, ParseException {
		List<Statement> statements = SQLParserManager.parse(reader);

		// conversion
		List<Statement> h2Statements = new H2Converter().convertScript(statements);

		// load in H2
		for (Statement stat : h2Statements) {
			executeUpdate(stat.toString());
		}
	}

	@Test
	public void testStringEscaping() throws Exception {
		String strValue1 = "string with escaping '' \\' \\\\ \\n \\t";
		String strResult1 = "string with escaping ' ' \\ \n \t";

		String strValue2 = "string with no escaping";
		String strResult2 = strValue2;

		String sql = "CREATE TABLE test (str1 VARCHAR(255), str2 VARCHAR(255)); \n" +
				"INSERT INTO test VALUES ('" + strValue1 + "','" + strValue2 + "');\n";

		executeScript(new StringReader(sql));

		// check inserted string
		List<Map<String, Object>> result = executeSelect("SELECT * FROM test");

		assertTrue(result.size() == 1);
		assertEquals(result.get(0).get("STR1"), strResult1);
		assertEquals(result.get(0).get("STR2"), strResult2);
	}

	@Test
	public void testScriptConversion() throws Exception {
		for (String s : new String[] {
				"wordpress.sql", "drupal.sql", "xwiki.sql", "xwiki-no-foreign-key-checks.sql", "xwiki-sqlyog.sql"
		}) {
			LOGGER.info("Executing script " + s);
			executeScript(new InputStreamReader(getClass().getResourceAsStream("/scripts/" + s)));

			LOGGER.info("Done");
		}
    }
}
