package org.lenuscreations.lelib.database;

import org.lenuscreations.lelib.database.mysql.MySQLDatabaseBuilder;

public class DatabaseBuilder {

    public static MySQLDatabaseBuilder mysql(String host, String database, String username, String password) {
        return new MySQLDatabaseBuilder(host, database, username, password);
    }

}
