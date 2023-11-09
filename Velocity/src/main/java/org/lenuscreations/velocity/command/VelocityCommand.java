package org.lenuscreations.velocity.command;

import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class VelocityCommand implements SimpleCommand {

    private final CommandNode node;

    public VelocityCommand(CommandNode node) {
        this.node = node;
    }

    @Override
    public void execute(Invocation invocation) {
        try {
            node.execute(invocation.source(), invocation.arguments());
        } catch (InvocationTargetException | IllegalAccessException e) {
            invocation.source().sendMessage(Component.text("An error occurred whilst executing this command.", NamedTextColor.RED));
            if (e.getMessage() != null) invocation.source().sendMessage(Component.text(e.getMessage(), NamedTextColor.RED));
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return node.suggest(invocation.source(), invocation.arguments());
    }

}
