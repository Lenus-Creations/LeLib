package org.lenuscreations.lelib.bukkit.disguise;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.grcq.v1_8_r3.V1_8_R3;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.lenuscreations.lelib.bukkit.disguise.event.DisguiseChangeEvent;
import org.lenuscreations.lelib.bukkit.disguise.event.DisguiseClearEvent;
import org.lenuscreations.lelib.bukkit.utils.Util;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DisguiseHandler {

    private final List<Disguise> disguises = new ArrayList<>();

    @SneakyThrows
    public void disguise(Disguise disguise) {
        if (disguise == null) throw new RuntimeException("Disguise data cannot be null.");
        if (isDisguised(disguise.getActualUUID())) disguise.setActualName(disguise.getActualName());

        JsonObject object = get(disguise.getUuid());

        switch (Util.getNMSVersion()) {
            case "1_8_R3":
                V1_8_R3.getDisguiseHandler().disguise(Bukkit.getPlayer(disguise.getActualUUID()), object);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented for this server version.");
        }

        this.disguises.add(disguise);
        Bukkit.getServer().getPluginManager().callEvent(new DisguiseChangeEvent(disguise));
    }

    @SneakyThrows
    public void clear(Player player) {
        if (!isDisguised(player.getUniqueId())) return;

        Disguise disguise = this.disguises.stream().filter(d -> d.getActualUUID().equals(player.getUniqueId())).findFirst().orElse(null);
        if (disguise == null) throw new RuntimeException("An uncaught exception was discovered while attempting to clear a disguise.");

        this.disguises.remove(disguise);

        Disguise clearDisguise = new Disguise(disguise.getActualUUID(), disguise.getActualName(), disguise.getActualUUID(), disguise.getActualName());
        this.disguise(disguise);

        Bukkit.getServer().getPluginManager().callEvent(new DisguiseClearEvent(disguise));

        this.disguises.remove(clearDisguise);
    }

    public boolean isDisguised(UUID actualUUID) {
        return this.disguises.stream().anyMatch(d -> d.getActualUUID().equals(actualUUID));
    }

    public boolean isDisguised(Player player) {
        return this.isDisguised(player.getUniqueId());
    }

    private JsonObject get(UUID uuid) throws IOException {
        String uuidString = uuid.toString().replace("-", "");
        InputStream is = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidString + "?unsigned=false").openStream();

        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);

            return (JsonObject) new JsonParser().parse(jsonText);
        } finally {
            is.close();
        }

    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

}
