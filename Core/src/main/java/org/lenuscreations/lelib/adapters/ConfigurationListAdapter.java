package org.lenuscreations.lelib.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.lenuscreations.lelib.file.Configuration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationListAdapter implements JsonSerializer<List<Configuration>> {

    @Override
    public JsonElement serialize(List<Configuration> configurations, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        for (Configuration configuration : configurations) {
            object.add(configuration.getName(), jsonSerializationContext.serialize(configuration.getValue()));
        }

        // what? it wont be executed?
        System.out.println(object + " AAAA");

        return object;
    }
}
