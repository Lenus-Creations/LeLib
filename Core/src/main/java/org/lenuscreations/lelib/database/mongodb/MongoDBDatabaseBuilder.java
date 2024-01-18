package org.lenuscreations.lelib.database.mongodb;

import org.lenuscreations.lelib.database.MongoDBDatabase;

public class MongoDBDatabaseBuilder {

    private final String address;
    private int port;
    private final String database;
    private final String username;
    private final String password;

    public MongoDBDatabaseBuilder(String address, String database, String username, String password) {
        this.address = address;
        this.port = -1;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public MongoDBDatabaseBuilder(String address, int port, String database, String username, String password) {
        this.address = address;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public MongoDBDatabase build() {
        MongoDBDatabase db = new MongoDBDatabase(address, port, database, username, password);


        return db;
    }

}
