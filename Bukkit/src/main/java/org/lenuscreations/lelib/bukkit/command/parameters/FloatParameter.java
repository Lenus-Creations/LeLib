package org.lenuscreations.lelib.bukkit.command.parameters;

import org.bukkit.command.CommandSender;
import org.lenuscreations.lelib.bukkit.utils.Util;
import org.lenuscreations.lelib.command.ParameterType;

public class FloatParameter implements ParameterType<Float, CommandSender> {

    @Override
    public Float parse(CommandSender sender, String source) {
        if (source.toLowerCase().contains("e")) {
            sender.sendMessage(Util.format("&cError: '&e" + source + "&c' is not a valid number."));
            return null;
        }

        try {
            float parsed = Float.parseFloat(source);

            if (Float.isNaN(parsed) || !Float.isFinite(parsed)) {
                sender.sendMessage(Util.format("&cError: '&e" + source + "&c' is not a valid number."));
                return null;
            }

            return parsed;
        } catch (NumberFormatException exception) {
            sender.sendMessage(Util.format("&cError: '&e" + source + "&c' is not a valid number."));
        }

        return null;
    }
}
