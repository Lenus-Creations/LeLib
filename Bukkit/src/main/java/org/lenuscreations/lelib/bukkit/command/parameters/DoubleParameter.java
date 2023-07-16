package org.lenuscreations.lelib.bukkit.command.parameters;

import org.bukkit.command.CommandSender;
import org.lenuscreations.lelib.bukkit.utils.Util;
import org.lenuscreations.lelib.command.ParameterType;

public class DoubleParameter implements ParameterType<Double, CommandSender> {

    @Override
    public Double parse(CommandSender sender, String source) {
        if (source.toLowerCase().contains("e")) {
            sender.sendMessage(Util.format("&cError: '&e" + source + "&c' is not a valid number."));
            return null;
        }

        try {
            double parsed = Double.parseDouble(source);

            if (Double.isNaN(parsed) || !Double.isFinite(parsed)) {
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
