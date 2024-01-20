package org.lenuscreations.lelib.bukkit.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@UtilityClass
public class MojangUtils {

    @SneakyThrows
    public static JsonObject getMojangProfile(UUID uuid, boolean unsigned) {
        URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", "") + "?unsigned=" + unsigned);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        InputStreamReader isr = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(isr);

        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }

        return (JsonObject) new JsonParser().parse(content.toString());
    }

    @SneakyThrows
    public static JsonObject getMojangProfile(String name, boolean unsigned) {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        InputStreamReader isr = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(isr);

        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }

        JsonObject object = (JsonObject) new JsonParser().parse(content.toString());
        if (object.has("id")) {
            return getMojangProfile(UUID.fromString(object.get("id").getAsString()), unsigned);
        }

        return null;
    }

    public static String getNameFromUUID(UUID uuid) {
        JsonObject object = getMojangProfile(uuid, true);
        return object.get("name").getAsString();
    }

    public static UUID getUUIDFromName(String name) {
        JsonObject object = getMojangProfile(name, true);
        return UUID.fromString(object.get("id").getAsString());
    }

    public static List<Map<String, String>> getProfileProperties(UUID uuid, boolean unsigned) {
        JsonObject object = getMojangProfile(uuid, unsigned);

        List<Map<String, String>> properties = new ArrayList<>();
        object.get("properties").getAsJsonArray().forEach(property -> {
            Map<String, String> map = new HashMap<>();
            map.put("name", property.getAsJsonObject().get("name").getAsString());
            map.put("value", property.getAsJsonObject().get("value").getAsString());
            if (property.getAsJsonObject().has("signature")) {
                map.put("signature", property.getAsJsonObject().get("signature").getAsString());
            }
            properties.add(map);
        });

        return properties;
    }

    public static List<Map<String, String>> getProfileProperties(UUID uuid) {
        return getProfileProperties(uuid, true);
    }

    public static List<Map<String, String>> getProfileProperties(String name, boolean unsigned) {
        JsonObject object = getMojangProfile(name, unsigned);

        List<Map<String, String>> properties = new ArrayList<>();
        object.get("properties").getAsJsonArray().forEach(property -> {
            Map<String, String> map = new HashMap<>();
            map.put("name", property.getAsJsonObject().get("name").getAsString());
            map.put("value", property.getAsJsonObject().get("value").getAsString());
            if (property.getAsJsonObject().has("signature")) {
                map.put("signature", property.getAsJsonObject().get("signature").getAsString());
            }
            properties.add(map);
        });

        return properties;
    }

    public static List<Map<String, String>> getProfileProperties(String name) {
        return getProfileProperties(name, true);
    }

}
