package org.lenuscreations.lelib.database;

import com.google.gson.JsonObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoDBDatabase {

    private final MongoClient client;
    private final MongoDatabase database;

    private String currentCollection;

    public MongoDBDatabase(String address, int port, String database, String username, String password) {
        this.client = MongoClients.create(
                "mongodb://" + username + ":" + password + "@" + address + ":" + port + "/" + database + "?retryWrites=true&w=majority"
        );
        this.database = client.getDatabase(database);
    }

    public MongoDBDatabase(String host, String database, String username, String password) {
        this.client = MongoClients.create(
                "mongodb+srv://" + username + ":" + password + "@" + host + "/" + database + "?retryWrites=true&w=majority"
        );
        this.database = client.getDatabase(database);
    }

    public MongoDBDatabase inCollection(String collection) {
        this.currentCollection = collection;
        return this;
    }

    public MongoDBDatabase addDocument(JsonObject object) {
        return addDocument(object, null);
    }

    public MongoDBDatabase addDocument(JsonObject object, Bson filter) {
        if (currentCollection == null) throw new IllegalStateException("No collection selected!");

        if (filter == null)
            database.getCollection(this.currentCollection)
                    .insertOne(Document.parse(object.toString()));
        else
            database.getCollection(this.currentCollection)
                .replaceOne(filter, Document.parse(object.toString()));
        return this;
    }

}
