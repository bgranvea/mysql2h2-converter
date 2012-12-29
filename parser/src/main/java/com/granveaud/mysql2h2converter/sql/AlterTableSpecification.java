package com.granveaud.mysql2h2converter.sql;

import java.util.List;

/*
alter_specification:
		| ADD INDEX [index_name] [index_type] (index_col_name,...)
		| ADD [CONSTRAINT [symbol]]
		PRIMARY KEY [index_type] (index_col_name,...)
		| ADD [CONSTRAINT [symbol]]
		UNIQUE [index_name] [index_type] (index_col_name,...)
		| ADD [FULLTEXT|SPATIAL] [index_name] (index_col_name,...)
		| ADD [CONSTRAINT [symbol]]
		FOREIGN KEY [index_name] (index_col_name,...)
		[reference_definition]

 		TODO:
		ADD [COLUMN] column_definition [FIRST | AFTER col_name ]
		| ADD [COLUMN] (column_definition,...)

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
/*
ColumnConstraint:

  | [CONSTRAINT [symbol]] PRIMARY KEY [index_type] (index_col_name,...)
  | KEY [index_name] [index_type] (index_col_name,...)
  | INDEX [index_name] [index_type] (index_col_name,...)
  | [CONSTRAINT [symbol]] UNIQUE [INDEX]
        [index_name] [index_type] (index_col_name,...)
  | [FULLTEXT|SPATIAL] [INDEX] [index_name] (index_col_name,...)
  | [CONSTRAINT [symbol]] FOREIGN KEY
        [index_name] (index_col_name,...) [reference_definition]
  | CHECK (expr)

 */
// TODO: support remaining ALTER TABLE commands
public class AlterTableSpecification {
	private String operation; // ADD
	private ColumnConstraint constraint;

	public AlterTableSpecification(String operation, ColumnConstraint constraint) {
		this.operation = operation;
		this.constraint = constraint;
	}

	@Override
	public String toString() {
		return operation +
				(constraint != null ? " " + constraint : "");
	}
}
