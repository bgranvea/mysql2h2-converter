package com.granveaud.mysql2h2converter.sql;

import com.google.common.base.Joiner;

import java.util.List;

/*
ALTER [IGNORE] TABLE tbl_name
    alter_specification [, alter_specification] ...

alter_specification:
    ADD [COLUMN] column_definition [FIRST | AFTER col_name ]
  | ADD [COLUMN] (column_definition,...)
  | ADD INDEX [index_name] [index_type] (index_col_name,...)
  | ADD [CONSTRAINT [symbol]]
        PRIMARY KEY [index_type] (index_col_name,...)
  | ADD [CONSTRAINT [symbol]]
        UNIQUE [index_name] [index_type] (index_col_name,...)
  | ADD [FULLTEXT|SPATIAL] [index_name] (index_col_name,...)
  | ADD [CONSTRAINT [symbol]]
        FOREIGN KEY [index_name] (index_col_name,...)
        [reference_definition]
  | ALTER [COLUMN] col_name {SET DEFAULT literal | DROP DEFAULT}
  | CHANGE [COLUMN] old_col_name column_definition
        [FIRST|AFTER col_name]
  | MODIFY [COLUMN] column_definition [FIRST | AFTER col_name]
  | DROP [COLUMN] col_name
  | DROP PRIMARY KEY
  | DROP INDEX index_name
  | DROP FOREIGN KEY fk_symbol
  | DISABLE KEYS
  | ENABLE KEYS
  | RENAME [TO] new_tbl_name
  | ORDER BY col_name
  | CONVERT TO CHARACTER SET charset_name [COLLATE collation_name]
  | [DEFAULT] CHARACTER SET charset_name [COLLATE collation_name]
  | DISCARD TABLESPACE
  | IMPORT TABLESPACE
  | table_options
 */

// TODO: implement complete ALTER TABLE statement
public class AlterTableStatement implements Statement {
    private boolean ignore;
    private String tableName;
	private List<AlterTableSpecification> specifications;

	public AlterTableStatement(boolean ignore, String tableName, List<AlterTableSpecification> specifications) {
		this.ignore = ignore;
		this.tableName = tableName;
		this.specifications = specifications;
	}

	@Override
    public String toString() {
        return "ALTER" + (ignore ? " IGNORE" : "") + " TABLE " + tableName + " " + Joiner.on(',').join(specifications);
    }
}
