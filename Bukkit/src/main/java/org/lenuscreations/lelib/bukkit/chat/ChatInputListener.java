package org.lenuscreations.lelib.bukkit.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class ChatInputListener implements Listener {

    public static Map<Player, ChatInput> inputs = new HashMap<>();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent e) {
        ChatInput input = inputs.get(e.getPlayer());
        if (input == null) return;

        e.setCancelled(true);

        ChatOutput output = new ChatOutput(e.getPlayer(), e.getMessage(), System.currentTimeMillis() - input.getStartTime());
        input.getOutputConsumer().accept(output);

        BukkitRunnable runnable = ChatInputHandler.expiry.get(e.getPlayer());
        if (runnable != null) {
            runnable.cancel();
            ChatInputHandler.expiry.remove(e.getPlayer());
        }

        inputs.remove(e.getPlayer());
    }

}
