package com.granveaud.mysql2h2converter.sql;

import com.google.common.base.Joiner;

import java.util.List;

/*
INSERT [LOW_PRIORITY | DELAYED | HIGH_PRIORITY] [IGNORE]
    [INTO] tbl_name [(col_name,...)]
    {VALUES | VALUE} ({expr | DEFAULT},...),(...),...
    [ ON DUPLICATE KEY UPDATE
      col_name=expr
        [, col_name=expr] ... ]
Or:

INSERT [LOW_PRIORITY | DELAYED | HIGH_PRIORITY] [IGNORE]
    [INTO] tbl_name
    SET col_name={expr | DEFAULT}, ...
    [ ON DUPLICATE KEY UPDATE
      col_name=expr
        [, col_name=expr] ... ]
Or:

INSERT [LOW_PRIORITY | HIGH_PRIORITY] [IGNORE]
    [INTO] tbl_name [(col_name,...)]
    SELECT ...
    [ ON DUPLICATE KEY UPDATE
      col_name=expr
        [, col_name=expr] ... ]*/

public class InsertStatement implements Statement {
    private boolean lowPriority, highPriority, delayed, ignore, into;
	private String tableName;
	private List<String> columnNames;
	private List<ValueList> values;
// private SelectStatement subSelect;
	private List<Assignment> assignments;
	private List<Assignment> onDuplicateKeyUpdateAssignments;

	public InsertStatement(boolean lowPriority, boolean highPriority, boolean delayed, boolean ignore, boolean into, String tableName, List<String> columnNames, List<ValueList> values, List<Assignment> assignments, List<Assignment> onDuplicateKeyUpdateAssignments) {
		this.lowPriority = lowPriority;
		this.highPriority = highPriority;
		this.delayed = delayed;
		this.ignore = ignore;
		this.into = into;
		this.tableName = tableName;
		this.columnNames = columnNames;
		this.values = values;
		this.assignments = assignments;
		this.onDuplicateKeyUpdateAssignments = onDuplicateKeyUpdateAssignments;
	}

	@Override

    public String toString() {
        return "INSERT" + (lowPriority ? " LOW_PRIORITY" : "") + (highPriority ? " HIGH_PRIORITY" : "") + (delayed ? " DELAYED" : "") + (ignore ? " IGNORE" : "") + (into ? " INTO" : "") +
				" " + tableName +
				(columnNames != null ? " (" + Joiner.on(',').join(columnNames) + ")" : "") +
				(values != null ? " VALUES " + Joiner.on(',').join(values) : "") +
				(assignments != null ? " SET " + Joiner.on(',').join(assignments) : "") +
				(onDuplicateKeyUpdateAssignments != null ? " ON DUPLICATE KEY UPDATE " + Joiner.on(',').join(onDuplicateKeyUpdateAssignments) : "");
    }
}
