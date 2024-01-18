package org.lenuscreations.lelib.database.mysql;

import org.jetbrains.annotations.NotNull;
import org.lenuscreations.lelib.database.MySQLDatabase;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class MySQLDatabaseBuilder {

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    private final List<MySQLTable> tables = new ArrayList<>();

    public MySQLDatabaseBuilder(String host, String database, String username, String password) {
        this(host, 3306, database, username, password);
    }

    public MySQLDatabaseBuilder(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public MySQLDatabaseBuilder addTable(MySQLTable table) {
        tables.add(table);
        return this;
    }

    public MySQLDatabase build() {
        MySQLDatabase db = new MySQLDatabase(host, port, database, username, password);
        for (MySQLTable table : tables) {
            try (PreparedStatement stmt = db.statement("SHOW TABLES LIKE '" + table.getName() + "'")) {
                if (!stmt.executeQuery().next()) {
                    StringBuilder query = getQuery(table);
                    db.execute(query.toString());
                } else {
                    for (MySQLColumn column : table.getColumns()) {
                        try (PreparedStatement stmt2 = db.statement("SHOW COLUMNS FROM " + table.getName() + " LIKE '" + column.getName() + "'")) {
                            if (stmt2.executeQuery().next()) continue;

                            db.execute("ALTER TABLE " + table.getName() + " ADD " + column.getSQL());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return db;
    }

    @NotNull
    private static StringBuilder getQuery(MySQLTable table) {
        StringBuilder query = new StringBuilder("CREATE TABLE " + table.getName() + " (");

        int i = 0;
        for (MySQLColumn column : table.getColumns()) {
            query.append(column.getSQL());
            if (i < table.getColumns().size() - 1) {
                query.append(", ");
            }
            i++;
        }

        query.append(")");
        return query;
    }
}
