package org.lenuscreations.lelib.bukkit.event.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;
import org.lenuscreations.lelib.bukkit.event.EventListener;
import org.lenuscreations.lelib.bukkit.nick.NickData;
import org.lenuscreations.lelib.bukkit.nick.NicknameHandler;

@EventListener(event = PlayerJoinEvent.class)
public class JoinListener {

    public static void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        NicknameHandler handler = AbstractPlugin.getInstance().getNicknameHandler();
        if (handler.isNicked(player)) {
            handler.nick(new NickData(player.getUniqueId()));
        }
    }

}
