package org.lenuscreations.lelib.rabbitmq.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.lenuscreations.lelib.rabbitmq.MQParameter;

public class BooleanParameter implements MQParameter<Boolean> {

    @Override
    public Boolean parse(JsonElement obj) {
        if (obj.isJsonPrimitive()) {
            JsonPrimitive primitive = (JsonPrimitive) obj;
            if (primitive.isBoolean()) return primitive.getAsBoolean();
            if (primitive.isString()) {
                if (primitive.getAsString().equals("true")) return true;
                else if (primitive.getAsString().equals("false")) return false;
            }
        }

        return null;
    }
}
