package com.granveaud.mysql2h2converter.sql;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

import java.util.List;

public class CreateTableDefinition {
    private List<ColumnDefinition> columnDefinitions;
    private List<ColumnConstraint> constraints;

    public CreateTableDefinition(List<ColumnDefinition> columnDefinitions, List<ColumnConstraint> constraints) {
        this.columnDefinitions = columnDefinitions;
        this.constraints = constraints;
    }

    @Override
    public String toString() {
        return Joiner.on(',').join(Iterables.concat(columnDefinitions, constraints));
    }
}
