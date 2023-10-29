package org.lenuscreations.velocity.command.parameters;

import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.lenuscreations.lelib.command.ParameterType;

public class FloatParameter implements ParameterType<Float, CommandSource> {

    @Override
    public Float parse(CommandSource sender, String source) {
        if (source.toLowerCase().contains("e")) {
            sender.sendMessage(Component.text("Error: '", NamedTextColor.RED)
                    .append(Component.text(source, NamedTextColor.YELLOW))
                    .append(Component.text("' is not a valid number.", NamedTextColor.RED)));
            return null;
        }

        try {
            float parsed = Float.parseFloat(source);

            if (Float.isNaN(parsed) || !Float.isFinite(parsed)) {
                sender.sendMessage(Component.text("Error: '", NamedTextColor.RED)
                        .append(Component.text(source, NamedTextColor.YELLOW))
                        .append(Component.text("' is not a valid number.", NamedTextColor.RED)));
                return null;
            }

            return parsed;
        } catch (NumberFormatException exception) {
            sender.sendMessage(Component.text("Error: '", NamedTextColor.RED)
                    .append(Component.text(source, NamedTextColor.YELLOW))
                    .append(Component.text("' is not a valid number.", NamedTextColor.RED)));
        }

        return null;
    }
}
