package com.granveaud.mysql2h2converter.converter;

import com.granveaud.mysql2h2converter.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbUtils {
	final static private Logger LOGGER = LoggerFactory.getLogger(H2Converter.class);

    public static String unescapeDbObjectName(String str) {
        if (str.startsWith("\"") || str.startsWith("`") || str.startsWith("'")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

	public static Value transformStringValue(String str) {
		// we just have to transform \' to ''. STRINGDECODE will take care of the rest
		StringBuilder sb = new StringBuilder();
		int i = 1;
		while (i < str.length() - 1) {
			char c = str.charAt(i++);
			switch (c) {
				case '\\':
					if (i < str.length() - 1) {
						char nextChar = str.charAt(i++);
						switch (nextChar) {
							case '\'':
								sb.append("''");
								break;
							default:
								sb.append(c).append(nextChar);
								break;
						}
					} else {
						sb.append(c);
					}
					break;

				default:
					sb.append(c);
					break;
			}
		}

//		LOGGER.info("TRANSFORM " + str + " => STRINGDECODE('" + sb.toString() + "')");

		return new ExpressionValue("STRINGDECODE('" + sb.toString() + "')");
	}
}
