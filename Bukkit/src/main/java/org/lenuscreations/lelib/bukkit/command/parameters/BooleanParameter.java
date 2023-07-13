package org.lenuscreations.lelib.bukkit.command.parameters;

import org.bukkit.command.CommandSender;
import org.lenuscreations.lelib.command.ParameterType;

import java.util.ArrayList;
import java.util.List;

public class BooleanParameter implements ParameterType<Boolean, CommandSender> {
    @Override
    public Boolean parse(CommandSender executor, String target) {
        try {
            return Boolean.parseBoolean(target);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<String> completer(CommandSender executor, String source) {
        return new ArrayList<>();
    }
}
