package org.lenuscreations.lelib.database;

import org.lenuscreations.lelib.database.mongodb.MongoDBDatabaseBuilder;
import org.lenuscreations.lelib.database.mysql.MySQLDatabaseBuilder;

public class DatabaseBuilder {

    public static MySQLDatabaseBuilder mysql(String host, String database, String username, String password) {
        return new MySQLDatabaseBuilder(host, database, username, password);
    }

    public static MongoDBDatabaseBuilder mongodb(String address, String database, String username, String password) {
        return new MongoDBDatabaseBuilder(address, database, username, password);
    }

    public static MongoDBDatabaseBuilder mongodb(String address, int port, String database, String username, String password) {
        return new MongoDBDatabaseBuilder(address, port, database, username, password);
    }

}
