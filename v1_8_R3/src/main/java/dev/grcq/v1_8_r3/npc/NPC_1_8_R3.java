package dev.grcq.v1_8_r3.npc;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.apache.commons.lang3.Validate;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter
public class NPC_1_8_R3 {

    @NotNull
    private Location location;
    @NotNull
    private String name;

    @Nullable
    private GameProfile gameProfile;
    @Nullable
    private EntityPlayer npc;

    @Nullable
    private JsonObject skin;

    private boolean spawned = false;

    public NPC_1_8_R3(@NotNull Location location, @NotNull String name, @Nullable JsonObject skin) {
        Validate.notNull(name, "Name cannot be null");
        Validate.notNull(location, "Location cannot be null");
        Validate.isTrue(!name.isEmpty() && name.length() < 16, "Name must be between 1 and 16 characters");

        this.location = location;
        this.name = name;
        this.skin = skin;
    }

    public void spawn() {
        Validate.isTrue(!this.spawned, "NPC is already spawned");

        MinecraftServer server = MinecraftServer.getServer();
        WorldServer world = ((CraftWorld) this.location.getWorld()).getHandle();

        this.gameProfile = new GameProfile(UUID.randomUUID(), this.name);
        if (this.skin != null) {
            this.gameProfile.getProperties().put("textures", new Property("textures", this.skin.get("value").getAsString(), this.skin.get("signature").getAsString()));
        }

        this.npc = new EntityPlayer(server, world, gameProfile, new PlayerInteractManager(world));
        this.npc.setLocation(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());

        this.spawned = true;
    }

    public void remove() {
        Validate.isTrue(this.spawned, "NPC is not spawned");

        this.npc = null;
        this.gameProfile = null;

        this.spawned = false;
    }

    public void teleport(Location location) {
        Validate.isTrue(this.spawned, "NPC is not spawned");
        Validate.notNull(location, "Location cannot be null");

        this.location = location;

        remove();
        spawn();
    }

    public void rename(String name) {
        Validate.isTrue(this.spawned, "NPC is not spawned");
        Validate.notNull(name, "Name cannot be null");
        Validate.isTrue(!name.isEmpty() && name.length() < 16, "Name must be between 1 and 16 characters");

        this.name = name;

        remove();
        spawn();
    }

    public void setSkin(JsonObject skin) {
        Validate.isTrue(this.spawned, "NPC is not spawned");
        Validate.notNull(skin, "Skin cannot be null");

        this.skin = skin;

        remove();
        spawn();
    }

}
