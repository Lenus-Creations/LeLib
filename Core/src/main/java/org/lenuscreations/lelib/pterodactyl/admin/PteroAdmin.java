package org.lenuscreations.lelib.pterodactyl.admin;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import org.lenuscreations.lelib.LeLib;
import org.lenuscreations.lelib.pterodactyl.admin.server.PteroServer;
import org.lenuscreations.lelib.pterodactyl.client.PteroClient;
import org.lenuscreations.lelib.utils.HttpUtil;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class PteroAdmin {

    private final String url;
    private final String token;

    public PteroServer getServer(String id) {
        JsonObject object = HttpUtil.getPterodactylApi(url + "/api/application/servers/" + id, token);

        return LeLib.GSON.fromJson(object, PteroServer.class);
    }

    public PteroServer getServerByExternalId(String externalId) {
        JsonObject object = HttpUtil.getPterodactylApi(url + "/api/application/servers/external/" + externalId, token);

        return LeLib.GSON.fromJson(object, PteroServer.class);
    }

    public PteroServer createServer(
            PteroClient user,
            String name,
            String description,
            int memory,
            int swap,
            int disk,
            int cpu,
            int database,
            int backups
    ) {
        return null;
    }

    public List<PteroServer> getServers() {
        JsonObject object = HttpUtil.getPterodactylApi(url + "/api/application/servers", token);
        JsonArray array = object.getAsJsonArray("data");

        List<PteroServer> servers = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject server = array.get(i).getAsJsonObject();

            servers.add(LeLib.GSON.fromJson(server.get("attributes").getAsJsonObject(), PteroServer.class));
        }

        return servers;
    }

}
