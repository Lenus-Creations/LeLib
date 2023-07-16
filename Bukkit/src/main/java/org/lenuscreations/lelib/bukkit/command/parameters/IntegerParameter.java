package org.lenuscreations.lelib.bukkit.command.parameters;

import org.bukkit.command.CommandSender;
import org.lenuscreations.lelib.bukkit.utils.Util;
import org.lenuscreations.lelib.command.ParameterType;

public class IntegerParameter implements ParameterType<Integer, CommandSender> {

    @Override
    public Integer parse(CommandSender sender, String target) {
        try {
            return Integer.parseInt(target);
        } catch (NumberFormatException e) {
            sender.sendMessage(Util.format("&cError: Invalid number."));
            return null;
        }
    }

}
