package org.lenuscreations.velocity.command.parameters;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.lenuscreations.lelib.command.ParameterType;
import org.lenuscreations.velocity.VAbstractPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerParameter implements ParameterType<Player, CommandSource> {

    @Override
    public Player parse(CommandSource sender, String target) {
        if (target.length() > 16) {
            Player player = VAbstractPlugin.getInstance().getServer().getPlayer(UUID.fromString(target)).orElse(null);
            if (player == null) {
                sender.sendMessage(Component.text("Error: Player not found.", NamedTextColor.RED));
                return null;
            }

            return player;
        }

        Player player = VAbstractPlugin.getInstance().getServer().getPlayer(target).orElse(null);
        if (player != null) return player;

        sender.sendMessage(Component.text("Error: Player not found.", NamedTextColor.RED));
        return null;
    }

    @Override
    public List<String> completer(CommandSource executor, String source) {
        List<String> list = new ArrayList<>();
        for (Player player : VAbstractPlugin.getInstance().getServer().getAllPlayers()) {
            list.add(player.getUsername());
        }
        return list;
    }

}
