package org.lenuscreations.lelib.bukkit.gui;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MenuItem {

    String getName(Player player);

    List<String> getLore(Player player);

    Material getMaterial(Player player);

    int getAmount(Player player);

    byte getData(Player player);

    void onClick(Player player, ClickType clickType, int slot);

    default boolean cancelClick() {
        return true;
    }

    /**
     * @return A map of enchantments with levels
     */
    default Map<Enchantment, Integer> getEnchantments(Player player) {
        return new HashMap<>();
    }

}
