package org.lenuscreations.lelib.bukkit.gui.defaults;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;
import org.lenuscreations.lelib.bukkit.gui.MenuItem;
import org.lenuscreations.lelib.bukkit.gui.PaginatedGUI;
import org.lenuscreations.lelib.bukkit.utils.Util;

import java.util.List;

public class NextPageItem implements MenuItem {

    private final PaginatedGUI to;

    public NextPageItem(PaginatedGUI to) {
        this.to = to;
        to.setPage(to.getPage() + 1);
    }

    @Override
    public String getName(Player player) {
        return Util.format("&aNext Page");
    }

    @Override
    public List<String> getLore(Player player) {
        return Util.format(Lists.newArrayList("&7Click here to go to page " + to.getPage()));
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.ARROW;
    }

    @Override
    public void onClick(Player player, ClickType clickType, int slot) {
        player.closeInventory();
        Bukkit.getScheduler().runTask(AbstractPlugin.getInstance(), () -> to.open(player));
    }
}
