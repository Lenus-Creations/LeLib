package org.lenuscreations.lelib.adapters;

import com.google.gson.*;
import org.lenuscreations.lelib.pterodactyl.admin.location.PteroLocation;
import org.lenuscreations.lelib.pterodactyl.admin.node.PteroNode;

import java.lang.reflect.Type;

public class PteroNodeAdapter implements JsonDeserializer<PteroNode>, JsonSerializer<PteroNode> {

    @Override
    public PteroNode deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        int id = jsonElement.getAsJsonObject().get("id").getAsInt();
        String name = jsonElement.getAsJsonObject().get("name").getAsString();
        String uuid = jsonElement.getAsJsonObject().get("uuid").getAsString();
        String description = jsonElement.getAsJsonObject().get("description").getAsString();
        PteroLocation location = jsonDeserializationContext.deserialize(jsonElement.getAsJsonObject().get("location"), PteroLocation.class);
        boolean isPublic = jsonElement.getAsJsonObject().get("public").getAsBoolean();
        boolean isBehindProxy = jsonElement.getAsJsonObject().get("behind_proxy").getAsBoolean();
        boolean isMaintenanceMode = jsonElement.getAsJsonObject().get("maintenance_mode").getAsBoolean();
        String fqdn = jsonElement.getAsJsonObject().get("fqdn").getAsString();
        String scheme = jsonElement.getAsJsonObject().get("scheme").getAsString();
        int memory = jsonElement.getAsJsonObject().get("memory").getAsInt();
        int memoryOverallocate = jsonElement.getAsJsonObject().get("memory_overallocate").getAsInt();
        int disk = jsonElement.getAsJsonObject().get("disk").getAsInt();
        int diskOverallocate = jsonElement.getAsJsonObject().get("disk_overallocate").getAsInt();
        int uploadSize = jsonElement.getAsJsonObject().get("upload_size").getAsInt();
        int daemonListen = jsonElement.getAsJsonObject().get("daemon_listen").getAsInt();
        int daemonSftp = jsonElement.getAsJsonObject().get("daemon_sftp").getAsInt();
        String daemonBase = jsonElement.getAsJsonObject().get("daemon_base").getAsString();
        String createdAt = jsonElement.getAsJsonObject().get("created_at").getAsString();
        String updatedAt = jsonElement.getAsJsonObject().get("updated_at").getAsString();

        return new PteroNode(
                id,
                name,
                uuid,
                description,
                location,
                isPublic,
                isBehindProxy,
                isMaintenanceMode,
                fqdn,
                scheme,
                memory,
                memoryOverallocate,
                disk,
                diskOverallocate,
                uploadSize,
                daemonListen,
                daemonSftp,
                daemonBase,
                createdAt,
                updatedAt
        );
    }

    @Override
    public JsonElement serialize(PteroNode pteroNode, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("id", pteroNode.getId());
        object.addProperty("name", pteroNode.getName());
        object.addProperty("uuid", pteroNode.getUuid());
        object.addProperty("description", pteroNode.getDescription());
        object.add("location", jsonSerializationContext.serialize(pteroNode.getLocation()));
        object.addProperty("public", pteroNode.isPublicNode());
        object.addProperty("behind_proxy", pteroNode.isBehindProxy());
        object.addProperty("maintenance_mode", pteroNode.isMaintenanceMode());
        object.addProperty("fqdn", pteroNode.getFqdn());
        object.addProperty("scheme", pteroNode.getScheme());
        object.addProperty("memory", pteroNode.getMemory());
        object.addProperty("memory_overallocate", pteroNode.getMemoryOverallocate());
        object.addProperty("disk", pteroNode.getDisk());
        object.addProperty("disk_overallocate", pteroNode.getDiskOverallocate());
        object.addProperty("upload_size", pteroNode.getUploadSize());
        object.addProperty("daemon_listen", pteroNode.getDaemonListen());
        object.addProperty("daemon_sftp", pteroNode.getDaemonSftp());
        object.addProperty("daemon_base", pteroNode.getDaemonBase());
        object.addProperty("created_at", pteroNode.getCreatedAt());
        object.addProperty("updated_at", pteroNode.getUpdatedAt());
        return object;
    }

}
