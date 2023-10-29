package org.lenuscreations.velocity.command.test;

import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import org.lenuscreations.lelib.command.Command;

public class TestCmd {

    @Command(name = "hello")
    public void hello(CommandSource sender) {
        sender.sendMessage(Component.text("Hello world!"));
    }

}
