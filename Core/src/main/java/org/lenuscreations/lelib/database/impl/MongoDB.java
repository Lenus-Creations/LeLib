package org.lenuscreations.lelib.database.impl;

import com.google.gson.JsonObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.lenuscreations.lelib.LeLib;
import org.lenuscreations.lelib.database.Credentials;
import org.lenuscreations.lelib.database.IDatabase;


public class MongoDB implements IDatabase<JsonObject, JsonObject> {

    private Credentials credentials;

    private MongoClient client;
    private MongoDatabase database;

    @Override
    public String getDriverName() {
        return "MongoDB Driver";
    }

    @Override
    public JsonObject get(String var1, String query) {
        return null;
    }

    @Override
    public void put(String var1, JsonObject value, String where) {
        MongoCollection<Document> collection = database.getCollection(var1);
        if (where != null) {
            String[] split = where.split("=");
            Document old = collection.find(Filters.eq(split[0], split[1])).first();
            if (old == null) {
                collection.insertOne(Document.parse(value.toString()));
                return;
            }

            collection.replaceOne(old, Document.parse(value.toString()));
            return;
        }

        collection.insertOne(Document.parse(value.toString()));
    }

    @Override
    public void remove(String var1, String var2) {

    }

    @Override
    public void setCredentials(Credentials credentials) {
        if (this.client != null) {
            this.client.close();
        }

        this.credentials = credentials;
        this.connect();
    }

    private void connect() {
        this.client = MongoClients.create("mongodb://" + credentials.getUsername() + ":" + credentials.getPassword() + "@" + credentials.getAddress() + "/" + credentials.getDatabase() + "?w=majority");
        this.database = this.client.getDatabase(credentials.getDatabase());
    }

    @Override
    public String toString() {
        return getDriverName() + "@" + getClass().getName();
    }
}
