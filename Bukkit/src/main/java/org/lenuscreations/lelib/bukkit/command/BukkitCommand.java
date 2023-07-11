package org.lenuscreations.lelib.bukkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class BukkitCommand extends Command {

    private final CommandNode node;

    public BukkitCommand(CommandNode node) {
        super(node.getName(), node.getDescription(), "/", Arrays.asList(node.getAliases()));

        this.node = node;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        node.execute(sender, args);
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return super.tabComplete(sender, alias, args);
    }
}
