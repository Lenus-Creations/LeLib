package dev.grcq.v1_12_r1.tab;

import lombok.Data;
import lombok.SneakyThrows;
import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

@Data
public class TabHandler {

    private String header;
    private String footer;

    @SneakyThrows
    public void send(Player player) {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

        Field headerField = packet.getClass().getDeclaredField("a");
        headerField.setAccessible(true);
        headerField.set(packet, new ChatComponentText(header));

        Field footerField = packet.getClass().getDeclaredField("b");
        footerField.setAccessible(true);
        footerField.set(packet, new ChatComponentText(footer));

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}
