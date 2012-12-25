package com.granveaud.mysql2h2converter.sql;

public class CreateTableOption {
    private String name, value;

    public CreateTableOption(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name + (value != null ? "=" + value : "");
    }
}
