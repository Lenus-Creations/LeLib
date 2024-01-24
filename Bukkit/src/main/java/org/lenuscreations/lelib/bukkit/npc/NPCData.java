package org.lenuscreations.lelib.bukkit.npc;

import dev.grcq.v1_8_r3.npc.NPC_1_8_R3;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.lenuscreations.lelib.bukkit.utils.Util;

public class NPCData {

    @Getter
    private final int id;
    private final NPC_1_8_R3 npc_1_8_r3;

    public NPCData(int id, NPC_1_8_R3 npc) {
        this.id = id;
        this.npc_1_8_r3 = npc;
    }

    public NPC_1_8_R3 getNPC_1_8_R3() {
        return this.npc_1_8_r3;
    }

    public String getName() {
        switch (Util.getNMSVersion()) {
            case "v1_8_R3":
                return this.npc_1_8_r3.getName();
            default:
                return null;
        }
    }

    public void rename(String name) {
        switch (Util.getNMSVersion()) {
            case "v1_8_R3":
                this.npc_1_8_r3.rename(name);
                break;
        }
    }

    public void setSkin(NPCSkin skin) {
        switch (Util.getNMSVersion()) {
            case "v1_8_R3":
                this.npc_1_8_r3.setSkin(skin.toJson());
                break;
        }
    }

    public void teleport(double x, double y, double z, float yaw, float pitch) {
        switch (Util.getNMSVersion()) {
            case "v1_8_R3":
                World world = this.npc_1_8_r3.getLocation().getWorld();
                this.npc_1_8_r3.teleport(new Location(world, x, y, z, yaw, pitch));
                break;
        }
    }

    public void teleport(World world, double x, double y, double z, float yaw, float pitch) {
        switch (Util.getNMSVersion()) {
            case "v1_8_R3":
                this.npc_1_8_r3.teleport(new Location(world, x, y, z, yaw, pitch));
                break;
        }
    }

    public void teleport(Location location) {
        switch (Util.getNMSVersion()) {
            case "v1_8_R3":
                this.npc_1_8_r3.teleport(location);
                break;
        }
    }

}
