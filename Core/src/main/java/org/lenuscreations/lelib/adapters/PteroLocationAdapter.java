package org.lenuscreations.lelib.adapters;

import com.google.gson.*;
import org.lenuscreations.lelib.pterodactyl.admin.location.PteroLocation;

import java.lang.reflect.Type;

public class PteroLocationAdapter implements JsonDeserializer<PteroLocation>, JsonSerializer<PteroLocation> {

    @Override
    public PteroLocation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        int id = jsonElement.getAsJsonObject().get("id").getAsInt();
        String shortCode = jsonElement.getAsJsonObject().get("short_code").getAsString();
        String longCode = jsonElement.getAsJsonObject().get("long_code").getAsString();
        String description = jsonElement.getAsJsonObject().get("description").getAsString();
        String createdAt = jsonElement.getAsJsonObject().get("created_at").getAsString();
        String updatedAt = jsonElement.getAsJsonObject().get("updated_at").getAsString();

        return new PteroLocation(
                id,
                shortCode,
                longCode,
                description,
                createdAt,
                updatedAt
        );
    }

    @Override
    public JsonElement serialize(PteroLocation pteroLocation, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("id", pteroLocation.getId());
        object.addProperty("short_code", pteroLocation.getShortCode());
        object.addProperty("long_code", pteroLocation.getLongCode());
        object.addProperty("description", pteroLocation.getDescription());
        object.addProperty("created_at", pteroLocation.getCreatedAt());
        object.addProperty("updated_at", pteroLocation.getUpdatedAt());
        return object;
    }
}
