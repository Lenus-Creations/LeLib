package dev.grcq.v1_8_r3.tag;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TagHandler {

    public void setTag(Player player, JsonObject object) {
        JsonElement prefixElement = object.get("prefix");
        JsonElement suffixElement = object.get("suffix");
        JsonElement colourElement = object.get("colour");

        String prefix = (prefixElement == null || prefixElement.isJsonNull() ? null : prefixElement.getAsString());
        String suffix = (suffixElement == null || suffixElement.isJsonNull() ? null : suffixElement.getAsString());
        String colour = (colourElement == null || colourElement.isJsonNull() ? null : colourElement.getAsString());

        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        PacketPlayOutPlayerInfo removePlayer = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
        PacketPlayOutPlayerInfo addPlayer = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
        PacketPlayOutEntityDestroy entityDestroy = new PacketPlayOutEntityDestroy();
        PacketPlayOutNamedEntitySpawn entitySpawn = new PacketPlayOutNamedEntitySpawn(((CraftPlayer) player).getHandle());



        for (Player all : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) all).getHandle().playerConnection;

            connection.sendPacket(removePlayer);
            connection.sendPacket(addPlayer);
            connection.sendPacket(entityDestroy);
            connection.sendPacket(entitySpawn);
        }

    }

}
