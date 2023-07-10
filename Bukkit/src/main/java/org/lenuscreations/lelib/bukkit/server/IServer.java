package org.lenuscreations.lelib.bukkit.server;

public interface IServer {

    int getPlayerCount();

    int getMaxPlayerCount();

    void broadcast(String message);

    void broadcast(String message, String permission);

    void setTabHeader(String header);

    void setTabFooter(String footer);

    // todo: add more features to make it easier to manage things via NMS and bukkit

}
