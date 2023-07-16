package org.lenuscreations.lelib.bukkit.command.parameters;

import org.bukkit.command.CommandSender;
import org.lenuscreations.lelib.command.ParameterType;

import java.util.ArrayList;
import java.util.List;

public class StringParameter implements ParameterType<String, CommandSender> {

    @Override
    public String parse(CommandSender executor, String target) {
        return target;
    }

}
