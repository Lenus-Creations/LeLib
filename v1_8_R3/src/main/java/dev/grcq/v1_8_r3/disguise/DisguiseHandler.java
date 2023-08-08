package dev.grcq.v1_8_r3.disguise;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.SneakyThrows;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class DisguiseHandler {

    @SneakyThrows
    public void disguise(Player player, JsonObject object) {
        JsonObject properties = object.get("properties").getAsJsonArray().get(0).getAsJsonObject();

        String texture = properties.get("value").getAsString();
        String signature = object.get("signature").getAsString();

        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        GameProfile profile = entityPlayer.getProfile();

        Field nameField = profile.getClass().getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(profile, object.get("username").getAsString());

        profile.getProperties().clear();
        profile.getProperties().put(
                "textures", new Property("textures", texture, signature)
        );

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.hidePlayer(player);
            p.showPlayer(player);
        });
    }

}
