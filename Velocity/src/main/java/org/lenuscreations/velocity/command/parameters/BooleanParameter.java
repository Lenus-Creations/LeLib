package org.lenuscreations.velocity.command.parameters;

import com.google.common.collect.Lists;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.lenuscreations.lelib.command.ParameterType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooleanParameter implements ParameterType<Boolean, CommandSource> {

    private Map<String, Boolean> values = new HashMap<>();

    public BooleanParameter() {
        values.put("true", true);
        values.put("yes", true);
        values.put("1", true);
        values.put("on", true);
        values.put("y", true);
        values.put("false", false);
        values.put("no", false);
        values.put("0", false);
        values.put("off", false);
        values.put("n", false);
    }

    @Override
    public Boolean parse(CommandSource sender, String target) {
        if (values.containsKey(target)) return values.get(target);

        sender.sendMessage(Component.text("Invalid argument. Expected: boolean.", NamedTextColor.RED));
        return null;
    }

    @Override
    public List<String> completer(CommandSource executor, String source) {
        return Lists.newArrayList("true", "yes", "on", "false", "no", "off");
    }
}
