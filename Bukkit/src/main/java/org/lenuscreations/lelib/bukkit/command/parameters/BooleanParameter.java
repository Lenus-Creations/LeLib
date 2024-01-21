package org.lenuscreations.lelib.bukkit.command.parameters;

import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;
import org.lenuscreations.lelib.bukkit.utils.Util;
import org.lenuscreations.lelib.command.ParameterType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooleanParameter implements ParameterType<Boolean, CommandSender> {

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
    public Boolean parse(CommandSender sender, String target) {
        if (values.containsKey(target)) return values.get(target);

        sender.sendMessage(Util.format("&cError: Invalid argument."));
        return null;
    }

    @Override
    public List<String> completer(CommandSender executor, String source) {
        return Lists.newArrayList("true", "yes", "on", "false", "no", "off");
    }
}
