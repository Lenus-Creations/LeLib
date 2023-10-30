package org.lenuscreations.lelib.adapters;

import com.google.gson.*;
import org.lenuscreations.lelib.pterodactyl.ServerStatus;

import java.lang.reflect.Type;

public class ServerStatusAdapter implements JsonDeserializer<ServerStatus>, JsonSerializer<ServerStatus> {

    @Override
    public ServerStatus deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return ServerStatus.valueOf(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(ServerStatus serverStatus, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(serverStatus.name());
    }

}
