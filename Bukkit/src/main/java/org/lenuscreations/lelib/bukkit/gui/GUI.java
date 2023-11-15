package org.lenuscreations.lelib.bukkit.gui;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;
import org.lenuscreations.lelib.bukkit.utils.ItemBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class GUI {

    @Getter
    private static final Map<UUID, BukkitRunnable> runnableMap = new HashMap<>();
    @Getter
    private static final Map<UUID, GUI> guiMap = new HashMap<>();

    abstract public String getTitle(Player player);

    public int getSize(Player player) {
        return 9 * 2;
    }

    abstract public Map<Integer, MenuItem> getContent(Player player, Inventory inventory);

    public void onClose(Player player) {

    }

    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, getSize(player), getTitle(player));

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                getContent(player, inventory).forEach((slot, item) -> inventory.setItem(slot, toItemStack(player, item)));
            }
        };
        runnable.run();

        GUISettings settings = this.getClass().getDeclaredAnnotation(GUISettings.class);
        if (settings != null) {
            if (settings.autoUpdate()) {
                runnable.runTaskTimerAsynchronously(AbstractPlugin.getInstance(), settings.autoUpdateTime(), settings.autoUpdateTime());
            }
        }

        runnableMap.put(player.getUniqueId(), runnable);
        player.openInventory(inventory);
    }

    ItemStack toItemStack(Player player, MenuItem item) {
        ItemStack itemStack = new ItemStack(item.getMaterial(player), item.getAmount(player));
        itemStack.getData().setData(item.getData(player));

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(item.getName(player));
        meta.setLore(item.getLore(player));
        itemStack.setItemMeta(meta);

        item.getEnchantments(player).forEach(itemStack::addUnsafeEnchantment);
        return itemStack;
    }

}
