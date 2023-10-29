package org.lenuscreations.velocity.command.parameters;

import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.lenuscreations.lelib.command.ParameterType;

public class IntegerParameter implements ParameterType<Integer, CommandSource> {

    @Override
    public Integer parse(CommandSource sender, String target) {
        try {
            return Integer.parseInt(target);
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Invalid number.", NamedTextColor.RED));
            return null;
        }
    }

}
