package org.lenuscreations.lelib.pterodactyl.client.server;

import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.lenuscreations.lelib.pterodactyl.ServerStatus;
import org.lenuscreations.lelib.pterodactyl.client.PteroClient;
import org.lenuscreations.lelib.rabbitmq.Status;
import org.lenuscreations.lelib.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ClientServer {

    private final PteroClient client;

    @Getter
    private String id;
    @Getter
    private String uuid;
    @Getter
    private String name;
    @Getter
    private String description;

    @Getter
    private ServerStatus status;

    public boolean update() {
        JsonObject json = HttpUtil.getPterodactylApi(client.getUrl() + "/api/client/servers/" + id, client.getToken());
        if (json.has("attributes")) {
            JsonObject attributes = json.getAsJsonObject("attributes");
            this.uuid = attributes.get("uuid").getAsString();
            this.name = attributes.get("name").getAsString();
            this.description = attributes.get("description").getAsString();
            this.status = ServerStatus.valueOf(attributes.get("status").getAsString().toUpperCase());
            return true;
        }

        return false;
    }

    public void stop(boolean kill) {
        JsonObject json = new JsonObject();
        json.addProperty("signal", kill ? "kill" : "stop");

        HttpUtil.postPterodactylApi(client.getUrl() + "/api/client/servers/" + id + "/power", client.getToken(), json);

        if (!kill) {
            while (this.status != ServerStatus.OFFLINE) {
                this.update();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    public void start() {
        JsonObject json = new JsonObject();
        json.addProperty("signal", "start");

        HttpUtil.postPterodactylApi(client.getUrl() + "/api/client/servers/" + id + "/power", client.getToken(), json);

        while (this.status != ServerStatus.ONLINE) {
            this.update();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void restart() {
        JsonObject json = new JsonObject();
        json.addProperty("signal", "restart");

        HttpUtil.postPterodactylApi(client.getUrl() + "/api/client/servers/" + id + "/power", client.getToken(), json);

        while (this.status != ServerStatus.ONLINE) {
            this.update();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void kill() {
        this.stop(true);
    }

    public void sendCommand(String command) {
        JsonObject json = new JsonObject();
        json.addProperty("command", command);

        HttpUtil.postPterodactylApi(client.getUrl() + "/api/client/servers/" + id + "/command", client.getToken(), json);
    }

}
