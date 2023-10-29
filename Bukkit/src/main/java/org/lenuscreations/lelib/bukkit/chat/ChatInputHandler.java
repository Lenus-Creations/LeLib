package org.lenuscreations.lelib.bukkit.chat;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.lenuscreations.lelib.LeLib;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;
import org.lenuscreations.lelib.bukkit.utils.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ChatInputHandler {

    public static Map<Player, BukkitRunnable> expiry = new HashMap<>();

    public static ChatInput sendInput(Player player, Consumer<ChatOutput> outputConsumer) {
        return sendInput(player, -1, outputConsumer);
    }

    public static ChatInput sendInput(Player player, long expireAfter, Consumer<ChatOutput> outputConsumer) {
        ChatInput input = new ChatInput(outputConsumer, expireAfter);
        sendInput(player, input);

        return input;
    }

    @SneakyThrows
    public static void sendInput(Player player, ChatInput input) {
        if (ChatInputListener.inputs.containsKey(player)) return;

        ChatInputListener.inputs.put(player, input);

        if (input.getExpireAfter() != -1L) {
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if (ChatInputListener.inputs.get(player) != null) {
                        ChatInputListener.inputs.remove(player);
                        player.sendMessage(Util.format("&cTime limit exceeded, please try again."));
                    }
                }
            };

            runnable.runTaskLater(AbstractPlugin.getInstance(), input.getExpireAfter());
            expiry.put(player, runnable);
        }
    }

}
