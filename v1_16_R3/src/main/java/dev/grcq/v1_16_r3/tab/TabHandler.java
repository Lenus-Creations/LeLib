package dev.grcq.v1_16_r3.tab;

import lombok.Data;
import org.bukkit.entity.Player;

@Data
public class TabHandler {

    private String header;
    private String footer;

    public void send(Player player) {
        player.setPlayerListHeaderFooter(header, footer);
    }

}
