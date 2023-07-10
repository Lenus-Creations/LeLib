package org.lenuscreations.lelib.bukkit.server;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public interface IServer {

    int getPlayerCount();

    int getMaxPlayerCount();

    void broadcast(String message);

    void broadcast(String message, String permission);

    void setTabHeader(String header);

    void setTabFooter(String footer);

    void log(String message);

    void send(CommandSender sender, String message);

    ConsoleCommandSender getConsole();



    // todo: add more features to make it easier to manage things via NMS and bukkit

}
