package org.lenuscreations.lelib.bukkit.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemBuilder {

    private final ItemStack i;

    public ItemBuilder(ItemStack i) {
        this.i = i;
    }

    public ItemBuilder(Material material) {
        this.i = new ItemStack(material);
    }

    public ItemBuilder(Material material, int amount) {
        this.i = new ItemStack(material, amount);
    }

    public ItemBuilder(Material material, int amount, short damage) {
        this.i = new ItemStack(material, amount, damage);
    }

    public ItemBuilder setAmount(int amount) {
        i.setAmount(amount);
        return this;
    }

    public ItemBuilder setDurability(short durability) {
        i.setDurability(durability);
        return this;
    }


}
