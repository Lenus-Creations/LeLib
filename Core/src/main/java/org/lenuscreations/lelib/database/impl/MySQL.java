package org.lenuscreations.lelib.database.impl;

import org.lenuscreations.lelib.database.IDatabase;

import java.sql.ResultSet;

public class MySQL implements IDatabase<ResultSet, String> {

    @Override
    public String getDriverName() {
        return "MySQL Driver";
    }

    @Override
    public ResultSet get(String query) {
        return null;
    }

    @Override
    public void put(String value) {

    }

    @Override
    public String toString() {
        return getDriverName() + "@" + getClass().getName();
    }
}
