package org.lenuscreations.lelib.database.mysql;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class MySQLTable {

    private final String name;
    private final List<MySQLColumn> columns = new ArrayList<>();

    public MySQLTable addColumn(MySQLColumn column) {
        columns.add(column);
        return this;
    }

}
