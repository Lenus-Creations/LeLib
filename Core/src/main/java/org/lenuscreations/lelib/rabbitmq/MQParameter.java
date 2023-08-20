package org.lenuscreations.lelib.rabbitmq;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public interface MQParameter<T> {

    T parse(JsonElement element);

    default int priority() {
        return -1;
    }

}
