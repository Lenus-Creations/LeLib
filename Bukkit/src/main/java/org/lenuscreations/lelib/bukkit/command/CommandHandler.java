package org.lenuscreations.lelib.bukkit.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

public class CommandHandler {

    private CommandMap map;

    public CommandHandler() {
        this.map = Bukkit.getServer().getCommandMap();
    }

    public void register(CommandNode node) {
        map.register(node.getName(), new BukkitCommand(node));
    }

}
