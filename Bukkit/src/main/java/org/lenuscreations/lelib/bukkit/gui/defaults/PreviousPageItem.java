package org.lenuscreations.lelib.bukkit.gui.defaults;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.lenuscreations.lelib.bukkit.gui.MenuItem;
import org.lenuscreations.lelib.bukkit.gui.PaginatedGUI;

import java.util.List;

public class PreviousPageItem implements MenuItem {

    private final PaginatedGUI prev;

    public PreviousPageItem(PaginatedGUI prev) {
        this.prev = prev;
    }

    @Override
    public String getName(Player player) {
        return null;
    }

    @Override
    public List<String> getLore(Player player) {
        return null;
    }

    @Override
    public Material getMaterial(Player player) {
        return null;
    }

    @Override
    public int getAmount(Player player) {
        return 0;
    }

    @Override
    public byte getData(Player player) {
        return 0;
    }

    @Override
    public void onClick(Player player, ClickType clickType, int slot) {

    }
}
