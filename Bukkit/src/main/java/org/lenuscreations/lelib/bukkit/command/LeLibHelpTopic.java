package org.lenuscreations.lelib.bukkit.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;

public class LeLibHelpTopic extends HelpTopic {

    private final CommandNode node;

    public LeLibHelpTopic(CommandNode node) {
        this.node = node;
        Bukkit.getServer().getHelpMap().addTopic(this);
    }

    @Override
    public boolean canSee(CommandSender player) {
        return node.canAccess(player);
    }

    @Override
    public String getName() {
        return node.getName();
    }

    @Override
    public String getShortText() {
        return node.getDescription();
    }

    @Override
    public String getFullText(CommandSender forWho) {
        return node.getName() + " - " + node.getDescription();
    }
}
