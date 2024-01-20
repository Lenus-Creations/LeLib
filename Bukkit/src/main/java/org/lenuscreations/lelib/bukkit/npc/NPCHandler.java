package org.lenuscreations.lelib.bukkit.npc;

import dev.grcq.v1_8_r3.npc.NPC_1_8_R3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;
import org.lenuscreations.lelib.bukkit.utils.Util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class NPCHandler {

    private final List<NPCData> npcs;

    public NPCHandler() {
        this.npcs = new ArrayList<>();
        this.loadNPCs();
        this.spawnNPCs();
    }

    /*
     * File template:
     * npcs:
     *      <npc-id>:
     *          name: <npc-name>
     *          skin: <npc-skin>
     *          location:
     *             world: <world-name>
     *             x: <x>
     *             y: <y>
     *             z: <z>
     *             yaw: <yaw>
     *             pitch: <pitch>
     *          # optional
     *          skin:
     *              name: <skin-name>
     *              value: <skin-value>
     *              signature: <skin-signature>
     */
    private void loadNPCs() {
        File file = new File(AbstractPlugin.getInstance().getDataFolder(), "npcs.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (!config.contains("npcs")) return;

        config.getConfigurationSection("npcs").getKeys(false).forEach(id -> {
            String name = config.getString("npcs." + id + ".name");
            String world = config.getString("npcs." + id + ".location.world");
            double x = config.getDouble("npcs." + id + ".location.x");
            double y = config.getDouble("npcs." + id + ".location.y");
            double z = config.getDouble("npcs." + id + ".location.z");
            float yaw = (float) config.getDouble("npcs." + id + ".location.yaw");
            float pitch = (float) config.getDouble("npcs." + id + ".location.pitch");

            NPCSkin npcSkin = null;
            if (config.contains("npcs." + id + ".skin")) {
                String skinName = config.getString("npcs." + id + ".skin.name");
                String skinValue = config.getString("npcs." + id + ".skin.value");
                String skinSignature = config.getString("npcs." + id + ".skin.signature");

                npcSkin = new NPCSkin(skinName, skinValue, skinSignature);
            }

            NPCData data = null;
            switch (Util.getNMSVersion()) {
                case "1_8_R3":
                    data = new NPCData(Integer.parseInt(id), new NPC_1_8_R3(
                            new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch),
                            Util.format(name),
                            npcSkin
                    ));
                    break;
            }

            npcs.add(data);
        });
    }

    public void spawnNPCs() {
        npcs.forEach(npc -> {

            try {
                Field field = npc.getClass().getDeclaredField("npc_" + Util.getNMSVersion().toLowerCase());
                field.setAccessible(true);

                Object object = field.get(npc);
                object.getClass().getDeclaredMethod("spawn").invoke(object);
            } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void createNPC(String name, Location location, NPCSkin skin) {
        NPCData data = null;
        switch (Util.getNMSVersion()) {
            case "1_8_R3":
                NPC_1_8_R3 npc_1_8_r3 = new NPC_1_8_R3(location, name, skin);
                npc_1_8_r3.spawn();

                data = new NPCData(npcs.size(), npc_1_8_r3);
                break;
        }

        npcs.add(data);
    }

    public void createNPC(String name, Location location) {
        createNPC(name, location, null);
    }

}
