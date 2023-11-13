package org.lenuscreations.lelib.bukkit.gui;

import org.bukkit.entity.Player;

public abstract class PaginatedGUI extends GUI {

    private int page;
    private int maxPage;

    abstract public String getTitlePerPage(Player player, int page);

    @Override
    public String getTitle(Player player) {
        return getTitlePerPage(player, 1) + "[" + page + "/" + maxPage + "]";
    }
}
