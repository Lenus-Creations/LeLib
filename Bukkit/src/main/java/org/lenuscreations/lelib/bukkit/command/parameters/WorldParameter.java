package org.lenuscreations.lelib.bukkit.command.parameters;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.lenuscreations.lelib.bukkit.utils.Util;
import org.lenuscreations.lelib.command.ParameterType;

import java.util.ArrayList;
import java.util.List;

public class WorldParameter implements ParameterType<World, CommandSender> {

    @Override
    public World parse(CommandSender sender, String source) {
        World world = Bukkit.getWorld(source);
        if (world != null) return world;

        sender.sendMessage(Util.format("&cError: World '&e" + source + "&c' does not exist."));
        return null;
    }

    @Override
    public List<String> completer(CommandSender sender, String source) {
        List<String> arguments = new ArrayList<>();

        for (World world : Bukkit.getWorlds()) {
            arguments.add(world.getName());
        }

        return arguments;
    }
}
