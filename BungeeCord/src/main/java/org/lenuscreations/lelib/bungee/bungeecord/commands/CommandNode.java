package org.lenuscreations.lelib.bungee.bungeecord.commands;

import lombok.Data;

@Data
public class CommandNode {

    private final String name;
    private final String[] aliases;
    private final String permission;

}
