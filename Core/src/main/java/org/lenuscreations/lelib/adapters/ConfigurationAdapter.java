package org.lenuscreations.lelib.adapters;

import com.google.gson.*;
import org.checkerframework.checker.units.qual.C;
import org.lenuscreations.lelib.file.Configuration;
import org.lenuscreations.lelib.file.value.impl.ConfigurationChildren;
import org.lenuscreations.lelib.file.value.impl.StringValue;

import java.lang.reflect.Type;

public class ConfigurationAdapter implements JsonSerializer<Configuration> {

    @Override
    public JsonElement serialize(Configuration configuration, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject element = new JsonObject();
        JsonElement value = null;
        if (configuration.getValue().getClass().equals(StringValue.class)) {
            value = new JsonPrimitive((String) configuration.getValue().getValue());
        } else {
            value = jsonSerializationContext.serialize(configuration.getValue().getValue());
        }

        element.add(configuration.getName(), value);

        return element;
    }

}
