package org.lenuscreations.lelib.bukkit.hologram;

import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.lenuscreations.lelib.bukkit.utils.Util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class HologramHandler {

    private final Map<Hologram, ArmorStand> holograms = new HashMap<>();

    public void spawn(Hologram hologram) {
        ArmorStand armorStand = hologram.getLocation().getWorld().spawn(hologram.getLocation(), ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setCustomName(Util.format(hologram.getTitle()));
        armorStand.setCustomNameVisible(true);
        armorStand.setGravity(false);
        armorStand.setCollidable(false);
        armorStand.setCanPickupItems(false);
    }

    @SneakyThrows
    public void setLocation(Hologram hologram, Location location) {
        ArmorStand armorStand = holograms.get(hologram);

        Field field = hologram.getClass().getDeclaredField("location");

        field.setAccessible(true);
        field.set(hologram, location);
        field.setAccessible(false);

        armorStand.remove();
        spawn(hologram);
    }

    public void setLocation(String title, Location location, boolean caseSensitive) {
        Hologram hologram = null;
        for (Hologram h : holograms.keySet()) {
            if (caseSensitive && Util.format(title).equals(h.getTitle())) {
                hologram = h;
                break;
            } else if (!caseSensitive && Util.format(title).equalsIgnoreCase(h.getTitle())) {
                hologram = h;
                break;
            }
        }

        this.setLocation(hologram, location);
    }

    public void setLocation(String title, Location location) {
        this.setLocation(title, location, true);
    }

}
