package org.lenuscreations.lelib.bukkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.lenuscreations.lelib.bukkit.utils.Util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BukkitCommand extends Command {

    private final CommandNode node;

    public BukkitCommand(CommandNode node) {
        super(node.getName(), node.getDescription(), "/", Arrays.asList(node.getAliases()));

        this.node = node;

        if (!node.getPermission().isEmpty()) {
            setPermission(node.getPermission());
            setPermissionMessage(Util.format(CommandHandler.NO_PERMISSION_MESSAGE));
        }
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        try {
            node.execute(sender, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (!(sender instanceof Player)) return new ArrayList<>();

        return node.tabComplete(((Player) sender).getPlayer(), args);
    }
}
