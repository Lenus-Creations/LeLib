package org.lenuscreations.lelib.adapters;

import com.google.gson.*;
import org.lenuscreations.lelib.pterodactyl.admin.server.PteroServer;

import java.lang.reflect.Type;

public class PteroServerAdapter implements JsonSerializer<PteroServer>, JsonDeserializer<PteroServer> {

    @Override
    public PteroServer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        PteroServer pteroServer = new PteroServer(
                null,
                null,
                null,
                object.get("id").getAsInt(),
                object.get("uuid").getAsString(),
                object.get("identifier").getAsString(),
                object.get("name").getAsString(),
                object.get("description").getAsString(),
                jsonDeserializationContext.deserialize(object.get("status"), ServerStatusAdapter.class),
                object.get("suspended").getAsBoolean(),
                object.getAsJsonObject("limits").get("memory").getAsInt(),
                object.getAsJsonObject("limits").get("swap").getAsInt(),
                object.getAsJsonObject("limits").get("disk").getAsInt(),
                object.getAsJsonObject("limits").get("io").getAsInt(),
                object.getAsJsonObject("limits").get("cpu").getAsInt(),
                object.getAsJsonObject("feature_limits").get("databases").getAsInt(),
                object.getAsJsonObject("feature_limits").get("allocations").getAsInt(),
                object.getAsJsonObject("feature_limits").get("backups").getAsInt(),
                object.get("created_at").getAsString(),
                object.get("updated_at").getAsString()
        );
        return pteroServer;
    }

    @Override
    public JsonElement serialize(PteroServer pteroServer, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("id", pteroServer.getId());
        object.addProperty("uuid", pteroServer.getUuid());
        object.addProperty("identifier", pteroServer.getIdentifier());
        object.addProperty("name", pteroServer.getName());
        object.addProperty("description", pteroServer.getDescription());
        object.add("status", jsonSerializationContext.serialize(pteroServer.getStatus()));
        object.addProperty("suspended", pteroServer.isSuspended());

        JsonObject limits = new JsonObject();
        limits.addProperty("memory", pteroServer.getMemoryLimit());
        limits.addProperty("swap", pteroServer.getSwapLimit());
        limits.addProperty("disk", pteroServer.getDiskLimit());
        limits.addProperty("io", pteroServer.getIoLimit());
        limits.addProperty("cpu", pteroServer.getCpuLimit());
        object.add("limits", limits);

        JsonObject featureLimits = new JsonObject();
        featureLimits.addProperty("databases", pteroServer.getDatabaseLimit());
        featureLimits.addProperty("allocations", pteroServer.getAllocationLimit());
        featureLimits.addProperty("backups", pteroServer.getBackupsLimit());
        object.add("feature_limits", featureLimits);
        
        object.addProperty("createdAt", pteroServer.getCreatedAt());
        object.addProperty("updatedAt", pteroServer.getUpdatedAt());
        return object;
    }
}
