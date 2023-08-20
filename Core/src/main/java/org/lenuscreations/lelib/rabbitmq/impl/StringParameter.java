package org.lenuscreations.lelib.rabbitmq.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.lenuscreations.lelib.rabbitmq.MQParameter;

public class StringParameter implements MQParameter<String> {

    @Override
    public String parse(JsonElement element) {
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = (JsonPrimitive) element;
            if (primitive.isString()) return element.getAsString();
            else return primitive.toString();
        }

        return null;
    }

    @Override
    public int priority() {
        return Integer.MIN_VALUE;
    }
}
