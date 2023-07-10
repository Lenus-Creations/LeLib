package org.lenuscreations.lelib.database.impl;

import com.google.gson.JsonObject;
import org.lenuscreations.lelib.database.IDatabase;


public class MongoDB implements IDatabase<JsonObject, JsonObject> {

    @Override
    public String getDriverName() {
        return "MongoDB Driver";
    }

    @Override
    public JsonObject get(String query) {
        return null;
    }

    @Override
    public void put(JsonObject value) {

    }

    @Override
    public String toString() {
        return getDriverName() + "@" + getClass().getName();
    }
}
