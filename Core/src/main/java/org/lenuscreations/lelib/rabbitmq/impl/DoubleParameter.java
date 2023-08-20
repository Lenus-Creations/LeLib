package org.lenuscreations.lelib.rabbitmq.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.lenuscreations.lelib.rabbitmq.MQParameter;

public class DoubleParameter implements MQParameter<Double> {

    @Override
    public Double parse(JsonElement obj) {
        if (obj.isJsonPrimitive()) {
            JsonPrimitive primitive = (JsonPrimitive) obj;

            if (primitive.isNumber()) return obj.getAsDouble();
        }
        return null;
    }
}
