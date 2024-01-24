package org.lenuscreations.lelib.redis;

import com.google.gson.JsonObject;

public interface Packet {

    String action();

    JsonObject toJson();

    default void handle(JsonObject response) {

    }

    default void handleError(JsonObject error) {
        if (error.has("message")) throw new RuntimeException("An error occurred whilst processing the packet. Status: " + error.get("status").getAsString() + ", reason: " + error.get("message").getAsString());
        else throw new RuntimeException("An unknown error has occurred when processing the packet. Status: " + error.get("status").getAsString());
    }

    default boolean async() {
        return false;
    }

}
