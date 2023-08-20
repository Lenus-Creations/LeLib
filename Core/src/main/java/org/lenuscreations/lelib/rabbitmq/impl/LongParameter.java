package org.lenuscreations.lelib.rabbitmq.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.lenuscreations.lelib.rabbitmq.MQParameter;

public class LongParameter implements MQParameter<Long> {

    @Override
    public Long parse(JsonElement element) {
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = (JsonPrimitive) element;
            if (primitive.isNumber()) return element.getAsLong();
        }

        return null;
    }

    @Override
    public int priority() {
        return -2;
    }
}
