package org.lenuscreations.lelib.bukkit.gui.test;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.lenuscreations.lelib.bukkit.gui.GUI;
import org.lenuscreations.lelib.bukkit.gui.MenuItem;
import org.lenuscreations.lelib.bukkit.gui.defaults.GlassPaneItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUITest extends GUI {


    @Override
    public String getTitle(Player player) {
        return "Test";
    }

    @Override
    public Map<Integer, MenuItem> getContent(Player player, Inventory inventory) {
        Map<Integer, MenuItem> buttons = new HashMap<>();
        buttons.put(2, new GlassPaneItem());
        return buttons;
    }

    public static class TestItem implements MenuItem {

        @Override
        public String getName(Player player) {
            return "Test";
        }

        @Override
        public List<String> getLore(Player player) {
            return Lists.newArrayList("eeee");
        }

        @Override
        public Material getMaterial(Player player) {
            return Material.WOOD;
        }

        @Override
        public int getAmount(Player player) {
            return 5;
        }

        @Override
        public void onClick(Player player, ClickType clickType, int slot) {
            player.sendMessage("Test");
        }

    }
}
