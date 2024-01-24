package org.lenuscreations.lelib.bukkit.command.parameters;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.lenuscreations.lelib.bukkit.utils.Util;
import org.lenuscreations.lelib.command.ParameterType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerParameter implements ParameterType<Player, CommandSender> {

    @Override
    public Player parse(CommandSender sender, String target) {
        if (target.length() > 16) {
            Player player = Bukkit.getPlayer(UUID.fromString(target));
            if (player.hasPlayedBefore()) return player;

            sender.sendMessage(Util.format("&cError: Player not found."));
            return null;
        }
        Player player = Bukkit.getPlayer(target);
        if (player.hasPlayedBefore()) return player;

        sender.sendMessage(Util.format("&cError: Player not found."));
        return null;
    }

    @Override
    public List<String> completer(CommandSender executor, String source) {
        List<String> list = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            list.add(player.getName());
        }
        return list;
    }

}
