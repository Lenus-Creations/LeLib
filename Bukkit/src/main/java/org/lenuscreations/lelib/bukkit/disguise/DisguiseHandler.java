package org.lenuscreations.lelib.bukkit.disguise;

import com.google.gson.JsonObject;
import dev.grcq.v1_12_r1.V1_12_R1;
import dev.grcq.v1_8_r3.V1_8_R3;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.lenuscreations.lelib.bukkit.disguise.event.DisguiseChangeEvent;
import org.lenuscreations.lelib.bukkit.disguise.event.DisguiseClearEvent;
import org.lenuscreations.lelib.bukkit.utils.PlayerUtils;
import org.lenuscreations.lelib.bukkit.utils.Util;

import java.util.*;

public class DisguiseHandler {

    private final List<Disguise> disguises = new ArrayList<>();

    @SneakyThrows
    public boolean disguise(Disguise disguise) {
        if (disguise == null) throw new RuntimeException("Disguise data cannot be null.");
        if (isDisguised(disguise.getActualUUID())) disguise.setActualName(disguise.getActualName());

        JsonObject object = PlayerUtils.getProfileSigned(disguise.getUuid());
        if (object == null) return false;

        Player player = Bukkit.getPlayer(disguise.getActualUUID());

        switch (Util.getNMSVersion()) {
            case "v1_8_R3":
                V1_8_R3.getDisguiseHandler().disguise(Bukkit.getPlayer(disguise.getActualUUID()), object);
                break;
            case "v1_12_R1":
                V1_12_R1.getDisguiseHandler().disguise(Bukkit.getPlayer(disguise.getActualUUID()), object);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented for this server version.");
        }

        this.disguises.add(disguise);
        player.setDisplayName(disguise.getUsername());
        player.setCustomName(disguise.getUsername());
        player.setCustomNameVisible(true);
        Bukkit.getServer().getPluginManager().callEvent(new DisguiseChangeEvent(disguise));
        return true;
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
        player.setDisplayName(disguise.getActualName());
        player.setCustomName(disguise.getActualName());
        player.setCustomNameVisible(false);
    }

    public boolean isDisguised(UUID actualUUID) {
        return getDisguise(actualUUID) != null;
    }

    public boolean isDisguised(Player player) {
        return this.isDisguised(player.getUniqueId());
    }

    public Disguise getDisguise(UUID actualUUID) {
        return this.disguises.stream().filter(d -> d.getActualUUID().equals(actualUUID)).findFirst().orElse(null);
    }

    public Disguise getDisguise(Player player) {
        return this.getDisguise(player.getUniqueId());
    }

}
