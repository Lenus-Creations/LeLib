package org.lenuscreations.lelib.bukkit.test_commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;
import org.lenuscreations.lelib.bukkit.gui.GUIHandler;
import org.lenuscreations.lelib.bukkit.gui.example.ExampleGUI;

public class TestCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        Player player = (Player) commandSender;
        GUIHandler guiHandler = AbstractPlugin.getInstance().getGuiHandler();
        guiHandler.openGUI(player, ExampleGUI.class);
        return false;
    }
}
