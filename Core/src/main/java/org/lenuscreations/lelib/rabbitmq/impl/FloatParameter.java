package org.lenuscreations.lelib.rabbitmq.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.lenuscreations.lelib.rabbitmq.MQParameter;

public class FloatParameter implements MQParameter<Float> {

    @Override
    public Float parse(JsonElement element) {
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = (JsonPrimitive) element;
            if (primitive.isNumber()) return primitive.getAsFloat();
        }

        return null;
    }
}
