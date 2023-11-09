package org.lenuscreations.lelib.rabbitmq;

import com.google.gson.JsonObject;

public interface Packet {

    String getAction();

    JsonObject getMessage();

    default void handle(JsonObject response) {

    }

    default void handleError(JsonObject error) {

    }

    default boolean async() {
        return false;
    }

}
