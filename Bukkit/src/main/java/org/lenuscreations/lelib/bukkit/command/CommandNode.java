package org.lenuscreations.lelib.bukkit.command;

import lombok.Data;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class CommandNode {

    private final String name;
    private final String description;
    private final String[] aliases;
    private final String permission;
    private boolean playerOnly;
    private boolean consoleOnly;

    private Map<String, CommandNode> children;

    public void execute(CommandSender sender, String[] args) {

    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        return list;
    }

}
