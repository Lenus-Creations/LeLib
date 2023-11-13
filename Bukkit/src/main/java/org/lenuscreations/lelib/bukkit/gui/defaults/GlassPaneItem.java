package org.lenuscreations.lelib.bukkit.gui.defaults;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.material.MaterialData;
import org.lenuscreations.lelib.bukkit.gui.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class GlassPaneItem implements MenuItem {

    @Override
    public String getName(Player player) {
        return " ";
    }

    @Override
    public List<String> getLore(Player player) {
        return new ArrayList<>();
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.STAINED_GLASS_PANE;
    }

    @Override
    public int getAmount(Player player) {
        return 1;
    }

    @Override
    public byte getData(Player player) {
        return 15;
    }

    @Override
    public void onClick(Player player, ClickType clickType, int slot) {

    }
}
