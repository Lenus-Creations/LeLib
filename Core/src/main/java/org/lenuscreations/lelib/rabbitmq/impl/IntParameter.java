package org.lenuscreations.lelib.rabbitmq.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.lenuscreations.lelib.rabbitmq.MQParameter;

public class IntParameter implements MQParameter<Integer> {

    @Override
    public Integer parse(JsonElement obj) {
        if (obj.isJsonPrimitive()) {
            JsonPrimitive primitive = (JsonPrimitive) obj;
            if (primitive.isNumber()) return obj.getAsInt();
        }
        return null;
    }

}
